package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The class for the LifeCounter object. Inherits GameObject.
 */
public class LifeCounter extends GameObject {
    private TextRenderable textRenderable;

    /**
     * Construct a new LifeCounter instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param textRenderable    The textRenderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public LifeCounter(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
        this.textRenderable = textRenderable;
    }

    /**
     * Setter - sets the LifeCounter text
     * @param string - string repressing how many life left.
     */
    public void setLifeCounterString(String string) {
        this.textRenderable.setString(string);
    }

    /**
     * Setter - sets the lifeCounter's colour
     * @param color colour according to how many lives.
     */
    public void setColor(Color color) {
        textRenderable.setColor(color);
    }
}
