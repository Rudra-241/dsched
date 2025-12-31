package io.github.rudra241.dsserver.repositories;

import io.github.rudra241.dsserver.domain.Job;
import io.github.rudra241.dsserver.domain.JobState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository {
    void save(Job job);

    Optional<Job> findByJobId(UUID jobId);
    Optional<Job> findByRequestId(UUID requestId);
    /**
     * Cancel job if it exists.
     * Idempotent.
     */
    boolean cancelJob(UUID jobId);
    /**
     * Update job state atomically.
     * Used by scheduler only.
     */
    void updateState(UUID jobId, JobState newState);
    /**
     * Update job state + failure info atomically.
     */
    void failJob(UUID jobId, String failureCode, String failureMessage);
}
