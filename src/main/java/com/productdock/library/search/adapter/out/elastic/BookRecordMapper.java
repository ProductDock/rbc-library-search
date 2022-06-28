package com.productdock.library.search.adapter.out.elastic;

import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookRecordMapper {

    Book.RentalState.Record toRecord(BookDocument.RentalState.Record recordDoc);

    List<Book.RentalState.Record> toRecords(List<BookDocument.RentalState.Record> records);
}
