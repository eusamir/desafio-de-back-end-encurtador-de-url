package com.tds.challenge.urlshortener.controller.rest;

import com.tds.challenge.urlshortener.controller.docs.IUrlController;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlController implements IUrlController {
    private final UrlService urlService;

    @PostMapping(value = "/shorten")
    public ResponseEntity<UrlDTO> shortenUrl(@Valid @RequestBody UrlShortenRequestDTO request, HttpServletRequest servletRequest) {
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
