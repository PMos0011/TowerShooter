package pmos0011.biox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES31;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class GameObjectsLoader {

    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();
    private int[] textureIds;

    public GameObjectsLoader(Context context, int[] texturesId) {
        this.textureIds = new int[texturesId.length];
        int i=0;

        for (int texture : texturesId) {
            this.textureIds[i]=loadTexture(context, texture);
            i++;
        }
    }

    public TextureModel loadToVAO(float[] coords, float[] textureCoords, short[] drawOrder) {
        int vaoID = createVAO();
        bindVertexArray(vaoID);
        bindDrawOrdersBuffer(drawOrder);
        storeDataInAttribute(0, TextureModel.COORDS_PER_VERTEX, coords);
        storeDataInAttribute(1, TextureModel.COORDS_PER_VERTEX, textureCoords);
        unbindVertexArray();

        return new TextureModel(vaoID);
    }

    private int createVAO() {
        int[] vao = new int[1];
        GLES31.glGenVertexArrays(1, vao, 0);
        vaoList.add(vao[0]);
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

    private void bindVertexArray(int vaoID) {
        GLES31.glBindVertexArray(vaoID);
    }

    private void unbindVertexArray() {
        GLES31.glBindVertexArray(0);
    }


    private void storeDataInAttribute(int attributeNumber, int size, float[] data) {
        int vboID = createVBO();
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, vboID);

        FloatBuffer dataBuffer = test(data);

        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, data.length * 4, dataBuffer, GLES31.GL_STATIC_DRAW);
        GLES31.glVertexAttribPointer(attributeNumber, size, GLES31.GL_FLOAT, false, 0, 0);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer test(float[] data) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer dataBuffer = buffer.asFloatBuffer();
        dataBuffer.put(data);
        dataBuffer.position(0);

        return dataBuffer;
    }

    private int loadTexture(Context context, int texture_id) {

        int[] textureHandle = new int[1];
        GLES31.glGenTextures(1,textureHandle,0);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture_id, options);

        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);
        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, flippedBitmap, 0);

        bitmap.recycle();
        flippedBitmap.recycle();

        return textureHandle[0];
    }

    public int getTextureID(int id) {
        return textureIds[id];
    }

    public void loadMatrixUniform(int handle, float[] matrix) {
        GLES31.glUniformMatrix4fv(handle, 1, false, matrix, 0);
    }

    public void addProjectionMatrixAttribute(int vao,  float[] matrix) {
        int vboID = createVBO();
        bindVertexArray(vao);
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, vboID);

        FloatBuffer dataBuffer = test(matrix);

        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, matrix.length * 4, dataBuffer, GLES31.GL_STATIC_DRAW);
        GLES31.glVertexAttribPointer(2, 4, GLES31.GL_FLOAT, false, 16*4, 0);
        GLES31.glVertexAttribPointer(3, 4, GLES31.GL_FLOAT, false, 16*4, 4*4);
        GLES31.glVertexAttribPointer(4, 4, GLES31.GL_FLOAT, false, 16*4, 8*4);
        GLES31.glVertexAttribPointer(5, 4, GLES31.GL_FLOAT, false, 16*4, 12*4);

        GLES31.glVertexAttribDivisor(2, 1);
        GLES31.glVertexAttribDivisor(3, 1);
        GLES31.glVertexAttribDivisor(4, 1);
        GLES31.glVertexAttribDivisor(5, 1);


        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
        unbindVertexArray();
    }

    public void addUniformBlockBuffer(int bindingPoint, float[] matrix){
        int vboID = createVBO();
        GLES31.glBindBuffer(GLES31.GL_UNIFORM_BUFFER, vboID);

        FloatBuffer dataBuffer = test(matrix);

        GLES31.glBufferData(GLES31.GL_UNIFORM_BUFFER, matrix.length * 4, dataBuffer, GLES31.GL_STATIC_DRAW);
        GLES31.glBindBuffer(GLES31.GL_UNIFORM_BUFFER, 0);

        GLES31.glBindBufferRange(GLES31.GL_UNIFORM_BUFFER,bindingPoint,vboID,0,matrix.length*4);

    }
}
