package com.productdock.library.search;

import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toBookDto(BookIndex bookIndex);
}
