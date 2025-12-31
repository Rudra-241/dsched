package io.github.rudra241.dsserver.api;

import io.github.rudra241.ds.proto.client.*;
import io.github.rudra241.dsserver.domain.Job;
import io.github.rudra241.dsserver.helpers.UuidBytesHelper;
import io.github.rudra241.dsserver.mappers.RetryPolicyMapper;
import io.github.rudra241.dsserver.mappers.SchedulerMapper;
import io.github.rudra241.dsserver.repositories.JobRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Optional;
import java.util.UUID;

public class ClientGrpcService extends ClientServiceGrpc.ClientServiceImplBase {
    JobRepository jobRepository;

    public ClientGrpcService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void submitJob(SubmitJobRequest request, StreamObserver<SubmitJobResponse> responseObserver) {
        try {
            UUID reqId = UuidBytesHelper.toUuid(request.getRequestId());
            if (!request.hasSchedule()) {
                throw Status.INVALID_ARGUMENT
                        .withDescription("schedule is required")
                        .asRuntimeException();
            }

            Optional<Job> existing = jobRepository.findByJobId(reqId);
            if (existing.isPresent()) {
                SubmitJobResponse res = SubmitJobResponse.newBuilder().setJobId(UuidBytesHelper.toByteString(existing.get().getJobId())).build();
                responseObserver.onNext(res);
                responseObserver.onCompleted(); // returns
            }
            Job job = Job.createNew(UuidBytesHelper.toBytes(request.getRequestId()),
                    SchedulerMapper.mapToPOJO(request.getSchedule()),
                    request.getJobName(),
                    RetryPolicyMapper.mapToPOJO(request.getRetryPolicy()),
                    UuidBytesHelper.toBytes(request.getPayload()));
            jobRepository.save(job);
            responseObserver.onNext(SubmitJobResponse.newBuilder().setJobId(UuidBytesHelper.toByteString(job.getJobId())).build());
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("internal error").withCause(e).asRuntimeException());

        }
    }

    @Override
    public void getJobStatus(GetJobStatusRequest request, StreamObserver<GetJobStatusResponse> responseObserver) {
        try {
            UUID jobId = UuidBytesHelper.toUuid(request.getJobId());
            Optional<Job> job = jobRepository.findByJobId(jobId);
            if (job.isPresent()) {

            }
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void cancelJob(CancelJobRequest request, StreamObserver<CancelJobResponse> responseObserver) {
        super.cancelJob(request, responseObserver);
    }
}
