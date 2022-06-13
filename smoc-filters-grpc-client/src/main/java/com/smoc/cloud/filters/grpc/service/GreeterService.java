package com.smoc.cloud.filters.grpc.service;

import com.smoc.cloud.filters.grpc.lib.test.HelloReply;
import com.smoc.cloud.filters.grpc.lib.test.HelloRequest;
import com.smoc.cloud.filters.grpc.lib.test.SimpleGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class GreeterService {

    @GrpcClient("smoc-filters-grpc-server")
    private SimpleGrpc.SimpleBlockingStub simpleBlockingStub;

    public String greet(String name) {

        try {
            HelloReply response = simpleBlockingStub.sayHello(HelloRequest.newBuilder().setName(name).build());
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode();
        }
    }

}
