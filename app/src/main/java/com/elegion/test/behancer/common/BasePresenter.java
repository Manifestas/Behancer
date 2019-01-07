package com.elegion.test.behancer.common;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void disposeAll() {
        compositeDisposable.clear();
    }
}
