package com.library.catalog.repository;

import com.library.catalog.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCaseOrIsbnContainingIgnoreCaseOrderByCreatedAtDesc(
            String title,
            String author,
            String genre,
            String isbn,
            Pageable pageable);

    boolean existsByIsbn(String isbn);
}
