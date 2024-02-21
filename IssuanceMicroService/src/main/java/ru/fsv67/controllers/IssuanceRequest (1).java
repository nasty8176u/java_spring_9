package ru.fsv67.controllers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Класс описывает запрос на выдачу книги
 */
@Data
@Schema(description = "Сущность запроса на выдачу")
public class IssuanceRequest {
    /**
     * Идентификатор читателя
     */
    @Schema(description = "Ссылка идентификатор читателя")
    private long readerId;
    /**
     * Идентификатор книги
     */
    @Schema(description = "Ссылка идентификатор книги")
    private long bookId;
}
