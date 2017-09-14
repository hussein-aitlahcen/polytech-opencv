package com.github.polytech.app5.opencv;

public final class Evaluate extends Operation<EvaluateState> {

    @Override
    public void accept(final EvaluateState state) throws Exception {
        System.out.println("Evaluate operation.");
    }

    @Override
    protected EvaluateState initialState() {
        return new EvaluateState(0, "", "");
    }
}