package com.tds.challenge.urlshortener.controller;

import com.tds.challenge.urlshortener.controller.rest.UrlController;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Autowired
    private ObjectMapper objectMapper;

    private UrlDTO urlDTO;
    private UrlStatsDTO urlStatsDTO;
    private UrlShortenRequestDTO urlShortenRequestDTO;

    @BeforeEach
    void setUp() {
        urlShortenRequestDTO = new UrlShortenRequestDTO("http://example.com");

        urlDTO = new UrlDTO();
        urlDTO.setOriginalUrl("http://example.com");
        urlDTO.setShortUrl("http://localhost:8080/abc123");

        urlStatsDTO = new UrlStatsDTO();
        urlStatsDTO.setShortenCode("abc123");
        urlStatsDTO.setFullUrl("http://example.com");
        urlStatsDTO.setTotalAccesses(10);
        urlStatsDTO.setAverageAccessesPerDay(2.0);
        urlStatsDTO.setShortUrl("http://localhost:8080/api/v1/abc123");
    }

    @Test
    void testShortenUrl() throws Exception {
        Mockito.when(urlService.generateShortenUrl(any(UrlShortenRequestDTO.class), any()))
                .thenReturn(urlDTO);

        mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urlShortenRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://example.com"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/abc123"));
    }

    @Test
    void testRedirect() throws Exception {
        Mockito.when(urlService.getFullUrlById("abc123"))
                .thenReturn("http://example.com");

        mockMvc.perform(get("/api/v1/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://example.com"));
    }

    @Test
    void testGetStats() throws Exception {
        Mockito.when(urlService.getStatsById("abc123"))
                .thenReturn(urlStatsDTO);

        mockMvc.perform(get("/api/v1/abc123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortenCode").value("abc123"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/api/v1/abc123"))
                .andExpect(jsonPath("$.fullUrl").value("http://example.com"))
                .andExpect(jsonPath("$.totalAccesses").value(10))
                .andExpect(jsonPath("$.averageAccessesPerDay").value(2.0));
    }
}
