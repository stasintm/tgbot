package ru.alligator.bot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class BotApplicationTests {

    private static GenericContainer<?> hazelcastContainer = new GenericContainer<>("hazelcast/hazelcast:5.1.3")
        .withExposedPorts(5701);

    static {
        hazelcastContainer.start();
        System.setProperty("hazelcast.endpoint", hazelcastContainer.getHost()
            + ":" + hazelcastContainer.getMappedPort(5701).toString());
    }


	@Test
	void contextLoads() {
	}

}
