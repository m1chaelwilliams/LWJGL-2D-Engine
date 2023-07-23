package core;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import graphics.ShaderProgram;
import graphics.SpriteData;
import graphics.Surface;
import graphics.VAO;
import graphics.VBO;
import io.Keyboard;
import io.Mouse;
import utils.Rect;
import utils.SyncTimer;
import utils.Time;

import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class Window {
    private int width, height;
    private String title;
    private long window;

    ShaderProgram program;
    ShaderProgram debugProgram;

    static ArrayList <VAO> vaos = new ArrayList<>();
    static ArrayList <VBO> vbos = new ArrayList<>();

    SyncTimer timer;

    private int FPS = 60;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        create();

        timer = new SyncTimer();
        Time.init();
        SpriteData.loadData();
        program = new ShaderProgram("default.vert", "default.frag");
        debugProgram = new ShaderProgram("default.vert", "debug.frag");
    }

    public void draw(Surface surface, Vector2f position) {
        bind(false);
        program.putDataIntoUniformVec2(position, "sprite_position");
        surface.uploadData(program);
        program.putDataIntoUniformVec2(new Vector2f(width, height), "screen_size");
        surface.draw();
    }
    public void draw(Surface surface, Rect rect) {
        bind(false);
        program.putDataIntoUniformVec2(rect.getPosition(), "sprite_position");
        surface.uploadData(program);
        program.putDataIntoUniformVec2(new Vector2f(width, height), "screen_size");
        surface.draw();
    }
    public void drawDebug(Surface surface, Rect rect) {
        bind(true);
        program.putDataIntoUniformVec2(rect.getPosition(), "sprite_position");
        surface.uploadData(program);
        program.putDataIntoUniformVec2(new Vector2f(width, height), "screen_size");
        surface.draw();
    }
    public void drawDebug(Surface surface, Vector2f position) {
        bind(true);
        program.putDataIntoUniformVec2(position, "sprite_position");
        surface.uploadData(program);
        program.putDataIntoUniformVec2(new Vector2f(width, height), "screen_size");
        surface.draw();
    }

    public void bind(boolean debug) {
        if (debug) {
            debugProgram.bind();
        } else {
            program.bind();
        }
    }
    public void drawRect(Rect rect) {

    }

    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to intialize GLFW");
        }

        // GLFW parameters
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // create window
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        Keyboard.init(window);
        Mouse.init(window);
        Mouse.setMousePos(new Vector2f(getWidth()/2, getHeight()/2));

        // resize callback
        GLFW.glfwSetFramebufferSizeCallback(window, (window, w, h) -> {
            this.width = w;
            this.height = h;
            GL11.glViewport(0,0,w, h);
        });


        // get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // allocating memory for an int
            IntBuffer pHeight = stack.mallocInt(1); // allocating memory for another int

            // get the window size
            GLFW.glfwGetWindowSize(window, pWidth, pHeight); // sending the two ints as references and having them be returned as the values for width and height

            // get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // center the window
            GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0))/2, (vidmode.height() - pHeight.get(0))/2);
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        GL.createCapabilities();
        GL11.glViewport(0, 0, width, height);
    }
    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
        Time.update();
        try {
            timer.sync(FPS);
        } catch(Exception error) {
            System.err.println(error);
        }
    }
    public void fill(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }
    public void prepareDisplay() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    public int getWidth() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(window, w, null);
        return w.get(0);
    }
    public int getHeight() {
        IntBuffer h = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(window, null, h);
        return h.get(0);
    }
    public void setFPS(int fps) {
        FPS = fps;
    }
    public boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }
    public static void addVAO(VAO vao) {
        vaos.add(vao);
    }
    public static void addVBO(VBO vbo) {
        vbos.add(vbo);
    }
    public void close() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        program.delete();
        SpriteData.delete();

        for (VBO vbo : vbos) {
            vbo.dispose();
        }
        for (VAO vao : vaos) {
            vao.dispose();
        }
    }
}