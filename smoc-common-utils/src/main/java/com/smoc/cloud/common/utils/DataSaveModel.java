package com.smoc.cloud.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DataSaveModel implements Comparable<DataSaveModel>,Serializable {

    private String dateTime;

    private String project;

    private String key;

    @Override
    public int compareTo(DataSaveModel o) {
        return key.compareTo(o.key);
    }
}
