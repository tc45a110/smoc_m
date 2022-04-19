package com.smoc.cloud.http.repository;


import com.smoc.cloud.http.entity.SystemHttpApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemHttpApiRequestRepository  extends JpaRepository<SystemHttpApiRequest, String> {
}
