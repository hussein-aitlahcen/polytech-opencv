package com.github.polytech.app5.opencv;

public final class GrayScale extends Operation<GrayScaleState> {
    @Override
    public void accept(final GrayScaleState state) throws Exception {
        System.out.println("GrayScale operation.");
    }

    @Override
    protected GrayScaleState initialState() {
        return new GrayScaleState(0, 0, "", "");
    }

}