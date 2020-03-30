package com.example.pass.callbacks;

public interface LoadObjectCallback<T> {
    void onStart();
    void onFinish(T result);
    void onError(String error);
}
