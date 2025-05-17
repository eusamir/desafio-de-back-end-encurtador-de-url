package com.tds.challenge.urlshortener.domain.service;

import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlStatsDTO;
import com.tds.challenge.urlshortener.domain.model.entity.Url;
import com.tds.challenge.urlshortener.domain.repository.IUrlRepository;
import com.tds.challenge.urlshortener.mapper.UrlConverter;
import com.tds.challenge.urlshortener.suport.expection.BadRequestException;
import com.tds.challenge.urlshortener.suport.expection.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final IUrlRepository iUrlRepository;

    private void validateRequest(UrlShortenRequestDTO request) {
        if (request == null || request.getUrl() == null || request.getUrl().isBlank()) {
            throw new BadRequestException("A URL original não pode ser nula ou vazia");
        }
    }

    private String generateUniqueId() {
        String id;
        do {
            id = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 10);
        } while (iUrlRepository.existsById(id));
        return id;
    }

    public UrlDTO generateShortenUrl(UrlShortenRequestDTO request, HttpServletRequest servletRequest){
        validateRequest(request);


        try {
            String uniqueId = generateUniqueId();

            String baseUrl = servletRequest.getRequestURL().toString()
                    .replace(servletRequest.getRequestURI(), "");

            Url url = UrlConverter.toEntity(request, uniqueId, baseUrl);
            iUrlRepository.save(url);


            return UrlConverter.toDto(url, baseUrl);

        } catch (Exception ex) {
            throw new BadRequestException("Erro ao gerar URL encurtada: " + ex.getMessage());
        }
    }

    public String getFullUrlById(String id) {
        Url url = iUrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("URL não encontrada"));

        url.setAccessCount(url.getAccessCount() + 1);
        iUrlRepository.save(url);

        return url.getFullUrl();
    }

    public UrlStatsDTO getStatsById(String id) {
        Url url = iUrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("URL não encontrada"));

        long totalAccesses = url.getAccessCount();
        long days = Math.max(1, java.time.Duration.between(url.getCreatedAt(), LocalDateTime.now()).toDays());

        double averagePerDay = (double) totalAccesses / days;

        return new UrlStatsDTO(
                url.getShortenCode(),
                url.getShortenUrl(),
                url.getFullUrl(),
                totalAccesses,
                averagePerDay
        );
    }
}
