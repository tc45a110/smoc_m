package com.smoc.cloud.filters.grpc.lib.filter;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.35.0)",
    comments = "Source: com.smoc.cloud.filters.grpc/filters.proto")
public final class FilterServiceGrpc {

  private FilterServiceGrpc() {}

  public static final String SERVICE_NAME = "FilterService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.smoc.cloud.filters.grpc.lib.filter.FilterRequest,
      com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> getFilterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Filter",
      requestType = com.smoc.cloud.filters.grpc.lib.filter.FilterRequest.class,
      responseType = com.smoc.cloud.filters.grpc.lib.filter.FilterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smoc.cloud.filters.grpc.lib.filter.FilterRequest,
      com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> getFilterMethod() {
    io.grpc.MethodDescriptor<com.smoc.cloud.filters.grpc.lib.filter.FilterRequest, com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> getFilterMethod;
    if ((getFilterMethod = FilterServiceGrpc.getFilterMethod) == null) {
      synchronized (FilterServiceGrpc.class) {
        if ((getFilterMethod = FilterServiceGrpc.getFilterMethod) == null) {
          FilterServiceGrpc.getFilterMethod = getFilterMethod =
              io.grpc.MethodDescriptor.<com.smoc.cloud.filters.grpc.lib.filter.FilterRequest, com.smoc.cloud.filters.grpc.lib.filter.FilterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Filter"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smoc.cloud.filters.grpc.lib.filter.FilterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smoc.cloud.filters.grpc.lib.filter.FilterResponse.getDefaultInstance()))
              .setSchemaDescriptor(new FilterServiceMethodDescriptorSupplier("Filter"))
              .build();
        }
      }
    }
    return getFilterMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FilterServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilterServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilterServiceStub>() {
        @java.lang.Override
        public FilterServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilterServiceStub(channel, callOptions);
        }
      };
    return FilterServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FilterServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilterServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilterServiceBlockingStub>() {
        @java.lang.Override
        public FilterServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilterServiceBlockingStub(channel, callOptions);
        }
      };
    return FilterServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FilterServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FilterServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FilterServiceFutureStub>() {
        @java.lang.Override
        public FilterServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FilterServiceFutureStub(channel, callOptions);
        }
      };
    return FilterServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class FilterServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void filter(com.smoc.cloud.filters.grpc.lib.filter.FilterRequest request,
        io.grpc.stub.StreamObserver<com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFilterMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getFilterMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.smoc.cloud.filters.grpc.lib.filter.FilterRequest,
                com.smoc.cloud.filters.grpc.lib.filter.FilterResponse>(
                  this, METHODID_FILTER)))
          .build();
    }
  }

  /**
   */
  public static final class FilterServiceStub extends io.grpc.stub.AbstractAsyncStub<FilterServiceStub> {
    private FilterServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilterServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilterServiceStub(channel, callOptions);
    }

    /**
     */
    public void filter(com.smoc.cloud.filters.grpc.lib.filter.FilterRequest request,
        io.grpc.stub.StreamObserver<com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFilterMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FilterServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<FilterServiceBlockingStub> {
    private FilterServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilterServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilterServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.smoc.cloud.filters.grpc.lib.filter.FilterResponse filter(com.smoc.cloud.filters.grpc.lib.filter.FilterRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFilterMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FilterServiceFutureStub extends io.grpc.stub.AbstractFutureStub<FilterServiceFutureStub> {
    private FilterServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FilterServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FilterServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smoc.cloud.filters.grpc.lib.filter.FilterResponse> filter(
        com.smoc.cloud.filters.grpc.lib.filter.FilterRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFilterMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FILTER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FilterServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FilterServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FILTER:
          serviceImpl.filter((com.smoc.cloud.filters.grpc.lib.filter.FilterRequest) request,
              (io.grpc.stub.StreamObserver<com.smoc.cloud.filters.grpc.lib.filter.FilterResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class FilterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FilterServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smoc.cloud.filters.grpc.lib.filter.FullFilterProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FilterService");
    }
  }

  private static final class FilterServiceFileDescriptorSupplier
      extends FilterServiceBaseDescriptorSupplier {
    FilterServiceFileDescriptorSupplier() {}
  }

  private static final class FilterServiceMethodDescriptorSupplier
      extends FilterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FilterServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FilterServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FilterServiceFileDescriptorSupplier())
              .addMethod(getFilterMethod())
              .build();
        }
      }
    }
    return result;
  }
}
