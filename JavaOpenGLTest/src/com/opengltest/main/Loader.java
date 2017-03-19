package com.opengltest.main;

import com.opengltest.main.model.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 2017. 03. 19..
 */
public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();


    public RawModel loadToVao(float[] position){
        int vaoID = createVao();
        storeDataInAttributeList(0, position);
        unbindVAO();
        return new RawModel(vaoID, position.length/3);
    }

    public void cleanUp(){
        for (int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        vaos.clear();
        vbos.clear();
    }

    private int createVao(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3 , GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }
}
