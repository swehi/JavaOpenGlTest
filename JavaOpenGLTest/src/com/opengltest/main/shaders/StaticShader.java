package com.opengltest.main.shaders;

import com.opengltest.main.entities.Camera;
import com.opengltest.main.math.Matrix4f;
import com.opengltest.main.math.Matrix4f2;
import com.opengltest.main.toolbox.Maths;

/**
 * Created by Alexander on 2017. 03. 25..
 */
public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/com/opengltest/main/shaders/vertextTextureShader";
    private static final String FRAGMENT_FILE = "src/com/opengltest/main/shaders/fragmentTextureShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix (Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
