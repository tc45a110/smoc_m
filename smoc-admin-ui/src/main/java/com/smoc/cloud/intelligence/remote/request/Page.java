package com.smoc.cloud.intelligence.remote.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Page {

    private List<Content> contents;
}
