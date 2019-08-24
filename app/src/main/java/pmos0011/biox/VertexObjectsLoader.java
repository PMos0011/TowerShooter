package pmos0011.biox;

import android.opengl.GLES31;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexObjectsLoader {

    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();

    public Texture loadToVAO(float[] coords, short[] drawOrder) {
        int vaoID = createVAO();
        bindDrawOrdersBuffer(drawOrder);
        storeDataInAttribute(0, coords);
        GLES31.glBindVertexArray(0);

        return new Texture(vaoID);
    }

    private int createVAO() {
        int[] vao = new int[1];
        GLES31.glGenVertexArrays(1, vao,0 );
        vaoList.add(vao[0]);
        GLES31.glBindVertexArray(vao[0]);
        return vao[0];
    }

    private int createVBO() {
        int[] vbo = new int[1];
        GLES31.glGenBuffers(1, vbo, 0);
        vboList.add(vbo[0]);
        return vbo[0];
    }

    private void bindDrawOrdersBuffer(short[] drawOrder) {
        int vboID = createVBO();
        GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, vboID);

        ByteBuffer buffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        buffer.order(ByteOrder.nativeOrder());
        ShortBuffer indices = buffer.asShortBuffer();
        indices.put(drawOrder);
        indices.position(0);

        GLES31.glBufferData(GLES31.GL_ELEMENT_ARRAY_BUFFER, drawOrder.length * 2, indices, GLES31.GL_STATIC_DRAW);
    }

    private void storeDataInAttribute(int attributeNumber, float[] data) {
        int vboID = createVBO();
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, vboID);

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer dataBuffer = buffer.asFloatBuffer();
        dataBuffer.put(data);
        dataBuffer.position(0);

        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, data.length * 4, dataBuffer, GLES31.GL_STATIC_DRAW);
        GLES31.glVertexAttribPointer(attributeNumber, Texture.COORDS_PER_VERTEX, GLES31.GL_FLOAT, false, 0, 0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
    }


}
