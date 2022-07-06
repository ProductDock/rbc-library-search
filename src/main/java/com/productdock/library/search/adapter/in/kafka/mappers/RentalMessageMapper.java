package com.productdock.library.search.adapter.in.kafka.mappers;


import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;
import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RentalMessageMapper {

    List<Book.RentalState.Record> toRecords(List<RentalMessage.Record> source);

    @Mapping(source = "patron", target = "email")
    Book.RentalState.Record toRecord(RentalMessage.Record source);
}
