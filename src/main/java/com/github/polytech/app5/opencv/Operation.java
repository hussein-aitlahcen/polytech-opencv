package com.github.polytech.app5.opencv;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import io.reactivex.Observable;

public abstract class Operation<T extends State<T>> implements Consumer<T> {

    public static <T> Observable<T> streamToObservable(final Stream<T> stream) {
        return Observable.fromIterable(stream::iterator);
    }

    public static Function<String, Function<Integer, Mat>> loadImage = inputPath -> {
        return flag -> Highgui.imread(inputPath, flag);
    };

    public static Function<Mat, Mat> copyMatSize = mat -> Mat.zeros(mat.height(), mat.height(), CvType.CV_64F);

    public static Function<String, Function<Mat, Boolean>> saveImage = outputPath -> {
        return image -> Highgui.imwrite(outputPath, image);
    };

    public static Mat copyApply(final Mat input, final Consumer<Mat> f) {
        final Mat output = Mat.zeros(input.rows(), input.cols(), CvType.CV_64F);
        f.accept(output);
        return output;
    }

    /*
        Calcul du laplacien de l'image qui retourne une image binaire
    */
    public static Mat computeLaplacian(final Mat input) {
        final Mat laplacian = copyApply(input,
                output -> Imgproc.Laplacian(input, output, CvType.CV_64F, 1, 1, 0, Imgproc.BORDER_DEFAULT));
        return copyApply(laplacian, output -> {
            for (int i = 0; i < laplacian.rows(); i++) {
                for (int j = 0; j < laplacian.cols(); j++) {
                    final double e = getPixelValue(laplacian, i + 1, j);
                    final double w = getPixelValue(laplacian, i - 1, j);
                    final double n = getPixelValue(laplacian, i, j + 1);
                    final double s = getPixelValue(laplacian, i, j - 1);
                    final double c = getPixelValue(laplacian, i, j);
                    final double threshold = 50;
                    // changement de signe vertical (voisin nord/sud)
                    if (signChanged(n, s)) {
                        final double variation = n > s ? n - s : s - n;
                        /* 
                            Si la valeur du pixel courant est inférieure aux voisins,
                            c'est un passage par zéro.
                            La deuxième condition permet de s'assurer que la variation dépasse le seuil.
                        */
                        if (isAbsLess(c, n) && isAbsLess(c, s) && variation > threshold) {
                            output.put(i, j, 255);
                        }
                        // changement de signe horizontal (voisin est/ouest)
                    } else if (signChanged(e, w)) {
                        final double variation = e > w ? e - w : w - e;
                        if (isAbsLess(c, e) && isAbsLess(c, w) && variation > threshold) {
                            output.put(i, j, 255);
                        }
                    }
                }
            }
        });
    }

    private static boolean isAbsLess(final double a, final double b) {
        return Math.abs(a) < Math.abs(b);
    }

    private static boolean signChanged(final double a, final double b) {
        return a * b < 0;
    }

    private static double getPixelValue(final Mat matrix, final int i, final int j) {
        if (isOutOfBounds(matrix, i, j) || isOutOfBounds(matrix, i, j)) {
            return 0;
        }
        return matrix.get(i, j)[0];
    }

    private static boolean isOutOfBounds(final Mat mat, final int x, final int y) {
        return (x < 0 || x >= mat.rows() || y < 0 || y >= mat.cols());
    }

    // Retourne la magnitude maxi
    public static double getMaxMagnitude(final Mat magnitude) {
        double maxMagnitude = Integer.MIN_VALUE;
        for (int i = 0; i < magnitude.rows(); i++) {
            for (int j = 0; j < magnitude.cols(); j++) {
                final double magnitudeValue = magnitude.get(i, j)[0];
                if (magnitudeValue > maxMagnitude) {
                    maxMagnitude = magnitudeValue;
                }
            }
        }
        return maxMagnitude;
    }

    /*
        Calcul le Sobel X/Y d'une image, combine les deux matrices pour calculer le gradient
        et finalement retourne une image binaire contenant les pixels dépassant le seuil de magnitude.
    */
    public static Mat computeSobel(final Mat input) {
        final Mat sobelX = copyApply(input, output -> Imgproc.Sobel(input, output, input.depth(), 1, 0));
        final Mat sobelY = copyApply(input, output -> Imgproc.Sobel(input, output, input.depth(), 0, 1));
        final Mat magnitude = copyApply(input, output -> {
            for (int i = 0; i < input.rows(); i++) {
                for (int j = 0; j < input.cols(); j++) {
                    output.put(i, j, Math.sqrt(Math.pow(sobelX.get(i, j)[0], 2) + Math.pow(sobelY.get(i, j)[0], 2)));
                }
            }
        });
        return copyApply(input, output -> {

            // Seul les pixels dont la magnitude dépasse max*0.6 seront gardés.
            final double threshold = getMaxMagnitude(magnitude) * 0.6;
            for (int i = 0; i < input.rows(); i++) {
                for (int j = 0; j < input.cols(); j++) {
                    final double magn = magnitude.get(i, j)[0];
                    if (magn > threshold) {
                        output.put(i, j, 255);
                    }
                }
            }
        });
    }

    public static Mat combine(final Mat a, final Mat b) {
        return copyApply(a, output -> Core.bitwise_and(a, b, output));
    }

    public static QualityIndicator computeQuality(final int gray, final Mat computed, final Mat solution,
            final int radius) {
        int TP = 0, FP = 0, TN = 0, FN = 0;
        for (int i = 0; i < computed.rows(); i++) {
            for (int j = 0; j < computed.cols(); j++) {
                final double solutionPixel = solution.get(i, j)[0];
                if (solutionPixel == 255) {
                    if (hasNeighbourValue(computed, i, j, 255, radius)) {
                        TP++;
                    } else {
                        FN++;
                    }
                } else {
                    if (computed.get(i, j)[0] == 0) {
                        TN++;
                    } else {
                        FP++;
                    }
                }
            }
        }
        return new QualityIndicator(gray, TP, FP, TN, FN);
    }

    private static boolean hasNeighbourValue(final Mat matrice, final int i, final int j, final int value,
            final int radius) {
        final double pixel = matrice.get(i, j)[0];
        if (pixel == value) {
            return true;
        }
        for (final Point point : getRadius(i, j, radius)) {
            if (point.x >= 0 && point.y >= 0 && point.x < matrice.rows() && point.y < matrice.cols()) {
                if (matrice.get((int) point.x, (int) point.y)[0] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ArrayList<Point> getRadius(final int x, final int y, final int radius) {
        final ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < radius; i++) {
            points.add(new Point(x + i, y));
            points.add(new Point(x - i, y));
            points.add(new Point(x, y + i));
            points.add(new Point(x, y - i));
            points.add(new Point(x + i, y + i));
            points.add(new Point(x + i, y - i));
            points.add(new Point(x - i, y - i));
            points.add(new Point(x - i, y + i));
        }
        return points;
    }

    protected abstract T initialState();

    public void execute(final Observable<String> arguments) {
        arguments.reduce(initialState(), (final T state, final String nextArg) -> state.reduce(nextArg))
                .doAfterSuccess(this::accept).subscribe();
    }
}