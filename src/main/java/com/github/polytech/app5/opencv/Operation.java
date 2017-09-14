package com.github.polytech.app5.opencv;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public abstract class Operation<T extends State<T>> implements Consumer<T> {

    public static <T> Observable<T> streamToObservable(final Stream<T> stream) {
        return Observable.fromIterable(stream::iterator);
    }

    protected abstract T initialState();

    public void execute(final Observable<String> arguments) {
        arguments.reduce(initialState(), (final T state, final String nextArg) -> state.reduce(nextArg))
                .doAfterSuccess(this::accept).subscribe();
    }
}