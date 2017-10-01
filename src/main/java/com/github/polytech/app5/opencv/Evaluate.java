package com.github.polytech.app5.opencv;

import org.opencv.highgui.Highgui;

public final class Evaluate extends Operation<EvaluateState> {

    @Override
    public void accept(final EvaluateState state) {
        System.out.println("Evaluate operation.");
        loadImage.apply(state.detectionPath).andThen(detection -> loadImage.apply(state.truthPath).andThen(truth -> {
            final QualityIndicator quality = computeQuality(0, detection, truth, 3);
            System.out.println(quality);
            System.out.println(quality.p);
            System.out.println(quality.r);
            return truth;
        }).apply(Highgui.CV_LOAD_IMAGE_ANYCOLOR)).apply(Highgui.CV_LOAD_IMAGE_ANYCOLOR);
    }

    @Override
    protected EvaluateState initialState() {
        return new EvaluateState(0, "", "");
    }
}