package com.robparrett.genericmatchinggame;

/**
 * Created by Rob on 4/26/2015.
 */
public class ScoreEntry {
    public String initials;
    public float score;

    public ScoreEntry() {
        // no-argument constructor is required for Json deserialization
    }

    public ScoreEntry(String i, float s) {
        initials = i;
        score = s;
    }
}
