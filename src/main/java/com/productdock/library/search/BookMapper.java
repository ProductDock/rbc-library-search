package com.productdock.library.search;

import org.mapstruct.*;

import java.util.List;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toBookDto(BookIndex bookIndex);

    List<BookDto> toBookDtoCollection(List<BookIndex> bookIndexCollection);
}
