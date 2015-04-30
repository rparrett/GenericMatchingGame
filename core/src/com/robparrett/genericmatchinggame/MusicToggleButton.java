package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Rob on 4/26/2015.
 */
public class MusicToggleButton extends TextButton {
    private GenericMatchingGame game;

    public MusicToggleButton(GenericMatchingGame g) {
        super("Music Off", new TextButtonStyle(
                new NinePatchDrawable(g.skin.getPatch("button")),
                new NinePatchDrawable(g.skin.getPatch("buttonDown")),
                new NinePatchDrawable(g.skin.getPatch("buttonChecked")),
                g.skin.getFont("pixely64")
        ));

        // TODO add music to game

        game = g;

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (MusicToggleButton.this.isChecked()) {
                    Gdx.app.log("DEBUG", "Turning music on");
                } else {
                    Gdx.app.log("DEBUG", "Turning music off");
                }

                game.tapSound.play(game.data.volume);

                game.data.save();
            }
        });
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (checked) {
            MusicToggleButton.this.setText("Music On");
        } else {
            MusicToggleButton.this.setText("Music Off");
        }
    }
}
