package com.aegis.eventmanagement.repository.event;

import com.aegis.eventmanagement.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends MongoRepository<Event, String>, EventCustomizedRepository<Event, String> {

    @Query("{deviceId:?1,type:?0,isDeleted:false}")
    List<Event> findByTypeAndDeviceId(String type, String deviceId);

    @Query("{id:?0,isDeleted:false}")
    Optional<Event> findById(String id);

    @Query("{deviceId:?0,isDeleted:false}")
    List<Event> findByDeviceId(String deviceId);

    @Query("{isDeleted:false,type:?0}")
    List<Event> findByType(Event.EventType type);

    @Query("{isDeleted:false}")
    List<Event> findAllEvents();


}
