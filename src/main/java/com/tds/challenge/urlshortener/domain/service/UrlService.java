package com.tds.challenge.urlshortener.domain.service;

import com.tds.challenge.urlshortener.domain.model.dto.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.model.entity.Url;
import com.tds.challenge.urlshortener.domain.repository.IUrlRepository;
import com.tds.challenge.urlshortener.mapper.UrlConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final IUrlRepository iUrlRepository;

    public UrlDTO generateShortenUrl(UrlShortenRequestDTO request, HttpServletRequest servletRequest){

        String id;
        do{
            id = UUID.randomUUID()
                    .toString()
                    .replace("-","")
                    .substring(0, 10);
        }while (iUrlRepository.existsById(id));

        Url url = UrlConverter.toEntity(request, id);

        iUrlRepository.save(url);

        String baseUrl = servletRequest.getRequestURL().toString()
                .replace(servletRequest.getRequestURI(), "");

        return UrlConverter.toDto(url, baseUrl);
    }

    public String getFullUrlById(String id) {
        Url url = iUrlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL não encontrada"));

        url.setAccessCount(url.getAccessCount() + 1);
        iUrlRepository.save(url);

        return url.getFullUrl();
    }

    public UrlStatsDTO getStatsById(String id) {
        Url url = iUrlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL não encontrada"));

        long totalAccesses = url.getAccessCount();
        long days = Math.max(1, java.time.Duration.between(url.getCreatedAt(), LocalDateTime.now()).toDays());

        double averagePerDay = (double) totalAccesses / days;

        return new UrlStatsDTO(
                url.getShortenCode(),
                url.getFullUrl(),
                totalAccesses,
                averagePerDay
        );
    }
}
