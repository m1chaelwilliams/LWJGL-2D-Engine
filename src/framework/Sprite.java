package framework;

import graphics.Surface;
import utils.Rect;

public class Sprite {
    private Surface surface;
    private Rect rect;

    public Sprite(Surface surface, Rect rect) {
        this.surface = surface;
        this.rect = rect;
    }
    public Rect getRect() {
        return rect;
    }
    public Surface getSurface() {
        return surface;
    }
    public void update() {

    }
}
