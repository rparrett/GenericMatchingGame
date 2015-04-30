package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
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
 * Created by Rob on 4/24/2015.
 */
public class MainMenuScreen extends InputAdapter implements Screen {
    private GenericMatchingGame game;
    private OrthographicCamera cam;

    private Sprite bgSprite;

    private Stage stage;
    private InputMultiplexer inputMultiplexer;

    public MainMenuScreen(GenericMatchingGame g) {
        game = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(w, h);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        bgSprite = new Sprite(game.atlas.findRegion("bg"));
        bgSprite.setSize(w, h);

        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer(stage, this);

        Label logo = new Label("Generic\nMatching\nGame", game.skin, "title");
        logo.setAlignment(1);

        ScoresButton scores = new ScoresButton(game);
        PlayButton play = new PlayButton(game);
        SettingsButton settings = new SettingsButton(game);

        Table table = new Table();
        table.setFillParent(true);

        table.add(logo).padTop(80);
        table.row();
        table.add(play).expand();
        table.row();
        table.add(scores).expand();
        table.row();
        table.add(settings).expand();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        bgSprite.draw(game.batch);
        game.batch.end();

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("DEBUG", "MainMenuScreen touchDown");
        return true;
    }
}
