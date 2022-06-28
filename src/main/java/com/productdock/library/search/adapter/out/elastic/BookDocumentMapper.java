package com.productdock.library.search.adapter.out.elastic;

import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookDocumentMapper {

    @Mapping(source = "bookId", target = "id")
    Book toDomain(BookDocument source);

    @Mapping(source = "id", target = "bookId")
    BookDocument toEntity(Book source);
}
