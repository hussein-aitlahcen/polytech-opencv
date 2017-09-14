package com.github.polytech.app5.opencv;

public enum OperationType {
    UNKNOWN(-1, new Unknow()), GRAY_FLOOR(0, new GrayScale()), SOBEL(1, new Sobel()), EVALUATE(2, new Evaluate());

    private final int id;
    private final Operation<?> function;

    private OperationType(final int id, final Operation<?> function) {
        this.id = id;
        this.function = function;
    }

    public Operation<?> function() {
        return this.function;
    }

    public static OperationType ofId(final int id) {
        for (final OperationType operation : values()) {
            if (operation.id == id) {
                return operation;
            }
        }
        return OperationType.UNKNOWN;
    }
}