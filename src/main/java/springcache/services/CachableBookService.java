package springcache.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CachableBookService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CachableBookService.class);

  @Autowired
  private BookRepository bookRepository;

  @Cacheable("book")
  public Book getBookByIsbn(String isbn) {
    LOGGER.info("BookService, searching for book by {}", isbn);
    simulateSlowService();
    Book book = bookRepository.findOne(isbn);
    LOGGER.info("BookService, returning book");
    return book;
  }

  @CachePut(value="book", key="#book.isbn")
  public Book save(Book book) {
    LOGGER.info("BookService, saving book {}", book);
    book = bookRepository.save(book);
    LOGGER.info("BookService, save done");
    return book;
  }

  private void simulateSlowService() {
    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

}
