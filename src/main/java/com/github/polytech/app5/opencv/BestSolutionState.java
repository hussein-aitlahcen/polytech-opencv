package com.github.polytech.app5.opencv;

public final class BestSolutionState extends State<BestSolutionState> {

    public final String input;
    public final String solution;
    public final String output;

    public BestSolutionState(final int offset, final String input, final String solution, final String output) {
        super(offset);
        this.input = input;
        this.solution = solution;
        this.output = output;
    }

    @Override
    public BestSolutionState reduce(final String nextArg) {
        switch (this.offset) {
        case 0:
            return new BestSolutionState(this.offset + 1, nextArg, this.solution, this.output);
        case 1:
            return new BestSolutionState(this.offset + 1, this.input, nextArg, this.output);
        case 2:
            return new BestSolutionState(this.offset + 1, this.input, this.solution, nextArg);
        }
        return this;
    }

}