package com.app.sessionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.sessionservice.client.dto.ApiResponse;
import com.app.sessionservice.client.dto.PartnershipDTO;

@FeignClient(name = "partnership")
public interface PartnershipClient {

    @GetMapping("/api/partnerships/{partnershipId}")
    ApiResponse<PartnershipDTO> getPartnership(@PathVariable("partnershipId") Long partnershipId);
}
