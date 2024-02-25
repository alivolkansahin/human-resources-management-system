package org.musketeers.repository.enums;

import java.time.LocalDate;

public enum ETurkishHoliday {
    NEW_YEAR("New Year", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1)),
    NATIONAL_SOVEREIGNTY_AND_CHILDRENS_DAY("National Sovereignty and Children's Day", LocalDate.of(2024, 4, 23), LocalDate.of(2024, 4, 23)),
    LABOR_AND_SOLIDARITY_DAY("Labor and Solidarity Day", LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 1)),
    RAMADAN_BAYRAM("Ramadan Bayram", LocalDate.of(2024, 4, 29), LocalDate.of(2024, 4, 30)),
    VICTORY_DAY("Victory Day", LocalDate.of(2024, 8, 30), LocalDate.of(2024, 8, 30)),
    SACRIFICE_FEAST("Sacrifice Feast", LocalDate.of(2024, 7, 29), LocalDate.of(2024, 8, 1)),
    REPUBLIC_DAY("Republic Day", LocalDate.of(2024, 10, 29), LocalDate.of(2024, 10, 29));

    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;

    ETurkishHoliday(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
