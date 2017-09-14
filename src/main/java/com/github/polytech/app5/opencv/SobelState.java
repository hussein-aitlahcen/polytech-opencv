package com.github.polytech.app5.opencv;

public final class SobelState extends State<SobelState> {
    public final String inputPath;
    public final String outputPath;

    public SobelState(final int offset, final String inputPath, final String outputPath) {
        super(offset);
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    @Override
    public SobelState reduce(final String nextArg) {
        switch (this.offset) {
        case 0:
            return new SobelState(this.offset + 1, nextArg, this.outputPath);
        case 1:
            return new SobelState(this.offset + 1, this.inputPath, nextArg);
        }
        return this;
    }
}