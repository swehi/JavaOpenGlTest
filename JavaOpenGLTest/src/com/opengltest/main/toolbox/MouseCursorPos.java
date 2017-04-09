package com.opengltest.main.toolbox;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * Created by Alexander on 2017. 04. 09..
 */
public class MouseCursorPos extends GLFWCursorPosCallback {
    private double mouseX;
    private double mouseY;
    private double mouseDX;
    private double mouseDY;
    private boolean wasFirst;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        if (!wasFirst) {
            wasFirst = true;
        } else {
            mouseDX += xpos - mouseX;
            mouseDY += ypos - mouseY;
        }
        mouseX = xpos;
        mouseY = ypos;
    }

    public double getMouseDX() {
        double result = mouseDX;
        mouseDX = 0;
        return result;
    }

    public double getMouseDY() {
        double result = mouseDY;
        mouseDY = 0;
        return result;
    }
}
