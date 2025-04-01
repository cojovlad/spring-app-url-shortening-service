package com.example.spring_app_url_shortening_service.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service responsible for rate limiting various endpoints based on IP address and endpoint.
 * It provides different rate limits for login attempts, registration requests, and general API usage.
 */
@Service
public class RateLimitingService {

    private final ConcurrentMap<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Rate limit configuration for login attempts.
     * Allows 5 login attempts per 10 minutes.
     */
    private final Bandwidth loginLimit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(10)));

    /**
     * Rate limit configuration for registration attempts.
     * Allows 3 registration attempts per hour.
     */
    private final Bandwidth registrationLimit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));

    /**
     * Rate limit configuration for general API requests.
     * Allows 100 requests per minute.
     */
    private final Bandwidth apiLimit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));

    /**
     * Resolves a rate limit bucket for a given IP address and endpoint.
     * It creates a new bucket if one does not exist in the cache, applying the appropriate rate limit based on the endpoint.
     *
     * @param ip The IP address of the client making the request.
     * @param endpoint The endpoint being accessed.
     * @return A Bucket object representing the rate limit for the specified IP and endpoint.
     */
    public Bucket resolveBucket(String ip, String endpoint) {
        String key = ip + ":" + endpoint;

        return cache.computeIfAbsent(key, k -> {
            Bandwidth limit;
            if (endpoint.startsWith("/api/v1/auth/login")) {
                limit = loginLimit;
            } else if (endpoint.startsWith("/api/v1/auth/register")) {
                limit = registrationLimit;
            } else {
                limit = apiLimit;
            }
            return Bucket.builder().addLimit(limit).build();
        });
    }
}
