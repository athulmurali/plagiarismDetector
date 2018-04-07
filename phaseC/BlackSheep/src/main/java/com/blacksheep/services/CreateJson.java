package com.blacksheep.services;

import java.util.List;

public class CreateJson {

    private String file1;
    private String file2;
    //private List<Object> Matches;
    private double percentage;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    private List<Matches> matches;

    public CreateJson(String file1, String file2, double percentage, List<Matches> matches) {
        this.file1 = file1;
        this.file2 = file2;
        this.percentage = percentage;
        this.matches = matches;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public List<Matches> getMatches() {
        return matches;
    }

    public void setMatches(List<Matches> matches) {
        this.matches = matches;
    }
}
