package com.productdock.library.search.application.port.in;

import com.productdock.library.search.domain.BookChanges;

public interface UpdateBookUseCase {

    void updateBook(String bookId, BookChanges changes);
}
