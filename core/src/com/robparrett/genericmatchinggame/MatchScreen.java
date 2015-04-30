package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rob on 4/24/2015.
 */
public class MatchScreen extends InputAdapter implements Screen {
    public State state = State.IDLE;
    public Difficulty difficulty = Difficulty.EASY;

    private static final float delayAfterBadFlip = 1.0f;
    private static final float delayAfterFlipBack = 0.25f;
    private static final float delayAfterGoodFlip = 0.25f;
    private static final float delayAfterWinning = 1.0f;
    private static final float cardSpacingX = 30;
    private static final float cardSpacingY = 30;

    private GenericMatchingGame game;
    private OrthographicCamera cam;
    private PerspectiveCamera pCam;
    private Array<Card> cards;
    private Array<Card> selectedCards;
    private Stage stage;
    private Sprite bgSprite;
    private float delayI = 0;
    private float timeElapsed = 0;
    private int rows = 4;
    private int cols = 4;
    private int totalCards = rows * cols;
    private int cardsRemaining = totalCards;

    public MatchScreen(GenericMatchingGame g) {
        game = g;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera(w, h);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        bgSprite = new Sprite(game.atlas.findRegion("bg"));
        bgSprite.setSize(w, h);

        stage = new Stage();

        setDifficulty(game.data.lastDifficulty);
    }

    public void setDifficulty(Difficulty d) {
        difficulty = d;

        switch (d) {
            case TRIVIAL:
                cols = 2;
                rows = 1;
                break;
            case EASY:
                cols = 3;
                rows = 4;
                break;
            case MED:
                cols = 4;
                rows = 4;
                break;
            case HARD:
                cols = 4;
                rows = 5;
                break;
        }

        totalCards = rows * cols;
        cardsRemaining = totalCards;

        initCards();
        initPerspectiveCamera();

        state = State.IDLE;
        timeElapsed = 0;
    }

    public void update(float delta) {
        timeElapsed += delta;

        if (state == State.FLIPPED_TWO &&
                selectedCards.get(0).state == Card.State.FLIPPED &&
                selectedCards.get(1).state == Card.State.FLIPPED) {
            delayI = delayI + delta;

            if (selectedCards.get(0).value == selectedCards.get(1).value) {
                if (Math.abs(delayI - delta) < 0.00001) {
                    game.matchSound.play(game.data.volume);
                }

                if (delayI > delayAfterGoodFlip) {
                    selectedCards.get(0).flyAway();
                    selectedCards.get(1).flyAway();

                    selectedCards.clear();

                    cardsRemaining = cardsRemaining - 2;

                    if (cardsRemaining <= 0) {
                        game.scoreScreen.setScore(timeElapsed);
                        game.winSound.play(game.data.volume);

                        state = State.CELEBRATING;
                    } else {
                        state = State.IDLE;
                    }

                    delayI = 0;
                }
            } else {
                if (Math.abs(delayI - delta) < 0.00001) {
                    game.noMatchSound.play(game.data.volume);
                }

                if (delayI > delayAfterBadFlip) {
                    selectedCards.get(0).flipBack();
                    selectedCards.get(1).flipBack();

                    state = State.FLIPPING_BACK;

                    delayI = 0;
                }
            }
        } else if (state == State.FLIPPING_BACK) {
            delayI = delayI + delta;

            if (delayI > delayAfterFlipBack) {
                selectedCards.clear();

                state = State.IDLE;

                delayI = 0;
            }
        } else if (state == State.CELEBRATING) {
            delayI = delayI + delta;
            if (delayI > delayAfterWinning) {
                selectedCards.clear();

                game.setScreen(game.scoreScreen);

                delayI = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // bg

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        bgSprite.draw(game.batch);
        game.batch.end();

        // 3d cards

        pCam.update();

        game.modelBatch.begin(pCam);
        for (Card card : cards) {
            card.update(delta);
            card.render(game.modelBatch);
        }
        game.modelBatch.end();

        // 2d ui

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
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
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            game.setScreen(game.mainMenuScreen);
        }

        return false;
    }

    @Override
    public void dispose() {
        for (Card card : cards) {
            card.dispose();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("DEBUG", "MatchScreen touchDown");

        Card card = getCard(screenX, screenY);
        if (card == null) {
            return false;
        }

        if (state == State.IDLE) {
            boolean flipped = card.onTouchDown();
            if (flipped) {
                selectedCards.add(card);
                state = State.FLIPPED_ONE;
            }
        } else if (state == State.FLIPPED_ONE) {
            boolean flipped = card.onTouchDown();
            if (flipped) {
                selectedCards.add(card);
                state = State.FLIPPED_TWO;
            }
        }

        return true;
    }

    /**
     * Get a card at a some particular screen coordinates
     *
     * @param screenX Screen X coordinate
     * @param screenY Screen Y coordinate
     * @return Card if one was found, or null.
     */
    public Card getCard(int screenX, int screenY) {
        Ray ray = pCam.getPickRay(screenX, screenY);

        for (Card card : cards) {
            Vector3 pos = card.getPosition();

            float w = card.getWidth();
            float h = card.getHeight();
            float x = pos.x - w / 2f;
            float y = pos.y - h / 2f;
            float z = 0;

            Vector3 intersection = new Vector3();

            if (Intersector.intersectRayBounds(ray, new BoundingBox(new Vector3(x, y, z), new Vector3(x + w, y + h, z)), intersection)) {
                return card;
            }
        }

        return null;
    }

    public void initCards() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // construct a deck containing one of each possible card
        // shuffle it
        // deal the unique cards needed
        // duplicate the dealt cards
        // shuffle those

        int availableCards = Card.maxTextureIndex - Card.minTextureIndex + 1;

        int[] values = new int[Math.max(totalCards, availableCards)];

        for (int i = 0; i < availableCards; i++) {
            values[i] = Card.minTextureIndex + i;
        }

        MiscUtil.shuffleArray(values, availableCards);

        int totalUniqueCards = totalCards / 2;

        System.arraycopy(values, 0, values, totalUniqueCards, totalUniqueCards);

        MiscUtil.shuffleArray(values, totalCards);

        // create the cards and arrange them in a grid

        cards = new Array<Card>();
        selectedCards = new Array<Card>();

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                Card card = new Card(game, values[c * rows + r]);

                float cardW = card.getWidth();
                float cardH = card.getHeight();

                float totalFieldW = cardSpacingX * (cols - 1) + cardW * (cols - 1);
                float totalFieldH = cardSpacingY * (rows - 1) + cardH * (rows - 1);

                float x = w / 2 - totalFieldW / 2 + c * (cardSpacingX + cardW);
                float y = h / 2 - totalFieldH / 2 + r * (cardSpacingY + cardH);

                card.setPosition(
                        x,
                        y,
                        0
                );

                cards.add(card);
            }
        }
    }

    public void initPerspectiveCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float vFov = 67;

        pCam = new PerspectiveCamera(vFov, w, h);

        float hFov = MathUtils.degRad * vFov * w / h;

        float cardWidth = cards.get(0).getWidth();
        float totalFieldWidth = cardWidth * cols + cardSpacingX * (cols + 1);

        float d = (float) (totalFieldWidth / 2f / Math.tan(hFov / 2f));

        pCam.position.set(w / 2, h / 2, d);
        pCam.lookAt(w / 2, h / 2, 0);
        pCam.near = 1f;
        pCam.far = 3000f;
        pCam.update();
    }

    public enum State {IDLE, FLIPPED_ONE, FLIPPED_TWO, FLIPPING_BACK, CELEBRATING}

    public enum Difficulty {TRIVIAL, EASY, MED, HARD}
}
