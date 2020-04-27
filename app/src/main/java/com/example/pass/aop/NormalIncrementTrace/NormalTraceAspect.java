package com.example.pass.aop.NormalIncrementTrace;


import android.util.Log;

import com.example.pass.aop.annotations.BehaviorTrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class NormalTraceAspect {

    @Pointcut("execution(@com.example.pass.aop.NormalIncrementTrace.NormalTrace * *(..))")
    public void methodAnnotatedWithNormalTrace(){}

    @Around("methodAnnotatedWithBehaviorTrace()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String value = methodSignature.getMethod().getAnnotation(NormalTrace.class).value() ;
        Object result = joinPoint.proceed();//这个方法执行

        //埋点计数


        return result;
    }


}
