package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Rob on 4/26/2015.
 */
public class SoundToggleButton extends TextButton {
    private GenericMatchingGame game;

    public SoundToggleButton(GenericMatchingGame g) {
        super("Sound Off", new TextButton.TextButtonStyle(
                new NinePatchDrawable(g.skin.getPatch("button")),
                new NinePatchDrawable(g.skin.getPatch("buttonDown")),
                new NinePatchDrawable(g.skin.getPatch("buttonChecked")),
                g.skin.getFont("pixely64")
        ));

        game = g;

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (SoundToggleButton.this.isChecked()) {
                    game.data.volume = 1.0f;

                    game.tapSound.play(game.data.volume);
                } else {
                    game.data.volume = 0.0f;
                }

                game.data.save();
            }
        });
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (checked) {
            SoundToggleButton.this.setText("Sound On");
        } else {
            SoundToggleButton.this.setText("Sound Off");
        }
    }
}
