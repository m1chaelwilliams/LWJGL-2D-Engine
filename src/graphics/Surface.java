package graphics;

import org.lwjgl.opengl.GL11;

import core.Window;
import utils.Rect;

import org.joml.Vector2f;

public class Surface {
    VAO data;
    float[] rawData;
    Texture texture;
    private Vector2f size;

    // states
    boolean flippedX = false;
    boolean flippedY = false;

    public Surface(String filepath) {
        buildSprite(filepath);
        size = new Vector2f(texture.getWidth(), texture.getHeight());
        data = SpriteData.getData();
        rawData = SpriteData.getRawData();
    }

    public void scale(Vector2f scaleFactor) {
        size.x *= scaleFactor.x;
        size.y *= scaleFactor.y;
    }
    public void scaleBy(float factor) {
        size.x *= factor;
        size.y *= factor;
    }
    public void resize(Vector2f newSize) {
        size = newSize;
    }

    public void buildSprite(String filepath) {
        texture = new Texture(filepath);
    }
    public void draw() {
        data.bind();
        texture.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, rawData.length/4);
    }

    // getters setters
    public float getWidth() {
        return size.x;
    }
    public float getHeight() {
        return size.y;
    }
    public Vector2f getSize() {
        return size;
    }
    public void setSize(Vector2f newSize) {
        size = newSize;
    }
    public void uploadData(ShaderProgram shaderProgram) {
        shaderProgram.putDataIntoUniformVec2(size, "sprite_size");
    }

    // utils
    public Rect getRect() {
        return new Rect(0, 0, getWidth(), getHeight());
    }
    public Rect getRect(Vector2f position) {
        return new Rect(position.x, position.y, getWidth(), getHeight());
    }
    public void crop(Rect rect) {

        float normalizedX = rect.x / this.getWidth();
        float normalizedY = rect.y / this.getHeight();
        float normalizedWidth = rect.getWidth() / this.getWidth();
        float normalizedHeight = rect.getHeight() / this.getHeight();

        float[] vertices = {
            0f, 1f, normalizedX, normalizedHeight,
            1f, 0f, normalizedWidth, normalizedY,
            0f, 0f, normalizedX, normalizedY,

            0f, 1f, normalizedX, normalizedHeight,
            1f, 1f, normalizedWidth, normalizedHeight, 
            1f, 0f, normalizedWidth, normalizedY
        };

        VBO vbo = new VBO(vertices);
        vbo.bind();

        VAO newData = new VAO();
        newData.bind();
        newData.linkToVAO(0, 4);

        Window.addVAO(newData);
        Window.addVBO(vbo);

        setSize(new Vector2f(rect.getWidth(), rect.getHeight()));

        data = newData;
    }
    public Surface subsurface(Rect rect) {
        Surface newSurface = this;
        newSurface.crop(rect);
        return newSurface;
    }
    public void flip(boolean horizontal, boolean vertical) {
        float left = 0f;
        float right = 1f;
        if (horizontal) {
            float temp = right;
            right = left;
            left = temp;
        }
        if (flippedX) {
            float temp = right;
            right = left;
            left = temp;
        }
        float top = 1f;
        float bottom = 0f;
        if (vertical) {
            float temp = top;
            top = bottom;
            bottom = temp;
        }
        if (flippedY) {
            float temp = top;
            top = bottom;
            bottom = temp;
        }

        float[] vertices = {
            0f, 1f, left, top,
            1f, 0f, right, bottom,
            0f, 0f, left, bottom,

            0f, 1f, left, top,
            1f, 1f, right, top, 
            1f, 0f, right, bottom
        };

        VBO vbo = new VBO(vertices);
        vbo.bind();

        VAO newData = new VAO();
        newData.bind();
        newData.linkToVAO(0, 4);

        Window.addVAO(newData);
        Window.addVBO(vbo);

        data = newData;

        flippedX = !(flippedX == horizontal);
        flippedY = !(flippedY == vertical);
    }
    public void setData(float[] data) {
        VBO vbo = new VBO(data);
        vbo.bind();

        VAO newData = new VAO();
        newData.bind();
        newData.linkToVAO(0, 4);

        Window.addVAO(newData);
        Window.addVBO(vbo);

        this.data = newData;
        this.rawData = data;

        System.out.println(data.length);
    }
}
