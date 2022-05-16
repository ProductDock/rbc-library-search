package com.productdock.library.search.elastic;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RentalStateRecordMapper {

    @Mapping(source = "patron", target = "email")
    BookDocument.RentalState.Record toRecord(RentalMessage.Record bookRecord);

    List<BookDocument.RentalState.Record> toRecords(List<RentalMessage.Record> bookRecords);

}
