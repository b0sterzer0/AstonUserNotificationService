package com.aston.notification_service.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static Logger LOGGER =  LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.aston.notification_service.listeners.UserEventListener.*(..))")
    public void logUserCreatedKafkaMessage(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        LOGGER.debug("Calling method: " + methodName + " with args: " + java.util.Arrays.toString(joinPoint.getArgs()));
    }
}
