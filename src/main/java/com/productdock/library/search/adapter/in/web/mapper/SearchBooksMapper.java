package com.productdock.library.search.adapter.in.web.mapper;


import com.productdock.library.search.domain.Book;
import com.productdock.library.search.adapter.in.web.dto.BookDto;
import com.productdock.library.search.adapter.in.web.dto.SearchBooksResponse;
import com.productdock.library.search.domain.ds.SearchBooksResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SearchBooksMapper {

    SearchBooksResponse toSearchBooksResponse(SearchBooksResult searchBooksResult);

    Iterable<BookDto> toBooksDto(Iterable<Book> books);
}
