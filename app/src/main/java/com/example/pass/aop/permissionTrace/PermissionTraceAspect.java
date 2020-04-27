package com.example.pass.aop.permissionTrace;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pass.aop.AOPContextHelper;
import com.example.pass.aop.annotations.BehaviorTrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class PermissionTraceAspect {

    private String[] PERMISSIONS = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Pointcut("execution(@com.example.pass.aop.permissionTrace.PermissionTrace * *(..))")
    public void methodAnnotatedWithPermissionTrace(){}


    //需要确保返回的值是void或者实体类
    @Around("methodAnnotatedWithPermissionTrace()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        //先判断是否有权限
        Activity activity = AOPContextHelper.getInstance().getActivity();
        if (activity!=null){
            int permission = ContextCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
            }else{
                return joinPoint.proceed();
            }
        }
        return null;
    }

}
