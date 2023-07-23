package graphics;

public class SpriteData {
    static VAO vao;
    static VBO vbo;

    public static void loadData() {
        float[] vertices = {
            0f, 1f, 0f, 1f,
            1f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f,

            0f, 1f, 0f, 1f,
            1f, 1f, 1f, 1f, 
            1f, 0f, 1f, 0f
        };

        vao = new VAO();
        vao.bind();

        vbo = new VBO(vertices);
        vbo.bind();
        vao.linkToVAO(0, 4);
    }
    public static VAO getData() {
        return vao;
    }
    public static float[] getRawData() {
        float[] vertices = {
            0f, 1f, 0f, 1f,
            1f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f,

            0f, 1f, 0f, 1f,
            1f, 1f, 1f, 1f, 
            1f, 0f, 1f, 0f
        };
        return vertices;
    }
    public static void delete() {
        vao.dispose();
        vbo.dispose();
    }
}
