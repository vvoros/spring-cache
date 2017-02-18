package springcache.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import springcache.services.Book;
import springcache.services.CachableBookService;

@RestController
public class WorkerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerController.class);

  @Autowired
  private CachableBookService bookService;

  @GetMapping(value = "book/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
    LOGGER.info("Controller, getting book from service by {}", isbn);
    Book book = bookService.getBookByIsbn(isbn);
    if (book == null) {
      LOGGER.info("Controller, book was not found");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    LOGGER.info("Controller, returning book: {}", book);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @GetMapping(value = "books/{isbn}")
  public ResponseEntity<Void> saveBook(@PathVariable String isbn) {
    LOGGER.info("Controller, saving book via service by {}", isbn);
    Book book = new Book(isbn, "Title");
    bookService.save(book);
    LOGGER.info("Controller, book saved: {}", book);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
