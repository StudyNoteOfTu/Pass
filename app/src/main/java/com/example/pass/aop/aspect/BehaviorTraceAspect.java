package com.example.pass.aop.aspect;


import android.util.Log;

import com.example.pass.aop.annotations.BehaviorTrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

//声明为切面
@Aspect
public class BehaviorTraceAspect {
    //定义切面的规则
    //1. 就在原来的应用中哪些注解的地方放到当前切面进行处理


    /**
     * 其中@Pointcut表示切入点
     * 要进入切面，都会进入这个方法
     * execution(注解名字 注释用的地方(用*表示所有都可以） *(..) 表示不限方法，参数多少都可以)
     */
    @Pointcut("execution(@com.example.pass.aop.annotations.BehaviorTrace * *(..))")
    public void methodAnnotatedWithBehaviorTrace(){}

    //2.知道需要处理的方法之后，进入切面的内容，自定义如何处理

    //@Before 在切入点之前运行
    //@After 在切入点之后运行
    //@Around 前后都运行
    @Around("methodAnnotatedWithBehaviorTrace()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String value = methodSignature.getMethod().getAnnotation(BehaviorTrace.class).value() ;

        long begin= System.currentTimeMillis();
        Object result = joinPoint.proceed();//这个方法执行
        long duration = System.currentTimeMillis() - begin;
        Log.d("AspectLog","value = "+value+"duration = "+duration);
        return result;
    }


}
