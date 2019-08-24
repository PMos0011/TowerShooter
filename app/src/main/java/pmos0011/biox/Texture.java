package pmos0011.biox;

public class Texture {

    private int vao;


    public final static float SQUERE_CORDS[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
    };

    public final static short DRAW_ORDER[] = {
            0, 1, 2,
            0, 2, 3
    };

    public final static int COORDS_PER_VERTEX = 2;
    public final static int VERTEX_COUNT = SQUERE_CORDS.length / COORDS_PER_VERTEX;
    public final static int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    public Texture(int vaoID) {
        this.vao = vaoID;
    }

    public int getVao() {
        return vao;
    }
}
