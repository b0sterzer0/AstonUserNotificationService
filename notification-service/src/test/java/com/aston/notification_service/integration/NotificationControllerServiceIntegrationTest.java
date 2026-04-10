package com.aston.notification_service.integration;

import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.dto.UserEventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(properties = {
        "spring.config.import=",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
        "kafka.topics.user-events=test.user.events",
        "spring.kafka.consumer.group-id=test-group",
        "spring.kafka.consumer.properties.spring.json.trusted.packages=*",
        "spring.kafka.consumer.properties.spring.json.value.default.type=com.aston.notification_service.dto.UserEventDto"
})
public class NotificationControllerServiceIntegrationTest {

    private static final String STATUS = "success";
    private static final String EMAIL = "welcome@test.com";

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessWhenUserCreated() throws Exception {
        UserEventDto dto = new UserEventDto(1L, EMAIL, UserEventType.CREATED);

        mockMvc.perform(post("/notification/email/user/created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnSuccessWhenUserDeleted() throws Exception {
        UserEventDto dto = new UserEventDto(2L, EMAIL, UserEventType.DELETED);

        mockMvc.perform(post("/notification/email/user/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.message", is("Здравствуйте! Ваш аккаунт был удален!")));
    }
}
