package com.aegis.eventmanagement.repository.event;

import com.aegis.eventmanagement.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class EventCustomizedRepositoryImpl<T, ID> implements EventCustomizedRepository<T, ID> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteById(ID id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("isDeleted", "true");
        mongoTemplate.findAndModify(query, update, Event.class);
    }

    @Override
    public void deleteByTypeAndDeviceId(Event.EventType type, String deviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(type));
        query.addCriteria(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("isDeleted", "true");
        mongoTemplate.updateMulti(query, update, Event.class);
    }
}
