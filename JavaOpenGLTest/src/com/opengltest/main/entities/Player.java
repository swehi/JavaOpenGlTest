package com.opengltest.main.entities;

import com.opengltest.main.DisplayManager;
import com.opengltest.main.math.Vector3f;
import com.opengltest.main.models.TexturedModel;
import com.opengltest.main.terrains.Terrain;
import com.opengltest.main.toolbox.Keyboard;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

/**
 * Created by Alexander on 2017. 04. 09..
 */
public class Player extends Entity {

    private static final float RUN_SPEED = 2.5f;
    private static final float TURN_SPEED = 20;
    private static final float GRAVITY = -5;
    private static final float JUMP_POWER = 12;
    private static final float TERRAIN_HEIGT = 0;
    private boolean isInAir = false;

    private float currentSpeed = 0;
    private float currenTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private Vector3f position = new Vector3f(0,5,0);

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0, currenTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();

        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0 ,dz);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y<terrainHeight){
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }

//        if(Keyboard.isKeyDown(GLFW_KEY_W)){
//            position.z-=0.5f;
//        }
//        if(Keyboard.isKeyDown(GLFW_KEY_S)){
//            position.z-=-0.5f;
//        }
//        if(Keyboard.isKeyDown(GLFW_KEY_D)){
//            position.x+=0.5f;
//        }
//        if(Keyboard.isKeyDown(GLFW_KEY_A)){
//            position.x-=0.5f;
//        }
//        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
//            position.y+=0.2f;
//        }
//        if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
//            position.y-=0.2f;
//        }
    }

    private void jump(){
        if (!isInAir){
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
           currentSpeed = RUN_SPEED;
        }
        else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            currentSpeed = -RUN_SPEED;
        }else{
            currentSpeed = 0;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_D)){
            currenTurnSpeed = -TURN_SPEED;
        }else if(Keyboard.isKeyDown(GLFW_KEY_A)){
            currenTurnSpeed = TURN_SPEED;
        }else {
            currenTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            jump();
        }
    }
}
