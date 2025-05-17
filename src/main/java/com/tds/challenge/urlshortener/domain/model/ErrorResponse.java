package com.tds.challenge.urlshortener.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "errorResponse", description = "Dados de uma resposta de erro.")
public class ErrorResponse {
    @Schema(description = "Código do erro", example = "400")
    @JsonProperty("status")
    private int status;

    @Schema(description = "Mensagem do erro", example = "Bad Request")
    @JsonProperty("message")
    private String message;

}
