package com.productdock.library.search.adapter.in.web.mapper;

import com.productdock.library.search.adapter.in.web.dto.BookSearchSuggestionDto;
import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookSearchSuggestionDtoMapper {

    BookSearchSuggestionDto toDto(Book source);

}
