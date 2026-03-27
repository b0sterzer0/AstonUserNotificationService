package com.aston.user_service.aspects;

import com.aston.user_service.dto.UserDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(com.aston.user_service.services.UserService)")
    public Object logUserService(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.debug("Logging UserService:");
        LOGGER.debug("Called method: " + joinPoint.getSignature().getName());
        LOGGER.debug("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        if (result != null) {
            LOGGER.debug("Method proceeded result:");
            if (result instanceof UserDto) {
                UserDto user = (UserDto) result;
                LOGGER.debug("UserDto with id = " + user.id());
            }
            else if (result instanceof List<?> list) {
                LOGGER.debug("List result: " + Arrays.toString(list.toArray()));
            }
        }
        else LOGGER.debug("Method not proceeded result");
        return result;
    }
}
