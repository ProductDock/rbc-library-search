package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class BookMapper {

    @Autowired
    private BookDtoRecordMapper recordDtoMapper;

    public BookDto toBookDto(BookDocument bookDocument) {
        var bookDto = new BookDto();
        bookDto.id = bookDocument.getBookId();
        bookDto.title = bookDocument.getTitle();
        bookDto.author = bookDocument.getAuthor();
        bookDto.cover = bookDocument.getCover();

        var records = getBookDocumentRecords(bookDocument);
        int availableBookCount = getAvailableBooksCount(bookDocument);
        for (int i = 1; i <= availableBookCount; i++) {
            var bookRecord = BookDocument.RentalState.Record.builder().status(BookStatus.AVAILABLE).build();
            records.add(bookRecord);
        }
        bookDto.records = recordDtoMapper.toRecordsDto(records);

        return bookDto;
    }

    private List<BookDocument.RentalState.Record> getBookDocumentRecords(BookDocument bookDocument) {
        var bookRentalState = bookDocument.getRentalState();
        if (bookRentalState == null) {
            return Collections.emptyList();
        }
        return bookRentalState.getRecords();
    }

    private int getAvailableBooksCount(BookDocument bookDocument) {
        var bookRentalState = bookDocument.getRentalState();
        if (bookRentalState == null) {
            return 0;
        }
        return bookRentalState.getAvailableBooksCount();
    }

    public BookDocument toBookDocument(InsertBookMessage insertBookMessage) {
        return BookDocument.builder()
                .bookId(insertBookMessage.getBookId())
                .title(insertBookMessage.getTitle())
                .author(insertBookMessage.getAuthor())
                .cover(insertBookMessage.getCover())
                .topics(bookTopicListToTopicCollection(insertBookMessage.getTopics()))
                .build();
    }

    protected BookDocument.Topic bookTopicToTopic(InsertBookMessage.Topic bookTopic) {
        return BookDocument.Topic.builder()
                .id(bookTopic.getId())
                .name(bookTopic.getName())
                .build();
    }

    protected Collection<BookDocument.Topic> bookTopicListToTopicCollection(List<InsertBookMessage.Topic> list) {
        return list.stream().map(this::bookTopicToTopic).toList();
    }
}
