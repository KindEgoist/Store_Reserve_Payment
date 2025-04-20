package ru.gb.store.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(ru.gb.store.aspect.TrackAction)")
    public Object logUserAction(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TrackAction annotation = signature.getMethod().getAnnotation(TrackAction.class);

        String actionName = annotation.value().isEmpty()
                ? signature.getName()
                : annotation.value();

        log.info("Действие: - Метод: " + actionName + ", Аргументы: " + Arrays.toString(joinPoint.getArgs()));

        return joinPoint.proceed();
    }

    @Around("@annotation(ru.gb.store.aspect.Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        log.info("Время: Метод: " + joinPoint.getSignature().getName() + " выполнился за " + duration + "мс");

        return result;
    }
}
