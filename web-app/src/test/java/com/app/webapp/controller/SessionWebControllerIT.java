package com.app.webapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.app.webapp.security.UserInfoSession;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.openfeign.enabled=false",
        "feign.client.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@AutoConfigureMockMvc
class SessionWebControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean private SessionClient sessionClient;
    @MockBean private UserInfoSession userInfoSession;
    @MockBean private PartnershipClient partnershipClient;
    @MockBean private UserClient userClient;

    @Test
    @WithMockUser
    void list_sessions_view_renders_with_user() throws Exception {
        org.mockito.Mockito.when(userInfoSession.getUserId()).thenReturn("U1");
        SessionResponse s = new SessionResponse();
        s.setId(1L);
        s.setOrganizerId("U1");
        s.setPartnerId("U2");
        org.mockito.Mockito.when(sessionClient.list(any(), any(SessionStatus.class), any(java.time.LocalDateTime.class), any(java.time.LocalDateTime.class)))
                .thenReturn(List.of(s));

        mvc.perform(get("/sessions").param("status", SessionStatus.PLANNED.name()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sessions"));
    }

    @Test
    @WithMockUser
    void create_session_with_csrf_redirects_to_list() throws Exception {
        org.mockito.Mockito.when(userInfoSession.getUserId()).thenReturn("U1");

        PartnershipDTO p = new PartnershipDTO();
        p.setId(99L);
        p.setRequesterId("U1");
        p.setRequestedId("U2");
        org.mockito.Mockito.when(partnershipClient.getActivePartnerships("U1"))
                .thenReturn(ApiResponse.success(List.of(p)));

        SessionResponse created = new SessionResponse();
        created.setId(123L);
        org.mockito.Mockito.when(sessionClient.create(any(CreateSessionRequest.class))).thenReturn(created);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("partnerId", "U2")
                        .param("type", SessionType.VIRTUAL.name())
                        .param("locationOrLink", "https://meet")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions"));
    }
}


