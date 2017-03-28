package com.opengltest.main.entities;

import com.opengltest.main.Keyboard;
import com.opengltest.main.math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Alexander on 2017. 03. 26..
 */
public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera(){}

    public void move(){

        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            position.y+=0.02f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_S)){
            position.y-=0.02f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_D)){
            position.x-=0.02f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            position.x+=0.02f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}
