import java.security.Key;
import java.util.ArrayList;

import org.joml.Vector2f;

import core.Window;
import framework.Sprite;
import graphics.Surface;
import io.Keyboard;
import io.Keys;
import utils.Font;
import utils.Rect;
import utils.Time;

public class EngineTest {
    public static void main(String[] args) {
        Window window = new Window(1280, 720, "Game Engine Test");  
        window.setFPS(60);      

        Surface surface = new Surface("src/res/player_static.png");
        surface.scaleBy(5);
        Surface atlas = new Surface("src/res/owatlas.png");
        Surface grass = atlas.subsurface(new Rect(0, 0, 8, 8));
        grass.scaleBy(6);
        Rect surfaceRect = surface.getRect(new Vector2f(200, 200));

        ArrayList <Sprite> sprites = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sprites.add(new Sprite(grass, new Rect(i*48, 600, grass.getWidth(), grass.getHeight())));
        }

    Vector2f velocity = new Vector2f();
        float speed = 5;

        // states
        boolean grounded = false;
        boolean left = true;
        boolean right = false;

        // font
        Surface glyph = new Surface("src/res/coolfont.png");
        glyph.scaleBy(5);
        Font font = new Font(glyph, "abcdefghijklmnopqrstuvwxyz", 50);
        Surface textSurface = font.render("hello there");

        while (!window.closed()) {
            window.update();
            window.fill(0.5f, 0.7f, 1.0f, 1.0f);
            window.prepareDisplay();

            if (Keyboard.isKeyDown(Keys.Q)) {
                window.close();
            }
            if (Keyboard.isKeyDown(Keys.A)) {
                velocity.x = -1 * Time.getDeltaTime() * 100;
                if (!left) {
                    surface.flip(true, false);
                    left = true;
                    right = false;
                }
            }
            if (Keyboard.isKeyDown(Keys.D)) {
                velocity.x = 1 * Time.getDeltaTime() * 100;
                if (!right) {
                    surface.flip(true, false);
                    right = true;
                    left = false;
                }
            }
            if (Keyboard.isKeyDown(Keys.W)) {
                velocity.y = -1 * Time.getDeltaTime() * 100;
            }
            if (Keyboard.isKeyDown(Keys.S)) {
                velocity.y = 1 * Time.getDeltaTime() * 100;
            }
            if (Keyboard.isKeyDown(Keys.SPACE) && grounded) {
                velocity.y = -6 * Time.getDeltaTime() * 100;
            }

            velocity.y += 0.5 * Time.getDeltaTime() * 100;
            if (velocity.y > 5 * Time.getDeltaTime() * 100) {
                velocity.y = 5 * Time.getDeltaTime() * 100;
            }

            window.drawDebug(surface, surfaceRect);
            window.draw(surface, surfaceRect);
            for (Sprite sprite : sprites) {
                window.draw(sprite.getSurface(), sprite.getRect());
            }

            surfaceRect.x += velocity.x * speed;
            for (Sprite sprite : sprites) {
                Rect block = sprite.getRect();

                if (surfaceRect.collideRect(block)) {
                    if (velocity.x > 0) {
                        surfaceRect.x = block.x - surfaceRect.getWidth();
                    } else if (velocity.x < 0) {
                        surfaceRect.x = block.x + block.getWidth();
                    }
                }
            }
            surfaceRect.y += velocity.y * speed;
            int collisions = 0;
            for (Sprite sprite : sprites) {
                Rect block = sprite.getRect();

                if (surfaceRect.collideRect(block)) {
                    if (velocity.y > 0) {
                        collisions += 1;
                        velocity.y = 0;
                        surfaceRect.y = block.y - surfaceRect.getHeight();
                    } else if (velocity.y < 0) {
                        surfaceRect.y = block.y + block.getHeight();
                    }
                }
            }
            if (collisions > 0) {
                grounded = true;
            } else {
                grounded = false;
            }

            window.drawDebug(textSurface, new Vector2f(100,100));
            window.draw(textSurface, new Vector2f(100,100));

            velocity.x = 0;
        }
        window.close();
    }
}
