package com.aegis.eventmanagement.repository.device;

import com.aegis.eventmanagement.model.Device;
import com.aegis.eventmanagement.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class DeviceCustomizedRepositoryImpl<T, ID> implements DeviceCustomizedRepository<T, ID> {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void updateCount(String deviceId, int count) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        List<Device> devices = mongoTemplate.find(query, Device.class);
        Device device = devices.get(0);
        if (device.getSeqNo() > 50000) {
            update.set("rollOverCount", device.getRollOverCount() + 1);
            update.set("seqNo", 1);
        } else {
            long newCount = device.getSeqNo() + count;
            update.set("seqNo", newCount);
        }
        mongoTemplate.findAndModify(query, update, Device.class);
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("isDeleted", "true");
        mongoTemplate.findAndModify(query, update, Event.class);
    }

}
