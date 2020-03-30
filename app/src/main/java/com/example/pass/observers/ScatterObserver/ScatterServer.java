package com.example.pass.observers.ScatterObserver;

import com.example.pass.observers.base.Observer;
import com.example.pass.observers.base.Observerable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScatterServer implements Observerable<String> {

    private List<Observer<String>> list;

    //饿汉单例模式
    private ScatterServer(){
        list = new ArrayList<>();
    }

    //创建类的唯一实例
    private static ScatterServer instance = new ScatterServer();

    //提供一个获取实例的方法，使用public static修饰
    public static ScatterServer getInstance(){
        return instance;
    }

    @Override
    public void registerObserver(Observer<String> observer) {
        list.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<String> observer) {
        if (!list.isEmpty()){
            list.remove(observer);
        }
    }

    @Override
    public void notifyObserver(String msg) {
        for (Observer<String> stringObserver : list) {
            stringObserver.update(msg);
        }
    }

    /**
     * 发布通知
     * @param msg 通知
     */
    public void anounceInformation(String msg){
        notifyObserver(msg);
    }



}
