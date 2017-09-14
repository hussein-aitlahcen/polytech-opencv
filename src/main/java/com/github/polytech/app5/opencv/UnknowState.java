package com.github.polytech.app5.opencv;

public final class UnknowState extends State<UnknowState> {

    public UnknowState(final int offset) {
        super(offset);
    }

    @Override
    public UnknowState reduce(String nextArg) {
        return new UnknowState(this.offset + 1);
    }
}