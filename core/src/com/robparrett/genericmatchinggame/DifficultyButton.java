package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Rob on 4/26/2015.
 */
public class DifficultyButton extends TextButton {
    private GenericMatchingGame game;
    private MatchScreen.Difficulty difficulty;

    public DifficultyButton(GenericMatchingGame g, MatchScreen.Difficulty d) {
        super("Easy", new TextButtonStyle(
                new NinePatchDrawable(g.skin.getPatch("button")),
                new NinePatchDrawable(g.skin.getPatch("buttonDown")),
                new NinePatchDrawable(g.skin.getPatch("button")),
                g.skin.getFont("pixely64")
        ));

        game = g;
        difficulty = d;

        switch (difficulty) {
            case TRIVIAL:
                setText("Trivial");
                break;
            case EASY:
                setText("Easy");
                break;
            case MED:
                setText("Medium");
                break;
            case HARD:
                setText("Hard");
                break;
        }

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("DEBUG", "ScoresButton touchDown");
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("DEBUG", "ScoresButton touchDown");

                game.matchScreen.setDifficulty(difficulty);
                game.setScreen(game.matchScreen);

                game.tapSound.play(game.data.volume);
                game.data.lastDifficulty = difficulty;
            }
        });
    }
}
