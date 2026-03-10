package com.sampleproject.sampleproject.repository;

import com.sampleproject.sampleproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
