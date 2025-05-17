package com.tds.challenge.urlshortener.domain.model.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenRequestDTO {
    @NotBlank(message = "A URL original não pode estar vazia")
    @Pattern(regexp = "^(http|https)://.*$", message = "A URL deve começar com http:// ou https://")
    private String url;
}
