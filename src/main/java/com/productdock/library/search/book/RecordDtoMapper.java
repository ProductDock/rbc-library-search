package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.Record;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RecordDtoMapper {

    RecordDto toRecordDto(Record recordDocument);

    List<RecordDto> toRecordsDto(List<Record> records);
}
