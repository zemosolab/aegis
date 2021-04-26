package com.aegis.devicemanagement.repository.device;

import com.aegis.devicemanagement.model.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class DeviceCustomizedRepositoryImpl<T, ID> implements DeviceCustomizedRepository<T, ID> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteById(ID id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("isDeleted", "true");
        mongoTemplate.findAndModify(query, update, Device.class);
    }
}
