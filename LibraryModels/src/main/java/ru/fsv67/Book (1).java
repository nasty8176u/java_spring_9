package ru.fsv67;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Schema(description = "Сущность книги")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Идентификатор книги")
    private Long id;

    @Column(name = "title")
    @Schema(description = "Поле название книги")
    private String title;

    public Book(String title) {
        this.title = title;
    }
}
