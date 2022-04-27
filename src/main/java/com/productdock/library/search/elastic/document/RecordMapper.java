package com.productdock.library.search.elastic.document;

import com.productdock.library.search.kafka.cosumer.messages.BookRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RecordMapper {

    Record toRecord(BookRecord bookRecord);

    List<Record> toRecords(List<BookRecord> bookRecords);

}
