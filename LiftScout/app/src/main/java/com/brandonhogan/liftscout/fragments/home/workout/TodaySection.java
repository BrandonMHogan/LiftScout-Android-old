package com.brandonhogan.liftscout.fragments.home.workout;

public class TodaySection {

    private final int id;
    private final String name;
    private final double volume;

    public boolean isExpanded;

    public TodaySection(int id, String name, double volume) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        isExpanded = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getVolume() {
        return volume;
    }
}