//package com.aston.user_service.integration;
//
//import com.aston.user_service.dto.UserDto;
//import com.aston.user_service.exceptions.UserDeletionException;
//import com.aston.user_service.exceptions.UserNotFoundException;
//import com.aston.user_service.models.User;
//import com.aston.user_service.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest
//@Testcontainers
//@Transactional
//public class UserServiceControllerIntegrationTest {
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("Полный цикл: создание, получение, обновление и удаление пользователя")
//    void userFullCycleTest() throws Exception {
//        // 1. Create
//        UserDto createDto = new UserDto(null, "Ivan", "ivan@mail.ru", 25, null);
//
//        String createResponse = mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Ivan"))
//                .andReturn().getResponse().getContentAsString();
//
//        UserDto createdUser = objectMapper.readValue(createResponse, UserDto.class);
//        Long id = createdUser.id();
//
//        // 2. Get by ID
//        mockMvc.perform(get("/users/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("ivan@mail.ru"));
//
//        // 3. Update
//        UserDto updateDto = new UserDto(null, "Ivan Updated", null, 30, null);
//        mockMvc.perform(put("/users/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Ivan Updated"))
//                .andExpect(jsonPath("$.age").value(30));
//
//        // 4. Get All
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$[0].name").value("Ivan Updated"));
//
//        // 5. Delete
//        mockMvc.perform(delete("/users/{id}", id))
//                .andExpect(status().isOk());
//
//        // 6. Verify Deleted (Expecting 404 from your UserNotFoundException)
//        mockMvc.perform(get("/users/{id}", id))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("Валидация: ошибка при создании пользователя с некорректными данными")
//    void testCreateUserValidationFailed() throws Exception {
//        // Допустим, UserDto требует валидный email или непустое имя
//        UserDto invalidDto = new UserDto(null, "", "not-an-email", -5, null);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest());
//    }
//}
