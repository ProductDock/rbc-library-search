package com.productdock.library.search;

import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    BookDto toBookDto(BookIndex bookIndex);

    @Mapping(source = "totalHits", target = "totalHits")
    @Mapping(source = "items", target = "items")
    CountableCollectionDto toCountableCollectionDto(Long totalHits, Iterable<BookDto> items);
}
