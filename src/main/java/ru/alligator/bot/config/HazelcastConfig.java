package ru.alligator.bot.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    HazelcastInstance hazelcastClient(@Value("${hazelcast.cluster_name}") String clusterName,
                                      @Value("${hazelcast.endpoint}") String endpoint) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(endpoint);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
