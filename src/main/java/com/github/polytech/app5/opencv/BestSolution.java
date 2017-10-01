package com.github.polytech.app5.opencv;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public final class BestSolution extends Operation<BestSolutionState> {

    @Override
    public void accept(final BestSolutionState state) {
        loadImage.apply(state.input).andThen(input -> loadImage.apply(state.solution).andThen(solution -> {
            Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);
            final Mat sobel = computeSobel(input);
            final Mat laplacian = computeLaplacian(input);
            final Mat combined = combine(laplacian, sobel);
            return combined;
        }).andThen(saveImage.apply(state.output)).apply(Highgui.CV_LOAD_IMAGE_GRAYSCALE))
                .apply(Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }

    @Override
    protected BestSolutionState initialState() {
        return new BestSolutionState(0, "", "", "");
    }

}