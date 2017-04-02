package com.opengltest.main;

import com.opengltest.main.entities.Camera;
import com.opengltest.main.entities.Entity;
import com.opengltest.main.entities.Light;
import com.opengltest.main.math.Vector3f;
import com.opengltest.main.models.TexturedModel;
import com.opengltest.main.models.RawModel;
import com.opengltest.main.renderEngine.Loader;
import com.opengltest.main.renderEngine.MasterRenderer;
import com.opengltest.main.renderEngine.OBJLoader;
import com.opengltest.main.terrains.Terrain;
import com.opengltest.main.textures.ModelTexture;
import com.opengltest.main.toolbox.Keyboard;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Alexander on 2017. 03. 19..
 */
public class DisplayManager {
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private long window;
    private static final String TEXTURE_FILE = "res/tree.png";
    private GLFWKeyCallback keyCallback;


    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        keyCallback.free();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(600, 600, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetKeyCallback(window, keyCallback = new Keyboard());


        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        Loader loader = new Loader();


        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("res/tree.png")));

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }

        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("res/grass.png")));
        Terrain terrain2 = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("res/grass.png")));

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer(window);
//        ModelTexture texture = new ModelTexture(loader.loadTexture(TEXTURE_FILE));
//        texture.setReflectivity(11.2f);
//        texture.setShineDamper(111f);
//        RawModel model = OBJLoader.loadObjModel("tree", loader);
//        TexturedModel texturedModel = new TexturedModel(model, texture);
//
//        Entity entity = new Entity(texturedModel, new Vector3f(0, 0,-15f), 0, 0, 0, 1.5f);
//        Light light = new Light(new Vector3f(0,14,-10), new Vector3f(1,1,1));
//
//        Camera camera = new Camera();



        // Set the clear color
        glClearColor(0.0f, 1.0f, 1.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//            entity.increaseRotation(0, 0.5f, 0);
//            masterRenderer.processEntity(entity);
//            masterRenderer.render(light, camera);

            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);

            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        renderer.cleanUp();
        loader.cleanUp();
    }


    public static void main(String[] args) {
        new DisplayManager().run();
    }

}

