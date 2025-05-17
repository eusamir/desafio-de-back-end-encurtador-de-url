package com.tds.challenge.urlshortener.controller;

import com.tds.challenge.urlshortener.domain.model.dto.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlController implements IUrlController{
    private final UrlService urlService;

    @PostMapping(value = "/shorten")
    public ResponseEntity<UrlDTO> shortenUrl(@RequestBody UrlShortenRequestDTO request, HttpServletRequest servletRequest) {
        UrlDTO urlDTO = urlService.generateShortenUrl(request, servletRequest);
        return ResponseEntity.ok(urlDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        String fullUrl = urlService.getFullUrlById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(fullUrl));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<UrlStatsDTO> getStats(@PathVariable String id) {
        return ResponseEntity.ok(urlService.getStatsById(id));
    }

}
