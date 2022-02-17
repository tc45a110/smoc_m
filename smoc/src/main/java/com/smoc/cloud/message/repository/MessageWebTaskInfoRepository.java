package com.smoc.cloud.message.repository;

import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageWebTaskInfoRepository extends JpaRepository<MessageWebTaskInfo, String> {
}