package ru.polyan.onlinecart.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticAspectService {

    private Map<String, Long> durationStat;

    @PostConstruct
    private void prepare(){
        durationStat = new HashMap<>();
    }

    @Around("execution(public * ru.polyan.onlinecart.services2.*.*(..))")
    public Object timeTracker(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object out = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        long duration = end - begin;
        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        Long dr = durationStat.get(className);
        if(dr!=null){
            durationStat.put(className, duration+dr);
        }else {
            durationStat.put(className, duration);
        }
        return out;
    }

    public Map<String, Long> getStatistic(){
        return Collections.unmodifiableMap(durationStat);
    }

}
