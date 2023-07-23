package utils;

import org.joml.Vector2f;

public class Rect {
    public float x;
    public float y;
    private float width;
    private float height;

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public Vector2f getPosition() {
        return new Vector2f(x, y);
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public boolean collideRect(Rect rect) {
        if (rect.x < (this.x + this.getWidth()) && (rect.x + rect.getWidth()) > this.x) { // if intersecting from the right
            if (rect.y < (this.y + this.getHeight()) && (rect.y + rect.getHeight()) > this.y) {
                return true;
            } else if ((rect.y + rect.getHeight()) > this.y && rect.y < (this.y + this.getHeight())) {
                return true;
            }
            return false;
        } else if ((rect.x + rect.getWidth()) > this.x && rect.x < (this.x + this.getWidth())) {
            if (rect.y < (this.y + this.getHeight()) && (rect.y + rect.getHeight()) > this.y) {
                return true;
            } else if ((rect.y + rect.getHeight()) > this.y && rect.y < (this.y + this.getHeight())) {
                return true;
            }
            return false;
        }
        return false;
    }
}
