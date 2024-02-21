package ru.fsv67;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс описывает процесс выдачи книги в БД
 */
@Entity
@Data
@NoArgsConstructor
@Schema(description = "Сущность выдачи")
public class Issuance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Идентификатор выдачи")
    private Long id;

    @Column(name = "book_id")
    @Schema(description = "Ссылка идентификатор книги")
    private Long bookId;

    @Column(name = "reader_id")
    @Schema(description = "Ссылка идентификатор читателя")
    private Long readerId;
    /**
     * Дата выдачи книги
     */
    @Column(name = "issuance_at")
    @Schema(description = "Дата и время выдачи книги")
    private LocalDateTime issuance_at;
    /**
     * Дата возврата книги
     */
    @Column(name = "returned_at")
    @Schema(description = "Дата и время возврата книги")
    private LocalDateTime returned_at;

    public Issuance(long bookId, long readerId) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.issuance_at = LocalDateTime.now();
    }
}
