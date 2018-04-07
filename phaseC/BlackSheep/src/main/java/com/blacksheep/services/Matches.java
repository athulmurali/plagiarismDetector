package com.blacksheep.services;

import java.util.List;

public class Matches {

    private String type;
    private List<Integer> file1MatchLines;
    private List<Integer> file2MatchLines;


    public Matches(String type, List<Integer> file1MatchLines, List<Integer> file2MatchLines) {
        this.type = type;
        this.file1MatchLines = file1MatchLines;
        this.file2MatchLines = file2MatchLines;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getFile1MatchLines() {
        return file1MatchLines;
    }

    public void setFile1MatchLines(List<Integer> file1MatchLines) {
        this.file1MatchLines = file1MatchLines;
    }

    public List<Integer> getFile2MatchLines() {
        return file2MatchLines;
    }

    public void setFile2MatchLines(List<Integer> file2MatchLines) {
        this.file2MatchLines = file2MatchLines;
    }
}
