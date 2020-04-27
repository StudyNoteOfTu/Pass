package com.example.pass.aop.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//性能监控
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BehaviorTrace {

    String value();

}
