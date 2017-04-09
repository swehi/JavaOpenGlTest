package com.opengltest.main;

import com.opengltest.main.entities.Camera;
import com.opengltest.main.entities.Entity;
import com.opengltest.main.entities.Light;
import com.opengltest.main.entities.Player;
import com.opengltest.main.math.Vector3f;
import com.opengltest.main.models.TexturedModel;
import com.opengltest.main.models.RawModel;
import com.opengltest.main.renderEngine.Loader;
import com.opengltest.main.renderEngine.MasterRenderer;
import com.opengltest.main.renderEngine.OBJLoader;
import com.opengltest.main.renderEngine.newloader.ModelData;
import com.opengltest.main.renderEngine.newloader.OBJFileLoader;
import com.opengltest.main.terrains.Terrain;
import com.opengltest.main.textures.ModelTexture;
import com.opengltest.main.textures.TerrainTexture;
import com.opengltest.main.textures.TerrainTexturePack;
import com.opengltest.main.toolbox.Keyboard;
import com.opengltest.main.toolbox.MouseCursorPos;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
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
    private GLFWCursorPosCallback cursorPosCallback;
    private static long lastFrameTime;
    private static float delta;


    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        keyCallback.free();
        cursorPosCallback.free();

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
        window = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        lastFrameTime = getCurrentTime();
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetKeyCallback(window, keyCallback = new Keyboard());
        glfwSetCursorPosCallback(window, cursorPosCallback = new MouseCursorPos());

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

        /*
        ***************TEXTURE PACK************
         */
        TerrainTexture bacTerrainTexture = new TerrainTexture(loader.loadTexture("res/grassy.png"));
        TerrainTexture rTerrainTexture = new TerrainTexture(loader.loadTexture("res/dirt.png"));
        TerrainTexture gTerrainTexture = new TerrainTexture(loader.loadTexture("res/pinkFlowers.png"));
        TerrainTexture bTerrainTexture = new TerrainTexture(loader.loadTexture("res/path.png"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("res/blendMap.png"));

        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(bacTerrainTexture, rTerrainTexture,gTerrainTexture,bTerrainTexture);

//        ModelData modelData = OBJFileLoader.loadOBJ("tree");
//        loader.loadToVao(modelData.getVertices(), modelData.getIndices(), modelData.getTextureCoords(), modelData.getNormals());

        RawModel tree = OBJFileLoader.loadToModel("tree", loader);
        RawModel tree2 = OBJFileLoader.loadToModel("lowPolyTree", loader);

        TexturedModel staticModel = new TexturedModel(tree,new ModelTexture(loader.loadTexture("res/tree.png")));
        TexturedModel staticModel2 = new TexturedModel(tree,new ModelTexture(loader.loadTexture("res/lowPolyTree.png")));

        RawModel grassModel = OBJFileLoader.loadToModel("grassModel", loader);
        TexturedModel grassTextModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("res/grassTexture.png")));
        grassTextModel.getTexture().setHasTransparency(true);
        grassTextModel.getTexture().setUseFakeLighting(true);


        RawModel fernModel = OBJFileLoader.loadToModel("fern", loader);
        TexturedModel fernTextModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("res/fern.png")));
        fernTextModel.getTexture().setHasTransparency(true);
        fernTextModel.getTexture().setNumberOfRows(2);

        Terrain terrain2 = new Terrain(0,-1,loader, terrainTexturePack, blendMap, "heightmap");

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            float x = random.nextFloat()*800 - 400;
            float z = random.nextFloat() * -600;
            float y = terrain2.getHeightOfTerrain(x,z);
            if (i % 5 == 0){
                entities.add(new Entity(grassTextModel, new Vector3f(x,y,z),0,0,0,0.6f));
            }else if(i % 10 == 0){
                entities.add(new Entity(staticModel2, new Vector3f(x,y,z),0,0,0,2.6f));
            }else if(i%2 ==0){
                entities.add(new Entity(staticModel, new Vector3f(x,y,z),0,0,0,3));
            }else{
                entities.add(new Entity(fernTextModel, new Vector3f(x,y,z),0,0,0,1, random.nextInt(4)));
            }
        }

        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

//        Terrain terrain = new Terrain(-1,-1,loader, terrainTexturePack, blendMap, "heightmap");

        MasterRenderer renderer = new MasterRenderer(window);
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/image.png"));
        texture.setReflectivity(11.2f);
        texture.setShineDamper(111f);
        RawModel model = OBJLoader.loadObjModel("bunny", loader);
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Player player = new Player(texturedModel, new Vector3f(110, 0, -50),0,0,0,1);
        Camera camera = new Camera(player, window);

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

            camera.move((MouseCursorPos) cursorPosCallback);
            player.move(terrain2);

            renderer.processEntity(player);
//            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);

            long currentFrameTime = getCurrentTime();
            delta = currentFrameTime - lastFrameTime;
            lastFrameTime = currentFrameTime;

            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        renderer.cleanUp();
        loader.cleanUp();
    }

    public static float getFrameTimeSeconds(){
        return delta/100000000;
    }

    private static long getCurrentTime(){
//        return (long)org.lwjgl.glfw.GLFW.glfwGetTime();
        return System.nanoTime();
    }

    public static void main(String[] args) {
        new DisplayManager().run();
    }

}

