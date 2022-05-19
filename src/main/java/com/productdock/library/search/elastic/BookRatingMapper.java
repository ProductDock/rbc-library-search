package com.productdock.library.search.elastic;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.BookRatingMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookRatingMapper {

    @Mapping(source = "rating", target = "score")
    @Mapping(source = "ratingsCount", target = "count")
    BookDocument.Rating toRating(BookRatingMessage bookRatingMessage);
}
