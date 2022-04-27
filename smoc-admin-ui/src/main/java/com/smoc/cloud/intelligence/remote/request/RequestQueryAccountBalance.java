package com.smoc.cloud.intelligence.remote.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RequestQueryAccountBalance {

    private List<String> sendAccountList;
}
