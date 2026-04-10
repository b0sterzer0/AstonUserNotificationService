package com.aston.notification_service.listeners;

import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.dto.UserEventType;
import com.aston.notification_service.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
        "spring.config.import=",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "kafka.topics.user-events=test.user.events",
        "spring.kafka.consumer.group-id=test-group"
})
class UserEventListenerTest {
    private static final String EMAIL = "test@mail.com";
    private static final String USER_EVENT_TOPIC = "test.user.events";

    @Container
    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.producer.key-serializer",
                () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer",
                () -> "org.springframework.kafka.support.serializer.JsonSerializer");
        registry.add("spring.kafka.consumer.key-deserializer",
                () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer",
                () -> "org.springframework.kafka.support.serializer.JsonDeserializer");
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
        registry.add("spring.kafka.consumer.properties.spring.json.value.default.type",
                () -> "com.aston.notification_service.dto.UserEventDto");
        registry.add("spring.kafka.consumer.properties.spring.json.type.mapping",
                () -> "com.aston.notification_service.dto.UserEventDto:com.aston.notification_service.dto.UserEventDto");
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private NotificationService notificationService;

    @Test
    void shouldHandleUserCreatedEvent() {
        UserEventDto dto = new UserEventDto(1L, EMAIL, UserEventType.CREATED);
        kafkaTemplate.send(USER_EVENT_TOPIC, dto);
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(notificationService).userCreatedEventEmailNotification(dto)
                );
    }

    @Test
    void shouldHandleUserDeletedEvent() {
        UserEventDto dto = new UserEventDto(2L, EMAIL, UserEventType.DELETED);
        kafkaTemplate.send(USER_EVENT_TOPIC, dto);
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(notificationService).userDeletedEventEmailNotification(dto)
                );
    }
}
