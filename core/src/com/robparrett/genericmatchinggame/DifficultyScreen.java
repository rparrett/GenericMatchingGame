package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Rob on 4/25/2015.
 */
public class DifficultyScreen extends InputAdapter implements Screen {
    private GenericMatchingGame game;
    private OrthographicCamera cam;

    private Sprite bgSprite;

    private InputMultiplexer inputMultiplexer;
    private Stage stage;

    public DifficultyScreen(GenericMatchingGame g) {
        game = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(w, h);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        bgSprite = new Sprite(game.atlas.findRegion("bg"));
        bgSprite.setSize(w, h);

        Label title = new Label("Choose\nDifficulty", game.skin, "title");
        title.setAlignment(1);

        DifficultyButton easyButton = new DifficultyButton(game, MatchScreen.Difficulty.EASY);
        DifficultyButton medButton = new DifficultyButton(game, MatchScreen.Difficulty.MED);
        DifficultyButton hardButton = new DifficultyButton(game, MatchScreen.Difficulty.HARD);

        Table table = new Table();
        table.setFillParent(true);
        table.add(title).padTop(80);
        table.row();
        table.add().expand();
        table.row();
        table.add(easyButton).padBottom(40);
        table.row();
        table.add(medButton).padBottom(40);
        table.row();
        table.add(hardButton);
        table.row();
        table.add().expand();

        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer(stage, this);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // bg

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        bgSprite.draw(game.batch);
        game.batch.end();

        // stage

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            game.setScreen(game.mainMenuScreen);
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("DEBUG", "settingsScreen touchDown");
        return false;
    }
}
