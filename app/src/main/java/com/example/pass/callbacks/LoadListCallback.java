package com.example.pass.callbacks;

import java.util.List;

public interface LoadListCallback<T> {
    void onStart();
    void onFinish(List<T> list);
    void onError(String error);
}
