package com.aston.user_service.integration;

import com.aston.user_service.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceControllerIntegrationTest {
    private static final String USER_NAME = "John Doe";
    private static final String EMAIL = "john@example.com";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserAndReturnDto() throws Exception {
        UserDto inputDto = new UserDto(null, USER_NAME, EMAIL, 30, null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(USER_NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList", hasSize(greaterThan(0))));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UserDto inputDto = new UserDto(null, "To Delete", "delete@example.com", 20, null);
        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound());
    }
}
