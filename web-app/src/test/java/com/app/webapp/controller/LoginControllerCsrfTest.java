package com.app.webapp.controller;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.CustomUserDetailsService;
import com.app.webapp.security.SecurityConfig;
import com.app.webapp.security.UserInfoSession;

@WebMvcTest(
  controllers = LoginController.class,
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
class LoginControllerCsrfTest {

    @Autowired private MockMvc mvc;

    @MockBean private UserClient userClient;
    @MockBean private CustomUserDetailsService userDetailsService;
    @MockBean private UserInfoSession userInfoSession;

    @Test
    void post_login_without_csrf_forbidden() throws Exception {
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john@example.com")
                        .param("password", "x"))
                .andExpect(status().isForbidden());
    }

    @Test
    void post_login_with_csrf_success_redirect() throws Exception {
        UserDto user = new UserDto();
        user.setId("U1"); user.setEmail("john@example.com"); user.setUsername("john");
        when(userClient.getUserByEmail("john@example.com")).thenReturn(ApiResponse.success(user));
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        UserDetails details = User.withUsername("john@example.com").password(enc.encode("x")).roles("USER").build();
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(details);

        mvc.perform(post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "john@example.com")
                        .param("password", "x"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser
    void get_login_when_authenticated_redirects_dashboard() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }
}


