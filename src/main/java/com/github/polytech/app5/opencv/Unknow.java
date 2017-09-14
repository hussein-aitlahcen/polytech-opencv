package com.github.polytech.app5.opencv;

public final class Unknow extends Operation<UnknowState> {
    @Override
    public void accept(final UnknowState state) throws Exception {
        System.out.println("Unknow operation.");
    }

    @Override
    protected UnknowState initialState() {
        return new UnknowState(0);
    }
}