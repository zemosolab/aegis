package com.aegis.eventmanagement.repository.event;

import com.aegis.eventmanagement.helper.Helper;
import com.aegis.eventmanagement.model.Event;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;


    @Test
    public void findAndDelete() {
        Event event = (Event) Helper.populate(new Event(), Event.class);
        eventRepository.save(event);
        Event actual = eventRepository.findById(event.getId()).get();
        Assert.assertEquals(event.getType(), actual.getType());
        eventRepository.deleteById(actual.getId());
        Assert.assertTrue(!eventRepository.findById(event.getId()).isPresent());

    }

    @Test
    public void findByDeviceId() {
        Event event = (Event) Helper.populate(new Event(), Event.class);
        eventRepository.save(event);
        Event event2 = (Event) Helper.populate(new Event(), Event.class);
        event2.setDeviceId(event.getDeviceId());
        eventRepository.save(event2);
        List<Event> actual = eventRepository.findByDeviceId(event.getDeviceId());
        Assert.assertEquals(actual.size(), 2);
        eventRepository.deleteById(actual.get(0).getId());
        eventRepository.deleteById(actual.get(1).getId());
    }

    @Test
    public void deleteByDeviceId() {
        Event event = (Event) Helper.populate(new Event(), Event.class);
        eventRepository.save(event);
        Event event2 = (Event) Helper.populate(new Event(), Event.class);
        event2.setDeviceId(event.getDeviceId());
        event2.setType(event.getType());
        eventRepository.save(event2);
        Assert.assertEquals(eventRepository.findByDeviceId(event.getDeviceId()).size(), 2);
        eventRepository.deleteByTypeAndDeviceId(event.getType(), event.getDeviceId());
        Assert.assertEquals(eventRepository.findByDeviceId(event.getDeviceId()).size(), 0);

    }

}
