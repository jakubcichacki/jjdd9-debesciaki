package com.infoshareacademy.service;

import com.infoshareacademy.domain.entity.Event;
import com.infoshareacademy.domain.entity.Reservation;
import com.infoshareacademy.domain.entity.User;
import com.infoshareacademy.mail.MailService;
import com.infoshareacademy.mail.ReservationDeletedMail;
import com.infoshareacademy.mail.ReservationTokenMail;
import com.infoshareacademy.repository.EventDao;
import com.infoshareacademy.repository.ReservationDao;
import com.infoshareacademy.repository.UserDao;
import com.infoshareacademy.service.stat.TicketStatService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
public class ReservationService {

    private static final Logger STDLOG = LoggerFactory.getLogger(ReservationService.class.getName());

    @Inject
    private ReservationDao reservationDao;

    @Inject
    TicketStatService ticketStatService;

    @Inject
    private EventDao eventDao;

    @Inject
    private UserDao userDao;

    @Inject
    private MailService mailService;


    public String requestReservation(Long eventId, String mail, Boolean isFull) {

        Optional<Event> eventOptional = eventDao.findById(eventId);
        Optional<User> userOptional = userDao.findByEmail(mail);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Reservation reservation = new Reservation();

            Event event = eventOptional.get();
            Optional<Long> ticketAmount = Optional.ofNullable(event.getTicketAmount());
            if (ticketAmount.isPresent()) {
                if (ticketAmount.get() > 0) {
                    event.setTicketAmount(ticketAmount.get() - 1L);
                    eventDao.update(event);
                } else {
                    return "Brak biletów na wydarzenie";
                }
            } else {
                return "Brak biletów na wydarzenie";
            }

            ticketStatService.updateSingleTicketStat(eventId, isFull);


            reservation.setEvent(event);
            reservation.setFull(isFull);
            reservation.setUser(userOptional.get());
            reservation.setExpirationDate(LocalDateTime.now().plusMinutes(3L));
            reservation.setConfirmed(Boolean.FALSE);

            String token = RandomStringUtils.randomAlphabetic(10);
            reservation.setToken(token);

            reservationDao.save(reservation);

            mailService.sendEmail(new ReservationTokenMail(event.getName(), "http://debesciaki.jjdd9.is-academy.pl/api/request-reservation/consume/" + token), mail);
            return "Udało się wysłać zapytanie o rezerwację!";
        } else {
            STDLOG.error("Failed at finding event that is supposed to be reserved, probabble wrong ID passed or problem with finding user in database");
            return "Błąd przy wysyłaniu zapytania o rezerwację!";
        }
    }

    public String consumeToken(String token) {
        Optional<Reservation> optionalReservation = reservationDao.findByToken(token);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            if (LocalDateTime.now().isAfter(reservation.getExpirationDate())) {
                delete(reservation, "Link jest nieaktywny, minął czas na aktywację.");
                return "Link%20jest%20przeterminowany,%20możesz%20spróbować%20dokonać%20rezerwacji%20ponownie%20:)";
            }
            if (reservation.getConfirmed() == true) {
                return "Dana%20rezerwacja%20jest%20już%20potwierdzona,%20link%20nieaktywny";
            }
            reservation.setConfirmed(Boolean.TRUE);
            reservationDao.update(reservation);

            return "Dokonano%20potwierdzenia%20rezerwacji";
        } else {
            return "Link%20potwierdzający%20jest%20niepoprawny";
        }
    }

    public String deleteReservationManualFromList(String token) {
        Optional<Reservation> optionalReservation = reservationDao.findByToken(token);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            delete(reservation, "Usunięte z listy przez użytkownika własnoręcznie.");
            STDLOG.info("Reservation deletion end with success");
            return "Usuwanie rezerwacji zakończone powodzeniem.";
        } else {
            STDLOG.error("Failed at deleting reservation");
            return "Usunięcie rezerwacji zakończone niepowodzeniem, nie znaleziono rezerwacji w bazie. Najprawdopodobniej została już usunięta.";
        }
    }


    public void delete(Reservation reservation, String reason) {
        Optional<Event> eventOptional = eventDao.findById(reservation.getEvent().getId());
        Event event = eventOptional.get();
        String mail = reservation.getUser().getMail();
        event.setTicketAmount(event.getTicketAmount() + 1L);
        eventDao.update(event);
        reservationDao.delete(reservation);
        mailService.sendEmail(new ReservationDeletedMail(event.getName(), reason), mail);
    }

    public void deleteExpiredScheduler() {
        List<Reservation> expiredList = reservationDao.findExpired();
        for (Reservation reservation : expiredList) {
            delete(reservation, "Minął czas na aktywacje, automatyczne usunięcie wstępnej rezerwacji.");
        }
    }
}
