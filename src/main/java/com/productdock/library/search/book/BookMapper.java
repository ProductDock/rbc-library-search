package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.cosumer.messages.InsertBookMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {RecordDtoMapper.class})
public interface BookMapper {

    @Mapping(source="bookStatusWrapper.records", target="records")
    BookDto toBookDto(BookDocument bookDocument);

    BookDocument toBookDocument(InsertBookMessage insertBookMessage);

}
