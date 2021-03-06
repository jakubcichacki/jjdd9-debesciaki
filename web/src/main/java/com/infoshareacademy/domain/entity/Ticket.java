package com.infoshareacademy.domain.entity;

import javax.persistence.*;

@NamedQuery(
        name = "Ticket.findByEventId",
        query = "SELECT t FROM Ticket t WHERE t.event.id=:eventId"
)
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "start_ticket")
    private Integer startTicket;

    @Column(name = "end_ticket")
    private Integer endTicket;

    @Column(name = "number_of_tickets")
    private Integer numberOfTickets;

    @OneToOne(mappedBy = "ticket")
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStartTicket() {
        return startTicket;
    }

    public void setStartTicket(Integer startTicket) {
        this.startTicket = startTicket;
    }

    public Integer getEndTicket() {
        return endTicket;
    }

    public void setEndTicket(Integer endTicket) {
        this.endTicket = endTicket;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
