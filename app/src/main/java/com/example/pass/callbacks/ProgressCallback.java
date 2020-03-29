package com.example.pass.callbacks;

public interface ProgressCallback {
    void onStart();
    void onFinish(String result);
    void onError(String error);
}
