package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Rob on 4/26/2015.
 */
public class ClearScoresButton extends TextButton {
    private GenericMatchingGame game;

    public ClearScoresButton(GenericMatchingGame g) {
        super("Clear Scores", new TextButtonStyle(
                new NinePatchDrawable(g.skin.getPatch("button")),
                new NinePatchDrawable(g.skin.getPatch("buttonDown")),
                new NinePatchDrawable(g.skin.getPatch("button")),
                g.skin.getFont("pixely64")
        ));

        game = g;

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.tapSound.play(game.data.volume);

                game.data.clearScores();
                game.data.save();

                game.scoresScreen.initTables();
                game.scoresScreen.switchTab(game.data.lastDifficulty);
            }
        });
    }
}
