package com.smoc.cloud.http.api.common;


import com.google.gson.Gson;
import com.smoc.cloud.http.entity.PushMoParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;


@Slf4j
@RestController
@RequestMapping("mo")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PushMoController {

    /**
     * http://localhost:18091/http-server/mo/pushMo
     * @param params
     * @return
     */
    @RequestMapping(value = "/pushMo", method = RequestMethod.POST)
    public String pushReport(@RequestBody PushMoParams params) {

        log.info("[推送过来的上行短信]：{}", new Gson().toJson(params));
        return "0000";
    }
}
