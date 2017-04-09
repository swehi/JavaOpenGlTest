package com.opengltest.main.entities;

import com.opengltest.main.math.Vector3f;
import com.opengltest.main.toolbox.Keyboard;
import com.opengltest.main.toolbox.MouseCursorPos;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;

	private Vector3f position = new Vector3f(110,25,0);
	private float pitch = 30;
	private float yaw ;
	private float roll;
	private long window;

	private Player player;

	public Camera(Player player, long window){
		this.player = player;
		this.window = window;
	}
	
	public void move(MouseCursorPos mouseCursorPos){
        calculateZoom();
        calculateAngleAroundPlayer(mouseCursorPos);
        calculatePitch(mouseCursorPos);
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
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

	private void calculateZoom(){
//		float zoomLevel = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1);
//		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch(MouseCursorPos mouseCursorPos){
		if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
			float pitchChange = (float) mouseCursorPos.getMouseDY() *0.1f;
		    pitch -= pitchChange;
		}

	}

	private void calculateAngleAroundPlayer(MouseCursorPos mouseCursorPos){
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
            float angleChange = (float) mouseCursorPos.getMouseDX() *0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance(){
	    return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizDistance, float vertDistance){
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX =(float)(horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ =(float)(horizDistance * Math.cos(Math.toRadians(theta)));
        position.y = player.getPosition().y + vertDistance;
        position.z = player.getPosition().z - offsetZ;
        position.x = player.getPosition().x - offsetX;

    }
	

}
