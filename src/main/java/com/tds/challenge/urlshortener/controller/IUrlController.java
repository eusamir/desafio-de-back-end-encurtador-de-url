package com.tds.challenge.urlshortener.controller;

import com.tds.challenge.urlshortener.domain.model.dto.out.UrlDTO;
import com.tds.challenge.urlshortener.domain.model.dto.in.UrlShortenRequestDTO;
import com.tds.challenge.urlshortener.domain.model.dto.out.UrlStatsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Encurtador de URLs", description = "Endpoints para encurtar e consultar URLs")
@Validated
@RequestMapping("/api/v1")
public interface IUrlController {

    @Operation(
            summary = "Encurtar URL",
            description = "Gera uma URL encurtada com base na URL original enviada"
    )
    @ApiResponse(
            responseCode = "200",
            description = "URL encurtada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UrlDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "originalUrl": "https://exemplo.com",
                          "shortUrl": "http://localhost:8080/abc123",
                          "accessCount": 0,
                          "createdAt": "2025-05-17T12:00:00"
                        }
                    """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 400,
                      "message": "A URL original não pode ser nula ou vazia"
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 404,
                      "message": "Not found"
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 500,
                      "message": "Internal server error"
                    }
                    """
                    )
            )
    )
    @PostMapping("/shorten")
    ResponseEntity<UrlDTO> shortenUrl(@RequestBody UrlShortenRequestDTO request, HttpServletRequest servletRequest);

    @Operation(
            summary = "Redirecionar URL encurtada",
            description = "Redireciona para a URL original com base no código encurtado"
    )
    @ApiResponse(
            responseCode = "302",
            description = "Redirecionamento realizado com sucesso",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Código de URL não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 404,
                      "message": "Not found"
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 500,
                      "message": "Internal server error"
                    }
                    """
                    )
            )
    )
    @GetMapping("/{id}")
    ResponseEntity<Void> redirect(@PathVariable String id);

    @Operation(
            summary = "Consultar estatísticas da URL",
            description = "Retorna estatísticas de acesso da URL encurtada, como total de acessos e média diária"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estatísticas retornadas com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UrlStatsDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "shortenCode": "abc123",
                          "shortUrl": "http://localhost:8080/abc123",
                          "fullUrl": "https://exemplo.com",
                          "totalAccesses": 20,
                          "averagePerDay": 6.6
                        }
                    """)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Código de URL não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 404,
                      "message": "URL não encontrada"
                    }
                    """
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Requisição inválida",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            example = """
                    {
                      "status": 500,
                      "message": "Internal server error"
                    }
                    """
                    )
            )
    )
    @GetMapping("/{id}/stats")
    ResponseEntity<UrlStatsDTO> getStats(@PathVariable String id);
}
