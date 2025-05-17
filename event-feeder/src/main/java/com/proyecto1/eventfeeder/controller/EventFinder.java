package com.proyecto1.eventfeeder.controller;
import com.proyecto1.eventfeeder.model.SocialEvent;
import java.util.List;

public interface EventFinder {

    List<SocialEvent> find(String city);
}
