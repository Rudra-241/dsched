package io.github.rudra241.dsserver.mappers;

import io.github.rudra241.dsserver.domain.Schedule;
import io.github.rudra241.ds.proto.common.ScheduleSpec;

public class SchedulerMapper {

    public static Schedule mapToPOJO(ScheduleSpec spec) {
        if (spec.hasRunAtEpochMs()) {
            return Schedule.runAtEpochMs(spec.getRunAtEpochMs());
        }

        if (spec.hasFixedDelayMs()) {
            return Schedule.fixedDelayMs(spec.getFixedDelayMs());
        }

        throw new IllegalArgumentException("Invalid ScheduleSpec");
    }
}
