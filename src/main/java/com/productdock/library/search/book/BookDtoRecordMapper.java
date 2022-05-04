package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookDtoRecordMapper {

    BookDto.Record toRecordDto(BookDocument.RentalState.Record record);

    List<BookDto.Record> toRecordsDto(List<BookDocument.RentalState.Record> records);
}
