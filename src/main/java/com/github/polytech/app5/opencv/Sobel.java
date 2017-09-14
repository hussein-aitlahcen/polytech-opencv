package com.github.polytech.app5.opencv;

public final class Sobel extends Operation<SobelState> {

    @Override
    public void accept(final SobelState state) throws Exception {
        System.out.println("Sobel operation.");
    }

    @Override
    protected SobelState initialState() {
        return new SobelState(0, "", "");
    }

}