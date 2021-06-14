package com.hospitalplatform.hospital_platform.mercury.repository;

import com.hospitalplatform.hospital_platform.mercury.message.Message;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND tag = $1")
    List<Message> findAllByTag(String tag);
}
