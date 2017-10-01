package com.github.polytech.app5.opencv;

import nu.pattern.OpenCV;

import io.reactivex.*;

public final class App {
    public static void main(final String[] args) throws Exception {
        OpenCV.loadLibrary();
        if (args.length == 0) {
            System.out.println("Aucun paramètre en entrée. Exemple: java x <operationId> <arg1> <arg2> ...");
        } else {
            Observable.fromArray(args).take(1).map(Integer::parseInt).map(OperationType::ofId)
                    .map(OperationType::function).doOnNext(fun -> fun.execute(Observable.fromArray(args).skip(1)))
                    .subscribe();
        }
    }
}
