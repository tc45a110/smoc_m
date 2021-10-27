package com.smoc.cloud.bloomfilter;

import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class BloomFilterRedis {

    @Autowired
    private JedisPool jedisPool;

    public Client getClient() {

        Client client = new Client(jedisPool);
        return client;
    }

    public Client getClient(JedisPool pool) {

        Client client = new Client(pool);

        return client;
    }

}
