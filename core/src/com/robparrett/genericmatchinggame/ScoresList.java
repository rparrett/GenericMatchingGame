package com.robparrett.genericmatchinggame;

import java.util.ArrayList;

/**
 * Created by Rob on 4/26/2015.
 */
public class ScoresList {
    public ArrayList<ScoreEntry> scores;
    public int maxLen = 10;

    public ScoresList() {
        scores = new ArrayList<ScoreEntry>();
    }

    /**
     * Add a score onto the list if it's good enough.
     *
     * @param scoreEntry The ScoreEntry to add
     * @return The index of the newly added score
     */
    public int add(ScoreEntry scoreEntry) {
        if (scores.size() == 0) {
            scores.add(scoreEntry);
            return 0;
        }

        for (int i = 0; i < scores.size(); i++) {
            if (scoreEntry.score < scores.get(i).score) {
                scores.add(i, scoreEntry);

                if (scores.size() > maxLen) {
                    scores.remove(maxLen);
                }

                return i;
            }
        }

        if (scores.size() < maxLen) {
            scores.add(scoreEntry);
            return scores.size() - 1;
        }

        return -1;
    }

    public ScoreEntry get(int index) {
        return scores.get(index);
    }

    public int size() {
        return scores.size();
    }
}
