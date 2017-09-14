package com.github.polytech.app5.opencv;

public final class GrayScaleState extends State<GrayScaleState> {

    public final int scale;
    public final String inputPath;
    public final String outputPath;

    public GrayScaleState(final int offset, final int scale, final String inputPath, final String outputPath) {
        super(offset);
        this.scale = scale;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    @Override
    public GrayScaleState reduce(final String nextArg) {
        switch (this.offset) {
        case 0:
            return new GrayScaleState(this.offset + 1, Integer.parseInt(nextArg), this.inputPath, this.outputPath);
        case 1:
            return new GrayScaleState(this.offset + 1, this.scale, nextArg, this.outputPath);
        case 2:
            return new GrayScaleState(this.offset + 1, this.scale, this.inputPath, nextArg);
        }
        return this;
    }
}