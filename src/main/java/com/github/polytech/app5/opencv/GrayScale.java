package com.github.polytech.app5.opencv;

import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public final class GrayScale extends Operation<GrayScaleState> {
    @Override
    public void accept(final GrayScaleState state) {
        System.out.println("GrayScale operation.");
        loadImage.apply(state.inputPath).andThen(input -> {
            Imgproc.threshold(input, input, state.scale, 255, Imgproc.THRESH_BINARY);
            return input;
        }).andThen(saveImage.apply(state.outputPath)).apply(Highgui.CV_LOAD_IMAGE_GRAYSCALE);

    }

    @Override
    protected GrayScaleState initialState() {
        return new GrayScaleState(0, 0, "", "");
    }

}