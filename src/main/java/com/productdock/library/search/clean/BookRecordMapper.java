package com.productdock.library.search.clean;

import com.productdock.library.search.clean.Book;
import com.productdock.library.search.elastic.document.BookDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookRecordMapper {

    Book.Record toRecord(BookDocument.RentalState.Record recordDoc);

    List<Book.Record> toRecords(List<BookDocument.RentalState.Record> records);
}
