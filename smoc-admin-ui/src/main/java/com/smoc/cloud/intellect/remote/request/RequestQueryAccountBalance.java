package com.smoc.cloud.intellect.remote.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RequestQueryAccountBalance {

    private List<String> sendAccountList;
}
