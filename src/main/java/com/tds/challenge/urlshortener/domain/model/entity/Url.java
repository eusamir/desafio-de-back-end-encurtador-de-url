package com.tds.challenge.urlshortener.domain.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "urls")
public class Url {
    @Id
    private String shortenCode;

    private String shortenUrl;

    private String fullUrl;

    private LocalDateTime createdAt;

    private long accessCount;
}
