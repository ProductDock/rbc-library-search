package com.productdock.library.search.clean;

import com.productdock.library.search.elastic.document.BookDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookSearchSuggestionDtoMapper {

    @Mapping(source = "bookId", target = "id")
    BookSearchSuggestionDto toBookSearchSuggestionDto(BookDocument source);

}
