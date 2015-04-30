package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.StringWriter;

/**
 * Created by Rob on 4/26/2015.
 */
public class Data {
    public float volume;
    public String lastInitials;
    public ScoresList trivialScores;
    public ScoresList easyScores;
    public ScoresList medScores;
    public ScoresList hardScores;
    public MatchScreen.Difficulty lastDifficulty;

    public Data() {
        trivialScores = new ScoresList();
        easyScores = new ScoresList();
        medScores = new ScoresList();
        hardScores = new ScoresList();

        lastInitials = "";
        lastDifficulty = MatchScreen.Difficulty.EASY;

        volume = 1f;
    }

    public ScoresList getScoresList(MatchScreen.Difficulty d) {
        switch (d) {
            case TRIVIAL:
                return trivialScores;
            case EASY:
                return easyScores;
            case MED:
                return medScores;
            case HARD:
                return hardScores;
        }

        return trivialScores;
    }

    public void clearScores() {
        trivialScores = new ScoresList();
        easyScores = new ScoresList();
        medScores = new ScoresList();
        hardScores = new ScoresList();
    }

    public boolean load() {
        try {
            FileHandle handle = Gdx.files.local("data.json");
            if (!handle.exists()) {
                return false;
            }

            Json json = new Json();
            Data data = json.fromJson(Data.class, handle.reader());

            this.trivialScores = data.trivialScores;
            this.easyScores = data.easyScores;
            this.medScores = data.medScores;
            this.hardScores = data.hardScores;
            this.lastInitials = data.lastInitials;
            this.lastDifficulty = data.lastDifficulty;
            this.volume = data.volume;
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void save() {
        FileHandle handle = Gdx.files.local("data.json");

        Json json = new Json();
        json.toJson(this, handle);
    }
}
