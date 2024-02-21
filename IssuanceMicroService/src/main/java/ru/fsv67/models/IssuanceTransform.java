package ru.fsv67.models;

import lombok.Data;
import ru.fsv67.Book;
import ru.fsv67.Reader;

import java.time.LocalDateTime;

@Data
public class IssuanceTransform {
    private Long id;
    private Book book;
    private Reader reader;
    private LocalDateTime issuance_at;
    private LocalDateTime returned_at;
}
