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
    private static final String STATUS = "success";
    private static final String EMAIL = "welcome@test.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessWhenUserCreated() throws Exception {
        UserEventDto dto = new UserEventDto(1L, EMAIL);

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
        UserEventDto dto = new UserEventDto(2L, EMAIL);

        mockMvc.perform(post("/notification/email/user/deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(STATUS)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.message", is("Здравствуйте! Ваш аккаунт был удален!")));
    }
}
