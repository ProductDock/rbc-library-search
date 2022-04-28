package com.productdock.library.search.elastic;

import com.productdock.library.search.elastic.document.Record;
import com.productdock.library.search.kafka.cosumer.messages.BookRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RecordDocumentMapper {

    Record toRecord(BookRecord bookRecord);

    List<Record> toRecords(List<BookRecord> bookRecords);

}
