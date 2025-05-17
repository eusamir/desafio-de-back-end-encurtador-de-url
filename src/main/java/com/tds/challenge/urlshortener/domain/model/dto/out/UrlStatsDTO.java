package com.tds.challenge.urlshortener.domain.model.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatsDTO {
    private String shortenCode;
    private String shortUrl;
    private String fullUrl;
    private long totalAccesses;
    private double averageAccessesPerDay;
}
