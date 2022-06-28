package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.messages.InsertBookMessage;
import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InsertBookMessageMapper {

    Book toBook(InsertBookMessage insertBookMessage);

}
