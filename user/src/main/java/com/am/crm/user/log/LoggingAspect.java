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

    // Apply to all methods in a specific package
    @Around("within(com.am.crm.user.features..*)")
    public Object logPackageMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint);
    }

    private Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] methodArgs = joinPoint.getArgs();

        // Log method entry
        log.info("Entering method: {}.{} with arguments: {}", className, methodName, methodArgs);

        try {
            // Proceed with method execution
            Object result = joinPoint.proceed();

            // Log method exit
            log.info("Exiting method: {}.{} with result: {}", className, methodName, result);

            return result;
        } catch (Throwable throwable) {
            // Log method exit with exception
            log.error("Method {}.{} threw exception: {}", className, methodName, throwable.getMessage());
            throw throwable;  // Rethrow the exception after logging
        }
    }
}
