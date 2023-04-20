package com.annafabrichnaya.cloudstorage.controllersTest;

import com.annafabrichnaya.cloudstorage.controllers.AuthController;
import com.annafabrichnaya.cloudstorage.dto.request.SignupRequest;
import com.annafabrichnaya.cloudstorage.entities.Role;
import com.annafabrichnaya.cloudstorage.entities.User;
import com.annafabrichnaya.cloudstorage.security.UserDetailsService;
import com.annafabrichnaya.cloudstorage.security.jwt.JwtAuthenticationEntryPoint;
import com.annafabrichnaya.cloudstorage.security.jwt.JwtTokenFilter;
import com.annafabrichnaya.cloudstorage.security.jwt.JwtUtils;
import com.annafabrichnaya.cloudstorage.service.impl.UserServiceImpl;
import com.annafabrichnaya.cloudstorage.util.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {
    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @MockBean
    private UserServiceImpl userService;

    @MockBean(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserMapper userMapper;


    @DisplayName("Test signup endpoint returns HTTP.Status 200")
    @Test
    public void saveUser() throws Exception {
        User user = new User("name", "1234", Role.ROLE_USER);
        user.setId(1L);
        SignupRequest signupRequest = new SignupRequest("name", "1234");
        when(userMapper.convertToSignupUser(signupRequest)).thenReturn(user);
        doNothing().when(userService).registration(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("Test login endpoint returns HTTP.Status 200")
    void login() throws Exception {
        when(jwtUtils.generateToken("Shuha")).thenReturn("Bearer yourjwt");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"Shuha\", \"password\": \"9876\"}"))

                .andDo(print()).andExpect(status().isOk());

    }


    @Test
    @WithMockUser
    @DisplayName(" Test logout endpoint returns successful redirection to auth_login")
    void logout() throws Exception {

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));

    }


}
