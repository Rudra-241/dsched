package io.github.rudra241.dsserver.domain;

import io.github.rudra241.dsserver.helpers.UuidBytesHelper;

import java.time.Instant;
import java.util.Optional;

public class Job {
    private final byte[] jobId;
    private final byte[] requestId;

    private final Schedule schedule;
    private final String jobName;
    private final RetryPolicy retryPolicy;


    private final byte[] payload;
    private JobState state;
    private FailureInfo failureInfo;

    private final Instant createdAt;
    private Instant updatedAt;

    public String getJobName() {
        return jobName;
    }

    public byte[] getJobId() {
        return jobId;
    }

    public byte[] getRequestId() {
        return requestId;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public JobState getState() {
        return state;
    }

    public Optional<FailureInfo> getFailureInfo() {
        return Optional.ofNullable(failureInfo);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public byte[] getPayload() {
        return payload;
    }

    public Job(byte[] jobId, byte[] requestId, Schedule schedule, String jobName, RetryPolicy retryPolicy, byte[] payload, Instant createdAt) {
        this.schedule = schedule;
        if(jobId == null) throw new IllegalArgumentException("jobId cannot be null");
        if(requestId == null) throw new IllegalArgumentException("requestId cannot be null");
        if(jobName == null) throw new IllegalArgumentException("jobName cannot be null");
        if(retryPolicy.maxAttempts() <= 0) throw new IllegalArgumentException("retryPolicy.maxAttempts() must be > 0");
        if(retryPolicy.backOffMs() <= 0) throw new IllegalArgumentException("retryPolicy.backOffMs() must be > 0");


        this.jobId = jobId;
        this.requestId = requestId;
        this.jobName = jobName;
        this.retryPolicy = retryPolicy;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public static Job createNew(byte[] requestId, Schedule schedule, String jobName, RetryPolicy retryPolicy, byte[] payload) {
        return new Job(UuidBytesHelper.getRandomUuidBytes(),
                requestId,
                schedule,
                jobName,
                retryPolicy,
                payload,
                Instant.now()
        );
    }

    public void markRunning() {
        ensureState(JobState.PENDING);
        transitionTo(JobState.RUNNING);
    }

    public void markSucceeded() {
        ensureState(JobState.RUNNING);
        transitionTo(JobState.SUCCEEDED);
    }

    public void markFailed(FailureInfo failureInfo) {
        ensureState(JobState.RUNNING);
        this.failureInfo = failureInfo;
        transitionTo(JobState.FAILED);
    }

    public void cancel() {
        if (state == JobState.SUCCEEDED || state == JobState.FAILED) {
            return; // idempotent
        }
        transitionTo(JobState.CANCELED);
    }

    // State transition helpers
    private void transitionTo(JobState newState) {
        this.state = newState;
        this.updatedAt = Instant.now();
    }

    private void ensureState(JobState expected) {
        if (this.state != expected) {
            throw new IllegalStateException(
                    "invalid state transition from " + state
            );
        }
    }

    public Schedule getSchedule() {
        return schedule;
    }
}

