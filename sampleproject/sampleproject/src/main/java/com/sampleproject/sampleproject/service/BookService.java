package com.sampleproject.sampleproject.service;

import com.sampleproject.sampleproject.dto.BookRequest;
import com.sampleproject.sampleproject.exception.ResourceNotFoundException;
import com.sampleproject.sampleproject.model.Book;
import com.sampleproject.sampleproject.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setPages(request.getPages());
        book.setPublishedYear(request.getPublishedYear());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(String author, String genre) {
        List<Book> books = bookRepository.findAll(Sort.by("id"));

        if (author != null) {
            books = books.stream()
                    .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
        }
        if (genre != null) {
            books = books.stream()
                    .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }
        return books;
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}
