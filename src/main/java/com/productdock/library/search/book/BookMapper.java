package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Stream.concat;

@Component
@AllArgsConstructor
@Slf4j
public class BookMapper {

    private BookDtoRecordMapper recordDtoMapper;

    public BookDto toBookDto(BookDocument bookDocument) {
        log.debug("Map BookDocument [{}] to BookDto", bookDocument);
        var bookDto = new BookDto();
        mapSimpleProperties(bookDocument, bookDto);
        mapRecords(bookDocument, bookDto);
        mapRating(bookDocument, bookDto);
        return bookDto;
    }

    private void mapRating(BookDocument bookDocument, BookDto bookDto) {
        log.debug("Map BookDocument rating {} to BookDto rating", bookDocument.getRating());
        if (bookDocument.getRating() != null){
            bookDto.rating.score = bookDocument.getRating().getScore();
            bookDto.rating.count = bookDocument.getRating().getCount();
        }
    }

    private void mapRecords(BookDocument bookDocument, BookDto bookDto) {
        log.debug("Map BookDocument records [{}] to BookDto records", bookDocument.getRentalState().getRecords());
        var records = bookDocument.getRentalState().getRecords();
        var availableBookCount = bookDocument.getRentalState().getAvailableBooksCount();
        var availableRecords = createAvailableRecords(availableBookCount);
        var allRecords = concat(records.stream(), availableRecords.stream()).toList();
        bookDto.records = recordDtoMapper.toRecordsDto(allRecords);
    }

    private List<BookDocument.RentalState.Record> createAvailableRecords(int availableBookCount) {
        log.debug("Create {} available records", availableBookCount);
        var bookRecords = new ArrayList<BookDocument.RentalState.Record>();
        for (int i = 1; i <= availableBookCount; i++) {
            bookRecords.add(new BookDocument.RentalState.Record(BookStatus.AVAILABLE));
        }
        return bookRecords;
    }

    private void mapSimpleProperties(BookDocument bookDocument, BookDto bookDto) {
        log.debug("Map simple properties from BookDocument [{}] to BookDto", bookDocument);
        bookDto.id = bookDocument.getBookId();
        bookDto.title = bookDocument.getTitle();
        bookDto.author = bookDocument.getAuthor();
        bookDto.cover = bookDocument.getCover();
    }

//    public BookDocument toBookDocument(InsertBookMessage insertBookMessage) {
//        log.debug("Create BookDocument from insertBookMessage: {}", insertBookMessage);
//        return BookDocument.builder()
//                .bookId(insertBookMessage.getBookId())
//                .title(insertBookMessage.getTitle())
//                .author(insertBookMessage.getAuthor())
//                .cover(insertBookMessage.getCover())
//                .topics(insertBookMessageTopicsToBookDocumentTopics(insertBookMessage.getTopics()))
//                .build();
//    }
//
//    private BookDocument.Topic insertBookMessageTopicToBookDocumentTopic(InsertBookMessage.Topic bookTopic) {
//        log.debug("Insert bookMessageTopic [{}] to BookDocumentTopic", bookTopic);
//        return BookDocument.Topic.builder()
//                .id(bookTopic.getId())
//                .name(bookTopic.getName())
//                .build();
//    }
//
//    private Collection<BookDocument.Topic> insertBookMessageTopicsToBookDocumentTopics(List<InsertBookMessage.Topic> list) {
//        log.debug("Insert bookMessageTopics [{}] to BookDocumentTopic", list);
//        return list.stream().map(this::insertBookMessageTopicToBookDocumentTopic).toList();
//    }
}
