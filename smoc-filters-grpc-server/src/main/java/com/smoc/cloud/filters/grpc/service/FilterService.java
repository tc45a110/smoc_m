package com.smoc.cloud.filters.grpc.service;

import com.smoc.cloud.filters.grpc.filters.filter.FullFilterService;
import com.smoc.cloud.filters.grpc.lib.filter.FilterRequest;
import com.smoc.cloud.filters.grpc.lib.filter.FilterResponse;
import com.smoc.cloud.filters.grpc.lib.filter.FilterServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 过滤服务
 */
@GrpcService
public class FilterService extends FilterServiceGrpc.FilterServiceImplBase {

    @Autowired
    private FullFilterService fullFilterService;

    @Override
    public void filter(FilterRequest request, StreamObserver<FilterResponse> responseObserver) {
        Map<String, String> result = fullFilterService.filter(request);
        final FilterResponse.Builder responseBuild = FilterResponse.newBuilder().setCode(result.get("code")).setMessage(result.get("message"));
        responseObserver.onNext(responseBuild.build());
        responseObserver.onCompleted();
    }
}
