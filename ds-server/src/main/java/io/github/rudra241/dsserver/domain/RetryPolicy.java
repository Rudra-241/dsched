package io.github.rudra241.dsserver.domain;

public record RetryPolicy (int maxAttempts, long backOffMs) {}
