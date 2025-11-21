package com.eprogs.raiseme.service.authServices;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, key -> {
            Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(5)));
            return Bucket.builder().addLimit(limit).build();
        });
    }
}
