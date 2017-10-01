package com.github.polytech.app5.opencv;

import org.opencv.highgui.Highgui;

public final class Sobel extends Operation<SobelState> {

    @Override
    public void accept(final SobelState state) {
        System.out.println("Sobel operation.");
        loadImage.apply(state.inputPath).andThen(input -> {
            return computeSobel(input);
        }).andThen(saveImage.apply(state.outputPath)).apply(Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }

    @Override
    protected SobelState initialState() {
        return new SobelState(0, "", "");
    }

}