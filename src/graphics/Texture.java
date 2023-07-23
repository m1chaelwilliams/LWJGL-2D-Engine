package graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int id;
    private int width, height;

    public Texture(String filepath) {
        id = load(filepath);
    }

    public int load(String path) {
        ByteBuffer image;

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // STBImage.stbi_set_flip_vertically_on_load(true);
            image = STBImage.stbi_load(path, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file");
            }

            width = w.get();
            height = h.get();
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
                GL_UNSIGNED_BYTE, image);
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, getID());
    }

    public int getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
