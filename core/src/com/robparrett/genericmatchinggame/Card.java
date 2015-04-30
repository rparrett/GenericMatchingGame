package com.robparrett.genericmatchinggame;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Rob on 4/24/2015.
 */

public class Card {
    public static final int minTextureIndex = 1;
    public static final int maxTextureIndex = 15;

    public State state = State.IDLE;
    public int value;

    private static final float flipSpeed = 180;
    private static final float flipBackSpeed = 360;

    private GenericMatchingGame game;
    private TextureRegion backTex;
    private TextureRegion frontTex;
    private Model model;
    private ModelInstance modelInstance;
    private float flySpeed = MathUtils.random(800f, 1600f);
    private Vector3 flyDirection = new Vector3(MathUtils.random(-0.5f, 0.5f), MathUtils.random(-0.5f, 0.5f), 1.0f);
    private Vector3 flyRotationSpeed = new Vector3(MathUtils.random(400), MathUtils.random(400), MathUtils.random(400));
    private Vector3 pos = new Vector3(0, 0, 0);
    private Vector3 rot = new Vector3(0, 0, 0);

    public Card(GenericMatchingGame g, int v) {
        value = v;
        game = g;

        // "back" refers to the non-face side of the card, which is facing the screen by
        // default.

        backTex = game.atlas.findRegion("card_back");
        frontTex = game.atlas.findRegion("card", value);

        ModelBuilder mb = new ModelBuilder();
        MeshPartBuilder pb;

        float tw = backTex.getRegionWidth() / 2;
        float th = backTex.getRegionHeight() / 2;
        float bz = 0f;
        float fz = -0.1f;

        Material backMat = new Material();
        backMat.set(TextureAttribute.createDiffuse(backTex));
        backMat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

        Material frontMat = new Material();
        frontMat.set(TextureAttribute.createDiffuse(frontTex));
        frontMat.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

        mb.begin();
        pb = mb.part("back", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, backMat);
        pb.rect(-1 * tw, -1 * th, bz, tw, -1 * th, bz, tw, th, bz, -1 * tw, th, bz, 0f, 0f, 1f);
        pb = mb.part("front", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, frontMat);
        pb.rect(tw, th * -1, fz, -1 * tw, -1 * th, fz, tw * -1, th, fz, tw, th, fz, 0f, 0f, -1f);
        model = mb.end();

        modelInstance = new ModelInstance(model);
    }

    public void update(float delta) {
        if (state == State.FLIPPING_OVER) {
            float step = delta * flipSpeed;

            rot.x = rot.x + step;
            if (rot.x > 180) {
                rot.x = 180;
                state = State.FLIPPED;
            }
        } else if (state == State.FLIPPING_BACK) {
            float step = delta * flipBackSpeed;

            rot.x = rot.x + step;
            if (rot.x > 360) {
                rot.x = 0;
                state = State.IDLE;
            }
        } else if (state == State.FLYING_AWAY) {
            pos.x = pos.x + flyDirection.x * flySpeed * delta;
            pos.y = pos.y + flyDirection.y * flySpeed * delta;
            pos.z = pos.z + flyDirection.z * flySpeed * delta;
            rot.x = rot.x + flyRotationSpeed.x * delta;
            rot.y = rot.y + flyRotationSpeed.y * delta;
            rot.z = rot.z + flyRotationSpeed.z * delta;
        }

        modelInstance.transform.set(pos, new Quaternion().setEulerAngles(rot.x, rot.y, rot.z));
    }

    public void setPosition(float x, float y, float z) {
        pos.x = x;
        pos.y = y;
        pos.z = z;
    }

    public Vector3 getPosition() {
        return pos;
    }

    public float getWidth() {
        return backTex.getRegionWidth();
    }

    public float getHeight() {
        return backTex.getRegionHeight();
    }

    public void render(ModelBatch batch) {
        batch.render(modelInstance);
    }

    public boolean onTouchDown() {
        if (state == State.IDLE) {
            state = State.FLIPPING_OVER;

            game.tapSound.play(game.data.volume);
            return true;
        }

        return false;
    }

    public void flyAway() {
        state = State.FLYING_AWAY;
    }

    public void flipBack() {
        state = State.FLIPPING_BACK;
    }

    public void dispose() {
        model.dispose();
    }

    public enum State {IDLE, FLIPPING_OVER, FLIPPING_BACK, FLIPPED, FLYING_AWAY}
}
