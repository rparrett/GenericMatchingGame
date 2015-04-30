package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Rob on 4/25/2015.
 */
public class ScoreScreen extends InputAdapter implements Screen {
    private GenericMatchingGame game;
    private OrthographicCamera cam;

    private Sprite bgSprite;

    private Label scoreLabel;
    private TextField textField;

    private InputMultiplexer inputMultiplexer;
    private Stage stage;

    private float score = 0;

    public ScoreScreen(GenericMatchingGame g) {
        game = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(w, h);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        bgSprite = new Sprite(game.atlas.findRegion("bg"));
        bgSprite.setSize(w, h);

        Label title = new Label("Amazing!", game.skin, "title");
        Label yourTimeWas = new Label("Your time was:", game.skin, "subtitle");
        Label enterInitials = new Label("Enter Initials", game.skin, "subtitle");

        SkipButton skipButton = new SkipButton(game);
        DoneButton doneButton = new DoneButton(game);
        scoreLabel = new Label("", game.skin);

        textField = new TextField(game.data.lastInitials, game.skin);
        textField.setAlignment(1);
        textField.setMaxLength(4);

        Table enterInitialsTable = new Table();
        enterInitialsTable.add(enterInitials).padBottom(40);
        enterInitialsTable.row();
        enterInitialsTable.add(textField).size(514, 119).padBottom(40);
        enterInitialsTable.row();
        enterInitialsTable.add(doneButton);

        Table table = new Table();
        table.setFillParent(true);

        table.add(title).padTop(80).padBottom(40);
        table.row();
        table.add(yourTimeWas).padBottom(40);
        table.row();
        table.add(scoreLabel);
        table.row();
        table.add().expandY();
        table.row();
        table.add(enterInitialsTable);
        table.row();
        table.add().expandY();
        table.row();
        table.add(skipButton).padBottom(40);

        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer(stage, this);

        stage.addActor(table);

        setScore(0);
    }

    public void setScore(float s) {
        score = s;

        scoreLabel.setText(String.format("%.2f seconds", score));
    }

    public void doneEnteringInitials() {
        int lastIndex = game.data.getScoresList(game.matchScreen.difficulty)
                .add(new ScoreEntry(textField.getText(), score));

        game.data.lastInitials = textField.getText();
        game.data.save();

        game.scoresScreen.lastIndex = lastIndex;
        game.scoresScreen.initTables();
        game.scoresScreen.switchTab(game.data.lastDifficulty);
        game.scoresScreen.setShowPlayAgainButton(true);

        game.setScreen(game.scoresScreen);
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("DEBUG", "scoreScreen touchDown");
        return false;
    }
}
