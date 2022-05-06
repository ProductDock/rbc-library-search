package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class BookMapper {

    private BookDtoRecordMapper recordDtoMapper;

    public BookDto toBookDto(BookDocument bookDocument) {
        var bookDto = new BookDto();
        mapSimpleProperties(bookDocument, bookDto);
        mapRecords(bookDocument, bookDto);
        return bookDto;
    }

    private void mapRecords(BookDocument bookDocument, BookDto bookDto) {
        var records = bookDocument.getRentalState().getRecords();
        var availableBookCount = bookDocument.getRentalState().getAvailableBooksCount();
        records.addAll(createAvailableRecords(availableBookCount));
        bookDto.records = recordDtoMapper.toRecordsDto(records);
    }

    private Collection<BookDocument.RentalState.Record> createAvailableRecords(int availableBookCount) {
        var bookRecords = new ArrayList<BookDocument.RentalState.Record>();
        for (int i = 1; i <= availableBookCount; i++) {
            bookRecords.add(new BookDocument.RentalState.Record(BookStatus.AVAILABLE));
        }
        return bookRecords;
    }

    private void mapSimpleProperties(BookDocument bookDocument, BookDto bookDto) {
        bookDto.id = bookDocument.getBookId();
        bookDto.title = bookDocument.getTitle();
        bookDto.author = bookDocument.getAuthor();
        bookDto.cover = bookDocument.getCover();
    }

    public BookDocument toBookDocument(InsertBookMessage insertBookMessage) {
        return BookDocument.builder()
                .bookId(insertBookMessage.getBookId())
                .title(insertBookMessage.getTitle())
                .author(insertBookMessage.getAuthor())
                .cover(insertBookMessage.getCover())
                .topics(insertBookMessageTopicsToBookDocumentTopics(insertBookMessage.getTopics()))
                .build();
    }

    private BookDocument.Topic insertBookMessageTopicToBookDocumentTopic(InsertBookMessage.Topic bookTopic) {
        return BookDocument.Topic.builder()
                .id(bookTopic.getId())
                .name(bookTopic.getName())
                .build();
    }

    private Collection<BookDocument.Topic> insertBookMessageTopicsToBookDocumentTopics(List<InsertBookMessage.Topic> list) {
        return list.stream().map(this::insertBookMessageTopicToBookDocumentTopic).toList();
    }
}
