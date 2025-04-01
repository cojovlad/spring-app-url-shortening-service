package com.example.spring_app_url_shortening_service.config;

import com.example.spring_app_url_shortening_service.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter intercepts HTTP requests and applies rate limiting based on the IP address and endpoint.
 * It ensures that clients do not exceed the allowed number of requests within a given time frame.
 * If a client exceeds the limit, it responds with a 403 Forbidden status and an error message.
 */
@Component
public class RateLimitingFilterConfig extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;

    /**
     * Constructor that injects the RateLimitingService.
     *
     * @param rateLimitingServiceInjection The rate limiting service used to apply rate limiting logic.
     */
    @Autowired
    public RateLimitingFilterConfig(RateLimitingService rateLimitingServiceInjection) {
        this.rateLimitingService = rateLimitingServiceInjection;
    }

    /**
     * Filters the incoming HTTP request and applies rate limiting based on the IP address and request path.
     * If the rate limit is exceeded, the request is blocked with a 403 Forbidden response.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response that will be sent back to the client.
     * @param filterChain The filter chain to continue the request processing if rate limiting is not exceeded.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an error occurs while processing the request or response.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        String requestPath = request.getRequestURI();
        Bucket bucket = rateLimitingService.resolveBucket(clientIp, requestPath);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Too many requests. Try again later.");
        }
    }
}
