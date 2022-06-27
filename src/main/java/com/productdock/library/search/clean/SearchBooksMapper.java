package com.productdock.library.search.clean;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SearchBooksMapper {

    SearchBooksResponse toSearchBooksResponse(SearchBooksResult searchBooksResult);

    Iterable<BookDto> toBooksDto(Iterable<Book> books);
}
