package com.github.polytech.app5.opencv;

public final class QualityIndicator {
    public final int threshold;
    public final int p;
    public final int r;
    public final int tp;
    public final int fp;
    public final int tn;
    public final int fn;

    public QualityIndicator(final int threshold, final int tp, final int fp, final int tn, final int fn) {
        this.threshold = threshold;
        this.tp = tp;
        this.fp = fp;
        this.tn = fn;
        this.fn = fn;
        this.p = (int) Math.floor(tp / Math.max(1, (double) (tp + fp)) * 100);
        this.r = (int) Math.floor(tp / Math.max(1, (double) (tp + fn)) * 100);
    }

    @Override
    public final String toString() {
        return String.format("TRESH=%d, TP=%d, FP=%d, TN=%d, FN=%d\nP=%d, R=%d", this.threshold, this.tp, this.fp,
                this.tn, this.fn, this.p, this.r);
    }
}