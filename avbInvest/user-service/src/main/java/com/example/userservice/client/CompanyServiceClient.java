package com.example.userservice.client;

import com.example.userservice.dto.CompanyResponseDto;
import com.example.userservice.exps.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyServiceClient {

    private final RestClient restClient;

    public CompanyResponseDto getCompanyById(Integer companyId) {
        try {
            return restClient.get()
                    .uri("/api/companies/user/" + companyId)
                    .retrieve()
                    .body(CompanyResponseDto.class);
        } catch (RestClientException e) {
            log.error("Failed to fetch company with id={}", companyId, e);
            throw new RecordNotFoundException("Failed to fetch company with id: " + companyId);
        }
    }
}
