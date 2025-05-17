package com.tds.challenge.urlshortener.domain.model.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO {
    private String originalUrl;
    private String shortUrl;
    private long accessCount;
    private LocalDateTime createdAt;
}
