package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServiceLogging {

    @Pointcut("execution(* com.cydeo.service.*.*(..))")
    public void service(){}

    @Before("service()")
    public void beforeController_Advise(JoinPoint joinPoint) {
        log.info("Before -> User: {}, Method: {}",
                GetUsername.getUsername(),
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterReturning(pointcut = "service()", returning = "result")
    public void afterAnyProjectAndTaskControllerAdvise(JoinPoint joinPoint, Object result) {
        log.info("After Returning -> User: {}, Method: {}, Results: {}",
                GetUsername.getUsername(),
                joinPoint.getSignature().toShortString(),
                result.toString()
        );
    }

    @AfterThrowing(pointcut = "service()", throwing = "exception")
    public void afterAnyProjectAndTaskControllerAdvise(JoinPoint joinPoint, Exception exception) {
        log.info("After Returning -> User: {}, Method: {}, Results: {}",
                GetUsername.getUsername(),
                joinPoint.getSignature().toShortString(),
                exception.toString()
        );
    }

}
