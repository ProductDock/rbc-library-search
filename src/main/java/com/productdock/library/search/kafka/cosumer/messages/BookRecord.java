package com.productdock.library.search.kafka.cosumer.messages;

import com.productdock.library.search.book.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRecord {

    private String email;
    private BookStatus status;
}
