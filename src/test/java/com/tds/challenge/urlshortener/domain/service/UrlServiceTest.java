package com.tds.challenge.urlshortener.domain.service;

import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.model.entity.Url;
import com.tds.challenge.urlshortener.domain.repository.IUrlRepository;
import com.tds.challenge.urlshortener.suport.expection.BadRequestException;
import com.tds.challenge.urlshortener.suport.expection.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UrlServiceTest {
    private IUrlRepository iUrlRepository;
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        iUrlRepository = mock(IUrlRepository.class);
        urlService = new UrlService(iUrlRepository);
    }

    @Test
    void shouldGenerateShortenUrlSuccessfully() {
        UrlShortenRequestDTO requestDTO = new UrlShortenRequestDTO("http://example.com");
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        when(servletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/v1/shorten"));
        when(servletRequest.getRequestURI()).thenReturn("/api/v1/shorten");
        when(iUrlRepository.existsById(anyString())).thenReturn(false);

        ArgumentCaptor<Url> urlCaptor = ArgumentCaptor.forClass(Url.class);
        when(iUrlRepository.save(urlCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        UrlDTO result = urlService.generateShortenUrl(requestDTO, servletRequest);

        assertNotNull(result);
        assertEquals("http://example.com", result.getOriginalUrl());
        assertTrue(result.getShortUrl().startsWith("http://localhost:8080/"));

        verify(iUrlRepository).save(any(Url.class));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenUrlIsNull() {
        UrlShortenRequestDTO requestDTO = new UrlShortenRequestDTO(null);
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            urlService.generateShortenUrl(requestDTO, servletRequest);
        });

        assertEquals("A URL original não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void shouldReturnFullUrlWhenIdExists() {
        String id = "abc123defg";
        Url url = new Url();
        url.setFullUrl("http://example.com");
        url.setAccessCount(5);
        url.setShortenCode(id);

        when(iUrlRepository.findById(id)).thenReturn(Optional.of(url));
        when(iUrlRepository.save(any(Url.class))).thenReturn(url);

        String fullUrl = urlService.getFullUrlById(id);

        assertEquals("http://example.com", fullUrl);
        assertEquals(6, url.getAccessCount());
        verify(iUrlRepository).save(url);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenIdNotExists() {
        String id = "nonexistent";
        when(iUrlRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            urlService.getFullUrlById(id);
        });

        assertEquals("URL não encontrada", exception.getMessage());
    }

    @Test
    void shouldReturnStatsWhenIdExists() {
        String id = "stat123456";
        Url url = new Url();
        url.setShortenCode(id);
        url.setFullUrl("http://example.com");
        url.setShortenUrl("http://localhost:8080/" + id); // Adicionando o campo shortenUrl
        url.setAccessCount(10);
        url.setCreatedAt(LocalDateTime.now().minusDays(5));

        when(iUrlRepository.findById(id)).thenReturn(Optional.of(url));

        UrlStatsDTO stats = urlService.getStatsById(id);

        assertEquals(id, stats.getShortenCode());
        assertEquals("http://localhost:8080/" + id, stats.getShortUrl());
        assertEquals("http://example.com", stats.getFullUrl());
        assertEquals(10, stats.getTotalAccesses());
        assertTrue(stats.getAverageAccessesPerDay() > 0);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenStatsIdNotExists() {
        String id = "no_stats";
        when(iUrlRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            urlService.getStatsById(id);
        });

        assertEquals("URL não encontrada", ex.getMessage());
    }
}