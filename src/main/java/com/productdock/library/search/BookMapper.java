package com.productdock.library.search;

import com.productdock.library.search.cosumer.messages.InsertBook;
import com.productdock.library.search.elastic.document.BookDocument;
import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toBookDto(BookDocument bookDocument);

    BookDocument toBookDocument(InsertBook insertBookMessage);

}
