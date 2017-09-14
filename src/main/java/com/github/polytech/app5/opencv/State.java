package com.github.polytech.app5.opencv;

public abstract class State<T extends State<T>> {
    protected final int offset;

    public State(final int offset) {
        this.offset = offset;
    }

    public abstract T reduce(final String nextArg);
}