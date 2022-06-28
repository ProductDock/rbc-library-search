package com.productdock.library.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookChanges {

    private BookField field;
    private Object value;

}
