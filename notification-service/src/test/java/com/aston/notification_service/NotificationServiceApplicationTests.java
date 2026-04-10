package com.aston.notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestPropertySource(properties = {
        "spring.config.import=",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "kafka.topics.user-events=test.user.events",
        "spring.kafka.consumer.group-id=test-group"
})
class NotificationServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
