package com.productdock.library.search.adapter.in.web.mapper;

import com.productdock.library.search.adapter.in.web.dto.BookDto;
import com.productdock.library.search.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookRecordMapper {

    BookDto.Record toRecord(Book.RentalState.Record recordDoc);

    List<BookDto.Record> toRecords(List<Book.RentalState.Record> records);
}
