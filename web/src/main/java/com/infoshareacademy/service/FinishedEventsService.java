package com.infoshareacademy.service;

import com.infoshareacademy.domain.entity.Event;
import com.infoshareacademy.domain.entity.User;
import com.infoshareacademy.repository.EventDao;
import com.infoshareacademy.service.favourite.FavouriteRestService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class FinishedEventsService {

    private static final Logger STDLOG = Logger.getLogger(FinishedEventsService.class.getName());

    @Inject
    EventDao eventDao;

    @Inject
    FavouriteRestService favouriteRestService;

    @Inject
    MailService mailService;

    public void finishedManager() {
        List<Event> events = eventDao.findFinished();
        for (Event e : events) {
            Set<User> users = e.getUsers();
            for (User u : users) {
                favouriteRestService.removeFavourite(u.getMail(), e.getId());
            }
            mailService.sendMailOnFinished(e);
            STDLOG.info("Mails sent to all users.");
        }
    }
}