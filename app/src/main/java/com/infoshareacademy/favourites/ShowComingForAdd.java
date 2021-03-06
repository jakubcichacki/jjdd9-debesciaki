package com.infoshareacademy.favourites;

import com.infoshareacademy.display.ConsoleColor;
import com.infoshareacademy.display.DisplayEvents;
import com.infoshareacademy.display.EventPrinter;
import com.infoshareacademy.parser.Event;
import com.infoshareacademy.repository.EventRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.infoshareacademy.display.CMDCleaner.cleanConsole;

public class ShowComingForAdd extends DisplayEvents {
    private static final Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");
    private static final String DECISION_REQUEST = "\nWpisz numer wydarzenia, które chcesz dodać do ulubionych lub jedną z opcji menu: ";
    private static final String ASK_FOR_PAGE_COUNT = "Ile wydarzeń chcesz zobaczyć na jednej stronie? ";
    private Integer qty;
    private Integer elemPerPage;
    private boolean firstStart;
    private List<Event> eventList;
    private List<Event> eventListForSinglePage = new ArrayList<>();


    @Override
    public void displayComingEvents() {
        resetList();
        Optional<Integer> compQty;
        Optional<Integer> pageMaxElements;
        firstStart = true;
        do {
            if (!firstStart) {
                cleanConsole();
                STDOUT.info("Podano zerowe lub ujemne wartości parametrów, proszę wprowadzić je ponownie.\n");
            }
            firstStart = false;
            compQty = inputInteger("Ile nadchodzących wydarzeń chcesz zobaczyć łącznie? ");
            pageMaxElements = inputInteger(ASK_FOR_PAGE_COUNT);
            qty = compQty.get();
            elemPerPage = pageMaxElements.get();
        } while (qty <= 0 || elemPerPage <= 0);
        this.eventList = selectedListOfComingEvents(qty);
        displayPages(qty, elemPerPage, this.eventList);
    }


    public void displayPages(Integer qty, Integer elemPerPage, List<Event> eventList) {
        Optional<Integer> decision;

        double pageCountd = Math.ceil((double) qty / elemPerPage);
        Integer pageCount = (int) pageCountd;
        int limU = 0;
        int limD = elemPerPage;
        int actual = 1;

        do {
            cleanConsole();
            STDOUT.info("{}WYBIERZ ULUBIONE WYDARZENIE{}\n", ConsoleColor.RED_BOLD, ConsoleColor.RESET);
            for (int i = limU, j = 1; i < limD; i++, j++) {
                if (i < eventList.size()) {
                    Event e = eventList.get(i);
                    STDOUT.info("Numer wydarzenia: {}{}{}\n", ConsoleColor.BLUE_UNDERLINED, e.getId(), ConsoleColor.RESET);
                    consolePrintSingleEventScheme(e);
                    eventListForSinglePage.add(e);
                }
            }
            decision = pageNavigatorDisplay(pageCount, actual);
            int dec = 0;
            if (decision.isPresent()) {
                dec = decision.get();
            }
            if (actual > 1 && dec == 1) {
                actual--;
                limU -= elemPerPage;
                limD -= elemPerPage;
            } else if (actual < pageCount && dec == 2) {
                actual++;
                limU += elemPerPage;
                limD += elemPerPage;
            } else if (dec == 0 || dec > 2) {
                break;
            }
        } while (decision.get() != 0);
    }

    @Override
    public Optional<Integer> pageNavigatorDisplay(int pageCount, int actual) {
        if (actual == 1 && pageCount > 1) {
            return inputInteger("2 - Następna\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + DECISION_REQUEST);
        }
        if (actual == pageCount && actual != 1) {
            return inputInteger("1 - Poprzednia strona\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + DECISION_REQUEST);
        }
        if (actual > 1 && actual < pageCount) {
            return inputInteger("2 - Następna strona\n1 - Poprzednia strona\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + DECISION_REQUEST);
        }
        if (actual == 1 && pageCount == 1) {
            return inputInteger("0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + DECISION_REQUEST);
        }
        STDOUT.info("ERROR ERROR\n");
        return Optional.ofNullable(0);
    }


    @Override
    public void consolePrintSingleEventScheme(Event e) {
        EventPrinter eventPrinter = new EventPrinter(ConsoleColor.BLUE_BACKGROUND, ConsoleColor.RED_BACKGROUND);
        eventPrinter.printName(e);
        eventPrinter.printOrganizer(e);
        eventPrinter.printStartDate(e);
        eventPrinter.printEndDate(e);
        STDOUT.info("\n");
    }

    public Optional<Integer> inputInteger(String subject) {
        Integer quantity = null;
        Optional<Integer> opt;
        String in;
        do {
            STDOUT.info("{}", subject);
            Scanner scanner = new Scanner(System.in);
            in = scanner.nextLine();

            if (NumberUtils.isDigits(in)) {
                quantity = Integer.parseInt(in);
            }

            opt = Optional.ofNullable(quantity);

            if (!NumberUtils.isDigits(in)) {
                STDOUT.info("Źle wprowadzone dane, spróbuj ponownie!\n");
            }
        } while (opt.isEmpty());

        if (quantity > 1000) {

            while (!isPresent(quantity)) {
                STDOUT.info("Nie ma takiego numeru wydarzenia wśród ulubionych - wybierz ponownie: ");
                Scanner scanner = new Scanner(System.in);
                String newIn = scanner.nextLine();
                quantity = Integer.parseInt(newIn);
            }

            if (!opt.isEmpty()) {
                for (int i = 0; i < EventRepository.getAllEventsList().size(); i++) {
                    if (quantity == EventRepository.getAllEventsList().get(i).getId()) {
                        new AddFavourites().addFavourite(EventRepository.getAllEventsList().get(i));
                    }
                }
            }
        }

        return opt;
    }

    public boolean isPresent(Integer quantity) {
        for (int i = 0; i < eventListForSinglePage.size(); i++) {
            if (quantity == eventListForSinglePage.get(i).getId()) {
                return true;
            }
        }
        return false;
    }
}
