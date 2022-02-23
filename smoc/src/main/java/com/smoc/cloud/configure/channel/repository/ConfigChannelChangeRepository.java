package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.configure.channel.entity.ConfigChannelChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigChannelChangeRepository extends JpaRepository<ConfigChannelChange, String> {
}