package com.example.pass.observers.base;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public class BaseObserverable<T> implements Observerable<T> {

    private List<Observer<T>> list;

    public BaseObserverable() {
        list = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer<T> observer) {
        list.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<T> observer) {
        list.remove(observer);
    }

    @Override
    public void notifyObserver(T msg) {
        for (Observer<T> tObserver : list) {
            tObserver.update(msg);
        }
    }
}
