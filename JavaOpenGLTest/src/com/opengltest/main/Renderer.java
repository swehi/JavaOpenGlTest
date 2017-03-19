package com.opengltest.main;

import com.opengltest.main.model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by Alexander on 2017. 03. 19..
 */
public class Renderer {

    public void prepare(){
        GL11.glClearColor(1,0,0,1);
    }

    public void render(RawModel rawModel){
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, rawModel.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
