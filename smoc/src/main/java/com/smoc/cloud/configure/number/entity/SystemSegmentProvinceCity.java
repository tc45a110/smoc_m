package com.smoc.cloud.configure.number.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "system_segment_province_city")
public class SystemSegmentProvinceCity {
    private String id;
    private String segment;
    private String provinceCode;
    private String provinceName;
    private String cityName;
    private String isSync;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SEGMENT")
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Basic
    @Column(name = "PROVINCE_CODE")
    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Basic
    @Column(name = "PROVINCE_NAME")
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Basic
    @Column(name = "CITY_NAME")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "IS_SYNC")
    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemSegmentProvinceCity that = (SystemSegmentProvinceCity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(segment, that.segment) &&
                Objects.equals(provinceCode, that.provinceCode) &&
                Objects.equals(provinceName, that.provinceName) &&
                Objects.equals(cityName, that.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, segment, provinceCode, provinceName, cityName);
    }
}
