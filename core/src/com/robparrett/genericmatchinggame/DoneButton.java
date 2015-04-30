package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Rob on 4/26/2015.
 */
public class DoneButton extends TextButton {
    private GenericMatchingGame game;

    public DoneButton(GenericMatchingGame g) {
        super("Done", new TextButton.TextButtonStyle(
                new NinePatchDrawable(g.skin.getPatch("button")),
                new NinePatchDrawable(g.skin.getPatch("buttonDown")),
                new NinePatchDrawable(g.skin.getPatch("button")),
                g.skin.getFont("pixely64")
        ));

        game = g;

        addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("DEBUG", "doneButton touchDown");

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("DEBUG", "doneButton touchDown");

                game.tapSound.play(game.data.volume);
                game.scoreScreen.doneEnteringInitials();
            }
        });
    }
}
