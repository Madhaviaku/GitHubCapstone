package com.library.catalog.service;

import com.library.catalog.dto.BookCreateRequest;
import com.library.catalog.dto.BookResponse;
import com.library.catalog.dto.BookUpdateRequest;
import com.library.catalog.entity.Book;
import com.library.catalog.exception.BookNotFoundException;
import com.library.catalog.exception.DuplicateIsbnException;
import com.library.catalog.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Page<BookResponse> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(this::toResponse);
    }

    public BookResponse getBookByIsbn(String isbn) {
        String normalizedIsbn = normalizeIsbn(isbn);
        Book book = repository.findByIsbn(normalizedIsbn)
                .orElseThrow(() -> new BookNotFoundException(normalizedIsbn));
        return toResponse(book);
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        String isbn = normalizeIsbn(request.isbn());
        validateText(request.title(), "Title", 200, true);
        validateText(request.author(), "Author", 100, true);
        validateText(request.genre(), "Genre", 50, true);
        validateText(request.publisher(), "Publisher", 100, false);
        validateQuantity(request.quantityAvailable());
        validatePublicationDate(request.publicationDate());
        if (repository.existsByIsbn(isbn)) {
            throw new DuplicateIsbnException(isbn);
        }

        Book book = new Book(
                isbn,
                request.title().trim(),
                request.author().trim(),
                request.publisher() == null ? null : request.publisher().trim(),
                request.publicationDate(),
                request.genre().trim(),
                request.quantityAvailable() == null ? 0 : request.quantityAvailable()
        );
        return toResponse(repository.save(book));
    }

    @Transactional
    public BookResponse updateBook(String isbn, BookUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        String normalizedIsbn = normalizeIsbn(isbn);
        Book existing = repository.findByIsbn(normalizedIsbn)
                .orElseThrow(() -> new BookNotFoundException(normalizedIsbn));

        validateText(request.title(), "Title", 200, false);
        validateText(request.author(), "Author", 100, false);
        validateText(request.genre(), "Genre", 50, false);
        validateText(request.publisher(), "Publisher", 100, false);
        validateQuantity(request.quantityAvailable());
        validatePublicationDate(request.publicationDate());

        if (request.title() != null) {
            existing.setTitle(request.title().trim());
        }
        if (request.author() != null) {
            existing.setAuthor(request.author().trim());
        }
        if (request.publisher() != null) {
            existing.setPublisher(request.publisher().trim());
        }
        if (request.publicationDate() != null) {
            existing.setPublicationDate(request.publicationDate());
        }
        if (request.genre() != null) {
            existing.setGenre(request.genre().trim());
        }
        if (request.quantityAvailable() != null) {
            existing.setQuantityAvailable(request.quantityAvailable());
        }
        return toResponse(repository.save(existing));
    }

    @Transactional
    public void deleteBook(String isbn) {
        String normalizedIsbn = normalizeIsbn(isbn);
        Book existing = repository.findByIsbn(normalizedIsbn)
                .orElseThrow(() -> new BookNotFoundException(normalizedIsbn));
        repository.delete(existing);
    }

    public Page<BookResponse> searchBooks(String title, int page, int size) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Search title is required");
        }
        String searchTerm = title.trim();
        String normalizedIsbn = searchTerm.replace("-", "");
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCaseOrIsbnContainingIgnoreCaseOrderByCreatedAtDesc(
                        searchTerm,
                        searchTerm,
                        searchTerm,
                        normalizedIsbn,
                        pageable)
                .map(this::toResponse);
    }

    public String normalizeIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        String formatted = isbn.trim();
        if (!formatted.matches("(?:\\d-?){9}\\d|(?:\\d-?){12}\\d")) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        return formatted.replace("-", "");
    }

    private void validateText(String value, String field, int maxLength, boolean required) {
        if (required && (value == null || value.isBlank())) {
            throw new IllegalArgumentException(field + " is required");
        }
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(field + " length must not exceed " + maxLength + " characters");
        }
        if (!required && value != null && value.isBlank()
                && !field.equals("Publisher")) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
    }

    private void validatePublicationDate(LocalDate publicationDate) {
        if (publicationDate != null && publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future");
        }
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationDate(),
                book.getGenre(),
                book.getQuantityAvailable(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
