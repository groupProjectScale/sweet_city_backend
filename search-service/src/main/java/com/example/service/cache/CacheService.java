package com.example.service.cache;

import com.example.model.Activity;
import com.example.service.cache.annotations.Cached;
import com.example.service.cache.annotations.CachedAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RMapCache;
import org.redisson.api.RMapCacheAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class CacheService {

    private static final int CACHE_EXPIRATION_TIME = 10; // in minutes
    private final RedissonClient redissonClient;

    private final ObjectMapper objectMapper;
    private static final Logger logger = LogManager.getLogger(CacheService.class);

    public CacheService(RedissonClient redissonClient, ObjectMapper objectMapper) {
        this.redissonClient = redissonClient;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.example.service.cache.annotations.Cached)")
    public Object cacheSearchResults(ProceedingJoinPoint joinPoint) throws Throwable {
        Cached annotation =
                ((MethodSignature) joinPoint.getSignature())
                        .getMethod()
                        .getAnnotation(Cached.class);
        String cacheKey = getCacheKey(joinPoint, annotation.key());

        // check if the cache exists
        RMapCache<String, String> cache =
                redissonClient.getMapCache(joinPoint.getSignature().getName());
        // logger.warn("cache name is: " + method.getName());
        String cachedResult = cache.get(cacheKey);
        if (cachedResult != null) {
            logger.info("Retrieving result from cache: " + cacheKey);
            logger.info("the current size of cache is: " + cache.size());
            List<Activity> result = objectMapper.readValue(cachedResult, new TypeReference<>() {});
            return result;
        } else {
            List<Activity> result = (List<Activity>) joinPoint.proceed();

            String jsonResult = objectMapper.writeValueAsString(result);
            cache.put(cacheKey, jsonResult, CACHE_EXPIRATION_TIME, TimeUnit.MINUTES);
            logger.info("Saving cachedKey to cache: " + cacheKey);
            logger.info("Currently saving the cache record: " + jsonResult);
            return result;
        }
    }

    @Around("@annotation(com.example.service.cache.annotations.CachedAsync)")
    public CompletableFuture<List<Activity>> cacheSearchResultsAsync(
            ProceedingJoinPoint joinPoint) {
        CachedAsync annotation =
                ((MethodSignature) joinPoint.getSignature())
                        .getMethod()
                        .getAnnotation(CachedAsync.class);
        String cacheKey = getCacheKey(joinPoint, annotation.key());

        RMapCacheAsync<String, String> cache =
                redissonClient.getMapCache(joinPoint.getSignature().getName());

        CompletableFuture<String> cachedResultFuture =
                cache.getAsync(cacheKey).toCompletableFuture();

        return cachedResultFuture.thenCompose(
                cachedResult -> {
                    if (cachedResult != null) {
                        logger.info("Retrieving result from cacheAsync: " + cacheKey);
                        cache.sizeAsync()
                                .thenAccept(
                                        size ->
                                                logger.info(
                                                        "the current size of cacheAsync is: "
                                                                + size));
                        List<Activity> result;
                        try {
                            result = objectMapper.readValue(cachedResult, new TypeReference<>() {});
                            return CompletableFuture.completedFuture(result);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            CompletableFuture<List<Activity>> result =
                                    (CompletableFuture<List<Activity>>) joinPoint.proceed();
                            CompletableFuture<String> jsonResult =
                                    result.thenApply(
                                            activityList -> {
                                                try {
                                                    return objectMapper.writeValueAsString(
                                                            activityList);
                                                } catch (JsonProcessingException e) {
                                                    return null;
                                                }
                                            });
                            cache.putAsync(
                                    cacheKey,
                                    jsonResult.get(),
                                    CACHE_EXPIRATION_TIME,
                                    TimeUnit.MINUTES);
                            logger.info("Saving cachedKey to cacheAsync: " + cacheKey);
                            logger.info(
                                    "Currently saving the cacheAsync record: " + jsonResult.get());
                            return result;
                        } catch (Throwable e) {
                            logger.error(e.getMessage());
                            throw new CompletionException(e);
                        }
                    }
                });
    }

    private String getCacheKey(ProceedingJoinPoint joinPoint, String annotationKey) {
        // Get the method signature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // Get the method args
        List<Object> argList = Arrays.asList(joinPoint.getArgs());
        String cacheKey = annotationKey;
        if (cacheKey.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String argString = mapper.writeValueAsString(argList);
                cacheKey = method.getName() + ":" + argString;
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        }
        return cacheKey;
    }
}
