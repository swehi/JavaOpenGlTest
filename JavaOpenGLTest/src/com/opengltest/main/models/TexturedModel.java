package com.opengltest.main.models;

import com.opengltest.main.models.model.RawModel;
import com.opengltest.main.textures.ModelTexture;

/**
 * Created by Alexander on 2017. 03. 25..
 */
public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture modelTexture;

    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }
}
