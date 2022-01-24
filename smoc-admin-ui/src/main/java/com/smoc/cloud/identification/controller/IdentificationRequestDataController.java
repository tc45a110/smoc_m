package com.smoc.cloud.identification.controller;

import com.smoc.cloud.identification.service.IdentificationRequestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 身份认证数据
 */
@Slf4j
@Controller
@RequestMapping("/identification/data")
public class IdentificationRequestDataController {

    @Autowired
    private IdentificationRequestDataService identificationRequestDataService;
}
