package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GenericMatchingGame extends Game {
    public SpriteBatch batch;
    public ModelBatch modelBatch;

    public MainMenuScreen mainMenuScreen;
    public MatchScreen matchScreen;
    public ScoreScreen scoreScreen;
    public ScoresScreen scoresScreen;
    public SettingsScreen settingsScreen;
    public DifficultyScreen difficultyScreen;

    public TextureAtlas atlas;
    public Skin skin;

    public Sound matchSound;
    public Sound noMatchSound;
    public Sound winSound;
    public Sound tapSound;

    public Data data;

    public void create() {
        batch = new SpriteBatch();
        modelBatch = new ModelBatch();

        atlas = new TextureAtlas(Gdx.files.internal("textures-packed/pack.atlas"));
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);

        matchSound = Gdx.audio.newSound(Gdx.files.internal("sound/good2_lv.mp3"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("sound/win2_lv.mp3"));
        noMatchSound = Gdx.audio.newSound(Gdx.files.internal("sound/bad2_lv.mp3"));
        tapSound = Gdx.audio.newSound(Gdx.files.internal("sound/ok2_lv.mp3"));

        data = new Data();
        data.load();

        mainMenuScreen = new MainMenuScreen(this);
        matchScreen = new MatchScreen(this);
        scoreScreen = new ScoreScreen(this);
        scoresScreen = new ScoresScreen(this);
        settingsScreen = new SettingsScreen(this);
        difficultyScreen = new DifficultyScreen(this);

        setScreen(mainMenuScreen);

        Gdx.input.setCatchBackKey(true);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();

        matchSound.dispose();
        noMatchSound.dispose();
        tapSound.dispose();
        winSound.dispose();

        mainMenuScreen.dispose();
        matchScreen.dispose();
        scoreScreen.dispose();
        scoresScreen.dispose();
        settingsScreen.dispose();
        difficultyScreen.dispose();
    }
}
