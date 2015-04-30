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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Rob on 4/25/2015.
 */
public class ScoresScreen extends InputAdapter implements Screen {
    public enum Tabs {EASY, MED, HARD}
    public Tabs tab = Tabs.EASY;
    public int lastIndex = -1;

    private GenericMatchingGame game;
    private OrthographicCamera cam;
    private Sprite bgSprite;
    private Table table;
    private Table easyTable;
    private Table medTable;
    private Table hardTable;
    private EasyTabButton easyTabButton;
    private MedTabButton medTabButton;
    private HardTabButton hardTabButton;
    private PlayAgainButton playAgainButton;
    private Cell scoresCell;
    private InputMultiplexer inputMultiplexer;
    private Stage stage;

    public ScoresScreen(GenericMatchingGame g) {
        game = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(w, h);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        bgSprite = new Sprite(game.atlas.findRegion("bg"));
        bgSprite.setSize(w, h);

        Label title = new Label("High Scores", game.skin, "title");

        easyTabButton = new EasyTabButton(game);
        medTabButton = new MedTabButton(game);
        hardTabButton = new HardTabButton(game);

        playAgainButton = new PlayAgainButton(game);

        table = new Table();
        table.setFillParent(true);

        table.add(title).colspan(3).pad(80);
        table.row();
        table.add(easyTabButton).padBottom(40);
        table.add(medTabButton).padBottom(40);
        table.add(hardTabButton).padBottom(40);
        table.row();
        scoresCell = table.add().colspan(3);
        table.row();
        table.add(playAgainButton).colspan(3).expandY();

        initTables();

        switch (game.data.lastDifficulty) {
            case EASY:
                switchTab(Tabs.EASY);
                break;
            case MED:
                switchTab(Tabs.MED);
                break;
            case HARD:
                switchTab(Tabs.HARD);
                break;
        }

        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer(stage, this);

        stage.addActor(table);
    }

    public void initTables() {
        easyTable = new Table();
        medTable = new Table();
        hardTable = new Table();

        for (Tabs tab : Tabs.values()) {
            Table table;
            ScoresList scores;
            MatchScreen.Difficulty difficulty;

            switch (tab) {
                default:
                case EASY:
                    table = easyTable;
                    difficulty = MatchScreen.Difficulty.EASY;
                    scores = game.data.getScoresList(difficulty);
                    break;
                case MED:
                    table = medTable;
                    difficulty = MatchScreen.Difficulty.MED;
                    scores = game.data.getScoresList(difficulty);
                    break;
                case HARD:
                    table = hardTable;
                    difficulty = MatchScreen.Difficulty.HARD;
                    scores = game.data.getScoresList(difficulty);
                    break;
            }

            for (int i = 0; i < 10; i++) {
                String style = "default";
                if (difficulty == game.data.lastDifficulty && i == lastIndex) {
                    style = "newScore";
                }

                table.add(new Label(MiscUtil.ordinal(i + 1), game.skin, style)).padRight(40);

                if (i < scores.size()) {
                    ScoreEntry s = scores.get(i);

                    table.add(new Label(s.initials, game.skin, style));
                    table.add(new Label(String.format("%.2f", s.score), game.skin, style)).padLeft(40);
                } else {
                    table.add(new Label("---", game.skin, style));
                    table.add(new Label("??.??", game.skin, style)).padLeft(40);
                }

                table.row();
            }
        }
    }

    public void switchTab(MatchScreen.Difficulty d) {
        switch (d) {
            default:
            case EASY:
                switchTab(Tabs.EASY);
                break;
            case MED:
                switchTab(Tabs.MED);
                break;
            case HARD:
                switchTab(Tabs.HARD);
                break;
        }
    }

    public void switchTab(Tabs t) {
        tab = t;

        scoresCell.clearActor();

        easyTabButton.setChecked(false);
        medTabButton.setChecked(false);
        hardTabButton.setChecked(false);

        switch (t) {
            case EASY:
                scoresCell.setActor(easyTable);
                easyTabButton.setChecked(true);
                break;
            case MED:
                scoresCell.setActor(medTable);
                medTabButton.setChecked(true);
                break;
            case HARD:
                scoresCell.setActor(hardTable);
                hardTabButton.setChecked(true);
                break;
        }

        table.layout();
    }

    public void setShowPlayAgainButton(boolean show) {
        playAgainButton.setVisible(show);
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
        Gdx.app.log("DEBUG", "scoreScreen touchDown");
        return false;
    }
}
