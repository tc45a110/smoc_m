package com.smoc.cloud.filters.grpc.service;

import com.smoc.cloud.filters.grpc.lib.filter.FilterRequest;
import com.smoc.cloud.filters.grpc.lib.filter.FilterResponse;
import com.smoc.cloud.filters.grpc.lib.filter.FilterServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FiltersService {

    @GrpcClient("smoc-filters-grpc-server")
    private FilterServiceGrpc.FilterServiceBlockingStub filterServiceBlockingStub;

    public String filter(){
       try {
           long start = System.currentTimeMillis();
           FilterResponse response = filterServiceBlockingStub.filter(FilterRequest.newBuilder().setAccount("WHZ119").setPhone("18510816778").setCarrier("CMCC").setChannelId("").setMessage("【招商银行】尊敬的客户，为更好保障持卡人用卡权益，并逐步关闭部分账单日，建议您可拨打热线400-820-5555申请更改至新账单日。退订回#C白皮猪").setTemplateId("").setProvinceCode("").build());
           long end = System.currentTimeMillis();
           log.info("code:{},message:{}",response.getCode(),response.getMessage());
           return response.getCode();
       }catch (final StatusRuntimeException e){
          System.out.println(e.getStatus().getCode());
          return e.getStatus().getCode()+"";
       }
    }
}
