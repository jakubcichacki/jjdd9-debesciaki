package com.infoshareacademy.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infoshareacademy.domain.api.*;

import java.time.LocalDateTime;
import java.util.List;

public class EventView {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String descShort;
    private String descLong;
    private Integer active;
    private String categoryName;
    private UrlJSON urls;
    private PlaceJSON place;
    private TicketJSON tickets;
    private OrganizerJSON organizer;
    private List<AttachmentJSON> attachments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
