package com.tds.challenge.urlshortener.mapper;

import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.entity.Url;

import java.time.LocalDateTime;

public class UrlConverter {
    public static UrlDTO toDto(Url url, String baseUrl) {
        return new UrlDTO(
                url.getFullUrl(),
                baseUrl + "/api/v1/" + url.getShortenCode(),
                url.getAccessCount(),
                url.getCreatedAt()
        );
    }

    public static Url toEntity(UrlShortenRequestDTO dto, String id, String baseUrl) {
        return Url.builder()
                .shortenCode(id)
                .shortenUrl(baseUrl + "/api/v1/" + id)
                .fullUrl(dto.getUrl())
                .createdAt(LocalDateTime.now())
                .accessCount(0)
                .build();
    }
}
