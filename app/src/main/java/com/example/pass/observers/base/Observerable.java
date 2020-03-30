package com.example.pass.observers.base;

public interface Observerable<T> {

    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObserver(T msg);

}
