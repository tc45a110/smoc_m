package com.smoc.cloud.configure.filters;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmocFiltersService {

    @Autowired
    private SmocFiltersFeignClient smocFiltersFeignClient;

    public ResponseData filter(RequestFullParams requestFullParams) {

        try {
            ResponseData responseData = smocFiltersFeignClient.filter(requestFullParams);
            log.info("[filterResponse]:{}",new Gson().toJson(responseData));
            return responseData;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());

        }

    }
}
