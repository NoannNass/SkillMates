package com.app.webapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.client.SessionClient;
import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.PartnershipDTO;
import com.app.webapp.dto.session.CreateSessionRequest;
import com.app.webapp.dto.session.SessionResponse;
import com.app.webapp.dto.session.SessionStatus;
import com.app.webapp.dto.session.SessionType;
import com.app.webapp.security.CustomUserDetailsService;
import com.app.webapp.security.SecurityConfig;
import com.app.webapp.security.UserInfoSession;

@WebMvcTest(
  controllers = SessionWebController.class,
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
class SessionWebControllerCsrfTest {

    @Autowired
    private MockMvc mvc;

    @MockBean private SessionClient sessionClient;
    @MockBean private UserInfoSession userInfoSession;
    @MockBean private PartnershipClient partnershipClient;
    @MockBean private UserClient userClient;
    @MockBean private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void get_list_redirects_to_login_when_no_user() throws Exception {
        when(userInfoSession.getUserId()).thenReturn(null);
        mvc.perform(get("/sessions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser
    void get_list_ok_with_user_and_filters() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("U1");
        SessionResponse s = new SessionResponse();
        s.setId(1L);
        s.setOrganizerId("U1");
        s.setPartnerId("U2");
        when(sessionClient.list(any(), any(SessionStatus.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(s));

        mvc.perform(get("/sessions").param("status", SessionStatus.PLANNED.name()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sessions"));
    }

    @Test
    @WithMockUser
    void get_new_redirects_to_login_when_no_user() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("");
        mvc.perform(get("/sessions/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser
    void post_create_without_csrf_forbidden() throws Exception {
        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void post_create_with_csrf_success_redirect() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("U1");
        PartnershipDTO p = new PartnershipDTO();
        p.setId(99L);
        p.setRequesterId("U1"); p.setRequestedId("U2");
        when(partnershipClient.getActivePartnerships("U1"))
                .thenReturn(ApiResponse.success(List.of(p)));

        SessionResponse created = new SessionResponse();
        created.setId(123L);
        when(sessionClient.create(any(CreateSessionRequest.class))).thenReturn(created);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("partnerId", "U2")
                        .param("type", SessionType.VIRTUAL.name())
                        .param("locationOrLink", "https://meet")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions"));
    }

    @Test
    @WithMockUser
    void post_cancel_with_csrf_redirects_and_calls_client() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("U1");
        mvc.perform(post("/sessions/5/cancel").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/5"));
        verify(sessionClient).cancel(5L, "U1");
    }

    @Test
    @WithMockUser
    void post_complete_with_csrf_redirects_and_calls_client() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("U1");
        mvc.perform(post("/sessions/7/complete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/7"));
        verify(sessionClient).complete(7L, "U1");
    }

    @Test
    @WithMockUser
    void post_cancel_without_csrf_forbidden() throws Exception {
        mvc.perform(post("/sessions/5/cancel"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void post_complete_without_csrf_forbidden() throws Exception {
        mvc.perform(post("/sessions/7/complete"))
                .andExpect(status().isForbidden());
    }
}


