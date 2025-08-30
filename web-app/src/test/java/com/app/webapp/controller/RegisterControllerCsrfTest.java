package com.app.webapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.CustomUserDetailsService;
import com.app.webapp.security.SecurityConfig;
import com.app.webapp.security.UserInfoSession;

@WebMvcTest(
  controllers = RegisterController.class,
  excludeAutoConfiguration = {
    org.springframework.cloud.config.client.ConfigClientAutoConfiguration.class,
    org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration.class,
    org.springframework.cloud.openfeign.FeignAutoConfiguration.class
  },
  properties = {
    "spring.cloud.config.enabled=false",
    "spring.cloud.bootstrap.enabled=false",
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false",
    "spring.cloud.openfeign.enabled=false",
    "feign.client.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
  }
)
@Import(SecurityConfig.class)
class RegisterControllerCsrfTest {

    @Autowired private MockMvc mvc;

    @MockBean private UserClient userClient;
    @MockBean private CustomUserDetailsService userDetailsService;
    @MockBean private UserInfoSession userInfoSession;

    @Test
    void get_register_form_ok() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void post_register_without_csrf_forbidden() throws Exception {
        mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john")
                        .param("email", "john@example.com")
                        .param("password", "x"))
                .andExpect(status().isForbidden());
    }

    @Test
    void post_register_with_csrf_success_redirect() throws Exception {
        UserDto in = new UserDto(); in.setEmail("john@example.com"); in.setUsername("john");
        UserDto created = new UserDto(); created.setId("U1"); created.setEmail("john@example.com"); created.setUsername("john");
        when(userClient.createUser(any(UserDto.class))).thenReturn(ApiResponse.success(created));
        UserDetails details = User.withUsername("john@example.com").password("{noop}x").roles("USER").build();
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(details);

        mvc.perform(post("/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john")
                        .param("email", "john@example.com")
                        .param("password", "x"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile-completion/step1"));
    }
}


