package com.github.polytech.app5.opencv;

public final class EvaluateState extends State<EvaluateState> {

    public final String truthPath;
    public final String detectionPath;

    public EvaluateState(final int offset, final String truthPath, final String detectionPath) {
        super(offset);
        this.truthPath = truthPath;
        this.detectionPath = detectionPath;
    }

    @Override
    public EvaluateState reduce(final String nextArg) {
        switch (this.offset) {
        case 0:
            return new EvaluateState(this.offset + 1, nextArg, this.detectionPath);
        case 1:
            return new EvaluateState(this.offset + 1, this.truthPath, nextArg);
        }
        return this;
    }
}