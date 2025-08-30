package com.app.webapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.security.CustomUserDetailsService;
import com.app.webapp.security.SecurityConfig;
import com.app.webapp.security.UserInfoSession;

@WebMvcTest(
  controllers = PartnershipController.class,
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
class PartnershipControllerCsrfTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PartnershipClient partnershipClient;

    @MockBean
    private UserInfoSession userInfoSession;

    @MockBean
    private UserClient userClient;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void createRequest_withCsrf_returns200Json() throws Exception {
        when(userInfoSession.getUserId()).thenReturn("U1");
        com.app.webapp.dto.PartnershipDTO dto = new com.app.webapp.dto.PartnershipDTO();
        dto.setId(1L);
        when(partnershipClient.createPartnershipRequest(any())).thenReturn(ApiResponse.success(dto));

        mvc.perform(post("/partnerships/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requestedId\":\"U2\",\"message\":\"hi\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createRequest_withoutCsrf_returns403() throws Exception {
        mvc.perform(post("/partnerships/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requestedId\":\"U2\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void accept_withCsrf_redirects() throws Exception {
        mvc.perform(post("/partnerships/10/accept").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/partnerships"));
        verify(partnershipClient).acceptPartnership(10L);
    }

    @Test
    @WithMockUser
    void accept_withoutCsrf_403() throws Exception {
        mvc.perform(post("/partnerships/10/accept"))
                .andExpect(status().isForbidden());
        verify(partnershipClient, never()).acceptPartnership(anyLong());
    }

    @Test
    @WithMockUser
    void deny_withCsrf_redirects() throws Exception {
        mvc.perform(post("/partnerships/10/deny").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/partnerships"));
        verify(partnershipClient).denyPartnership(10L);
    }

    @Test
    @WithMockUser
    void deny_withoutCsrf_403() throws Exception {
        mvc.perform(post("/partnerships/10/deny"))
                .andExpect(status().isForbidden());
        verify(partnershipClient, never()).denyPartnership(anyLong());
    }
}


