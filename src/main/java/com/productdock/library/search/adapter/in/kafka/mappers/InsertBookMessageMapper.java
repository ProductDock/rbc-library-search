package com.productdock.library.search.adapter.in.kafka.mappers;

import com.productdock.library.search.adapter.in.kafka.messages.InsertBookMessage;
import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InsertBookMessageMapper {

    @Mapping(source = "bookId", target = "id")
    Book toBook(InsertBookMessage insertBookMessage);

}
