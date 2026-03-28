package com.aston.notification_service.integration;

import com.aston.notification_service.dto.UserEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessWhenUserCreated() throws Exception {
        UserEventDto dto = new UserEventDto(1L, "welcome@test.com");

        mockMvc.perform(post("/notification/email/user/created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.email", is("welcome@test.com")))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnSuccessWhenUserDeleted() throws Exception {
        UserEventDto dto = new UserEventDto(2L, "goodbye@test.com");

        mockMvc.perform(post("/notification/email/user/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.email", is("goodbye@test.com")))
                .andExpect(jsonPath("$.message", is("Здравствуйте! Ваш аккаунт был удален!")));
    }
}
