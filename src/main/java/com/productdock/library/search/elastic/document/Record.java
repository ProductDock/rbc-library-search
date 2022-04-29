package com.productdock.library.search.elastic.document;

import com.productdock.library.search.book.BookStatus;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Record {

    @Field(type = FieldType.Text)
    private String email;

    @NonNull
    @Field(type = FieldType.Text)
    private BookStatus status;
}
