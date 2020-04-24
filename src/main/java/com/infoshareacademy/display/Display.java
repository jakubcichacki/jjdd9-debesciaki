package com.infoshareacademy.display;

import com.infoshareacademy.parser.Event;
import com.infoshareacademy.parser.Place;
import com.infoshareacademy.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Display {
    private final static Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");

    public void displayCurrentEvents() {
        Optional<Integer> compQty = inputInteger("Ile nadchodzących wydarzeń chcesz zobaczyć łącznie? ");
        Optional<Integer> pageMaxElements = inputInteger("Ile wydarzeń chcesz zobaczyć na jednej stronie? ");
        Integer qty = compQty.get();
        Integer elemPerPage = pageMaxElements.get();
        STDOUT.info("Number is: {} \n", qty);
        List<Event> eventList = selectedList(qty);
        displayPages(qty, elemPerPage, eventList);
    }

    public Optional<Integer> inputInteger(String subject) {
        Integer qty = null;
        Optional<Integer> opt = Optional.ofNullable(qty);
        do {
            STDOUT.info("{}", subject);
            try {
                Scanner scanner = new Scanner(System.in);
                opt = Optional.ofNullable(qty = scanner.nextInt());
            } catch (InputMismatchException e) {
                STDOUT.info("Źle wprowadzone dane, spróbuj ponownie! msg: {}\n", e.getMessage());
            }
        } while (opt.isEmpty());
        return opt;
    }

    public Map<Integer, Event> selectedMap(int qty) {
        //Selective filling
        Map<Integer, Event> eventMap = new HashMap<>();
        for (Event e : EventRepository.getAllEvents()) {
            if (eventMap.size() < qty) {
                if (isAfterNow(e.getEndDate())) eventMap.put(e.getId(), e);
            }
        }
        return eventMap;
    }

    public List<Event> selectedList(int qty) {
        //Selective filling
        List<Event> eventList = new ArrayList<>();
        for (Event e : EventRepository.getAllEvents()) {
            if (eventList.size() < qty) {
                if (isAfterNow(e.getEndDate())) eventList.add(e);
            }
        }
        return eventList;
    }

    public boolean isAfterNow(String eventTime) {
        String subEventTime = eventTime.substring(0, 19);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");     //("yyyy-MM-dd'T'HH:mm:ssZ");
        LocalDateTime convEventLTD = LocalDateTime.parse(subEventTime, formatter);
        return convEventLTD.isAfter(LocalDateTime.now());
    }

    public void displayPages(Integer qty, Integer elemPerPage, List<Event> eventList) {
        Optional<Integer> decision = null;
        double pageCountd = Math.ceil((double) qty / elemPerPage);
        Integer pageCount = (int) pageCountd;
        int limU = 0, limD = elemPerPage, actual = 1;
        do {
            cleanTerminal();
            for (int i = limU; i < limD; i++) {
                if (i < eventList.size()) {
                    Event e = eventList.get(i);
                    consoleEventScheme(e);
                }
            }
            decision = inputInteger("0-wyjdź\n1-poprzednia\n2-następna\nStrona nr " + actual + "\nTwój wybór to:");
            int dec = decision.get();
            if (actual > 1 && dec == 1) {
                actual--;
                limU -= elemPerPage;
                limD -= elemPerPage;
            } else if (actual < pageCount && dec == 2) {
                actual++;
                limU += elemPerPage;
                limD += elemPerPage;
            } else if (dec != 1 && dec != 2) {
                break;
            }
        } while (decision.get() == 1 || decision.get() == 2);
    }

    public void consoleEventScheme(Event e) {
        Place p = e.getPlace();
        STDOUT.info("Name: {}{}{}\nPlace: {}{}{} \nEnd date: {}{}{}\n", ConsoleColor.RED_UNDERLINED, e.getName(), ConsoleColor.RESET,
                ConsoleColor.BLUE, p.getName(), ConsoleColor.RESET,
                ConsoleColor.BLUE_BACKGROUND, e.getEndDate(), ConsoleColor.RESET);
    }

    public void cleanTerminal() {
        STDOUT.info("\033\143");
    }
}
