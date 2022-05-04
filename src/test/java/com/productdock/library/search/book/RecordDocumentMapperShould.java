package com.productdock.library.search.book;

import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapperImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {RentalStateRecordMapperImpl.class})
public class RecordDocumentMapperShould {

    @Autowired
    private RentalStateRecordMapper rentalStateRecordMapper;


}
