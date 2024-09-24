package com.am.crm.user.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("within(com.am.crm.user.features..*)")
    public Object logPackageMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint);
    }

    private Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Entering method: {}.{} with arguments: {}", className, methodName, methodArgs);
        try {
            Object result = joinPoint.proceed();
            log.info("Exiting method: {}.{} with result: {}", className, methodName, result);
            return result;
        } catch (Throwable throwable) {
            log.error("Method {}.{} threw exception: {}", className, methodName, throwable.getMessage());
            throw throwable;
        }
    }
}
