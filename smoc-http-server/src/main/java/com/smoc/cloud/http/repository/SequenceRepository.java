package com.smoc.cloud.http.repository;


import com.smoc.cloud.http.entity.SmocSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SequenceRepository extends CrudRepository<SmocSequence, String>, JpaRepository<SmocSequence, String> {


    /**
     * 获取索引值
     * @param seqName
     * @return
     */
    Integer findSequence(String seqName);
}
