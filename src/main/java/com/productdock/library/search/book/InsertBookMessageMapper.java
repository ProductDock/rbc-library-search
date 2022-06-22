package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface InsertBookMessageMapper {

    BookDocument toBookDocument(InsertBookMessage insertBookMessage);

}
