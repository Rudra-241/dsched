package io.github.rudra241.dsserver.mappers;

import io.github.rudra241.dsserver.domain.RetryPolicy;

public final class RetryPolicyMapper {

    private RetryPolicyMapper() {}

    public static RetryPolicy mapToPOJO(
            io.github.rudra241.ds.proto.common.RetryPolicy spec
    ) {
        return new RetryPolicy(
                spec.getMaxAttempts(),
                spec.getBackoffMs()
        );
    }
}
