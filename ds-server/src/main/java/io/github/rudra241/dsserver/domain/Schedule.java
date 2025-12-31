package io.github.rudra241.dsserver.domain;

import java.util.Objects;

public final class Schedule {

    public enum Type {
        RUN_AT_EPOCH_MS,
        FIXED_DELAY_MS
    }

    private final Type type;
    private final long value;

    private Schedule(Type type, long value) {
        this.type = Objects.requireNonNull(type);
        this.value = value;
    }

    public static Schedule runAtEpochMs(long epochMs) {
        return new Schedule(Type.RUN_AT_EPOCH_MS, epochMs);
    }

    public static Schedule fixedDelayMs(long delayMs) {
        return new Schedule(Type.FIXED_DELAY_MS, delayMs);
    }

    public Type getType() {
        return type;
    }

    public long getValue() {
        return value;
    }

    public long runAtEpochMs() {
        if (type != Type.RUN_AT_EPOCH_MS) {
            throw new IllegalStateException("Not RUN_AT_EPOCH_MS");
        }
        return value;
    }

    public long fixedDelayMs() {
        if (type != Type.FIXED_DELAY_MS) {
            throw new IllegalStateException("Not FIXED_DELAY_MS");
        }
        return value;
    }
}
