package com.productdock.library.search;

import com.productdock.library.search.cosumer.messages.InsertBook;
import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toBookDto(BookIndex bookIndex);

    BookIndex toBookIndex(InsertBook insertBookMessage);

}
