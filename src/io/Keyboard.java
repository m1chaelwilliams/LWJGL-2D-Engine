package io;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {
    private static Map <Keys, Integer> keymapper = new HashMap<>() {{
        put(Keys.A, GLFW.GLFW_KEY_A);
        put(Keys.B, GLFW.GLFW_KEY_B);
        put(Keys.C, GLFW.GLFW_KEY_C);
        put(Keys.D, GLFW.GLFW_KEY_D);
        put(Keys.E, GLFW.GLFW_KEY_E);
        put(Keys.F, GLFW.GLFW_KEY_F);
        put(Keys.G, GLFW.GLFW_KEY_G);
        put(Keys.H, GLFW.GLFW_KEY_H);
        put(Keys.I, GLFW.GLFW_KEY_I);
        put(Keys.J, GLFW.GLFW_KEY_J);
        put(Keys.K, GLFW.GLFW_KEY_K);
        put(Keys.L, GLFW.GLFW_KEY_L);
        put(Keys.M, GLFW.GLFW_KEY_M);
        put(Keys.N, GLFW.GLFW_KEY_N);
        put(Keys.O, GLFW.GLFW_KEY_O);
        put(Keys.P, GLFW.GLFW_KEY_P);
        put(Keys.Q, GLFW.GLFW_KEY_Q);
        put(Keys.R, GLFW.GLFW_KEY_R);
        put(Keys.S, GLFW.GLFW_KEY_S);
        put(Keys.T, GLFW.GLFW_KEY_T);
        put(Keys.U, GLFW.GLFW_KEY_U);
        put(Keys.V, GLFW.GLFW_KEY_V);
        put(Keys.W, GLFW.GLFW_KEY_W);
        put(Keys.X, GLFW.GLFW_KEY_X);
        put(Keys.Y, GLFW.GLFW_KEY_Y);
        put(Keys.Z, GLFW.GLFW_KEY_Z);
        put(Keys.SPACE, GLFW.GLFW_KEY_SPACE);
        put(Keys.LSHIFT, GLFW.GLFW_KEY_LEFT_SHIFT);
        put(Keys.ESCAPE, GLFW.GLFW_KEY_ESCAPE);
    }};

    private static GLFWKeyCallback keyCallback;

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public static void init(long window) {
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < GLFW.GLFW_KEY_LAST) {
                    keys[key] = action != GLFW.GLFW_RELEASE;
                }
            }
        };

        GLFW.glfwSetKeyCallback(window, keyCallback);
    }

    public static void cleanup() {
        keyCallback.free();
    }

    public static boolean isKeyDown(Keys key) {
        int keycode = keymapper.get(key);
        if (keycode >= 0 && keycode < GLFW.GLFW_KEY_LAST) {
            return keys[keycode];
        }
        return false;
    }


}
