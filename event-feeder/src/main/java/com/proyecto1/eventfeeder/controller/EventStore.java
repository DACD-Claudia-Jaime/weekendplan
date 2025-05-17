package com.proyecto1.eventfeeder.controller;
import com.proyecto1.eventfeeder.model.SocialEvent;

public interface EventStore {

    void saveEvent(SocialEvent event);
}
