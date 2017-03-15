package com.brandonhogan.liftscout.views.workout.tracker;

import com.brandonhogan.liftscout.core.model.Rep;

public class TrackerListModel {

    private int id;
    private int rowNum;
    private int count;
    private double weight;
    private boolean isRecord = false;

    private String countMetric;
    private String weightMetric;

    public TrackerListModel(int rowNum, Rep rep, String countMetric, String weightMetric, boolean isRecord) {
        this.rowNum = rowNum;
        this.id = rep.getId();
        this.count = rep.getCount();
        this.weight = rep.getWeight();
        this.countMetric = countMetric;
        this.weightMetric = weightMetric;
        this.isRecord = isRecord;
    }

    public TrackerListModel() {

    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCountMetric() {
        return countMetric;
    }

    public void setCountMetric(String countMetric) {
        this.countMetric = countMetric;
    }

    public String getWeightMetric() {
        return weightMetric;
    }

    public void setWeightMetric(String weightMetric) {
        this.weightMetric = weightMetric;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }
}
