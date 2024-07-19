import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    private static long window;
    public static int width = 600, height= 400;

    public static boolean gameLost;
    public static Main instance;

    public void run() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        instance = this;
        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "aSteROiDs", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        GL.createCapabilities();
        glClearColor(0.05f, 0.05f, 0.05f, 0.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glOrtho(0, width, height, 0, 1.0, -1.0); // p5 proj mat thing yeap
        EnemyManager.init();

//        Text score = new Text("Score " + Stats.SCORE);
        TextFast.init();
        Sound.init();
        Sound.setMasterVolume(Stats.MASTER_VOLUME);

//        new Sound(Sound.BACKGROUND_MUSIC).setLooping(true).play();

        AtomicBoolean paused = new AtomicBoolean(false);

        while ( !glfwWindowShouldClose(window) ) {

            {
                IntBuffer pWidth  = BufferUtils.createIntBuffer(1); // int*
                IntBuffer pHeight = BufferUtils.createIntBuffer(1); // int*
                glfwGetWindowSize(window, pWidth, pHeight);
                if(width != pWidth.get(0)) {
                    width = pWidth.get(0);
                    height = pHeight.get(0);
                    glViewport(0, 0, width, height);
                    EnemyManager.player.pos = new Vector2(width / 2f, height / 2f);
                    EnemyManager.player.updateText();
                    glLoadIdentity();
                    glOrtho(0, width, height, 0, 1.0, -1.0); // p5 proj mat thing yeap
                    glMatrixMode(GL11.GL_MODELVIEW);
                }
            }


            glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
                if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                    paused.set(!paused.get());
                    if(paused.get()) Stats.onPauseStart();
                    else             Stats.onPauseEnd();
                }
                if(key == GLFW_KEY_F3 && action == GLFW_RELEASE) Stats.shouldDraw = !Stats.shouldDraw;

                if(key == GLFW_KEY_D && action == GLFW_RELEASE) Stats.DIFFICULTY = Stats.DIFFICULTY % 4 + 1;

                if(key == GLFW_KEY_UP   && action == GLFW_PRESS)  Stats.MASTER_VOLUME += 0.05;
                if(key == GLFW_KEY_DOWN && action == GLFW_PRESS)  Stats.MASTER_VOLUME -= 0.05;
                if(key == GLFW_KEY_UP   && action == GLFW_REPEAT) Stats.MASTER_VOLUME += 0.025;
                if(key == GLFW_KEY_DOWN && action == GLFW_REPEAT) Stats.MASTER_VOLUME -= 0.025;

                if(key == GLFW_KEY_DOWN || key == GLFW_KEY_UP){
                    if(Stats.MASTER_VOLUME < 0) Stats.MASTER_VOLUME = 0;
                    else if(Stats.MASTER_VOLUME > 1) Stats.MASTER_VOLUME = 1;
                    Sound.setMasterVolume(Stats.MASTER_VOLUME);
                }

            });

            if(paused.get()){
                new TextFast("Game Paused!", width / 2, height / 4).setCentered(true).setCenteredY(true).setSpaceBetweenChars(12).draw();
                new TextFast("Difficulty (d to cycle): " + Stats.DIFFICULTY, width / 2, height - height / 4)
                        .setCentered(true).setCenteredY(true).setSpaceBetweenChars(11).undraw().draw();

            } else
            {

                glfwSetMouseButtonCallback(window, (long window, int button, int action, int mods) -> {
                    if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE && !paused.get())
                        EnemyManager.player.shoot();
                });

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                EnemyManager.trySpawn();
                EnemyManager.drawEnemies();
                EnemyManager.player.draw();
                EnemyManager.tick();

                new TextFast("Score: " + Stats.SCORE, width / 2, 10).setCentered(true).setSpaceBetweenChars(10).draw();

                Stats.draw();
                if (Stats.FRAME_COUNT % 10 == 0)
                    Stats.update();
                if(!Main.gameLost)
                    Stats.FRAME_COUNT++;
            }

            glfwSwapBuffers(window); // swap the color buffers
            glfwPollEvents();

        }
    }

    public static void rect(int x, int y, int w, int h){
        final int x2 = x + w, y2 = y + h;
        glBegin(GL_QUADS);
            glVertex2i(x , y );
            glVertex2i(x2, y );
            glVertex2i(x2, y2);
            glVertex2i(x , y2);
        glEnd();
    }

    public static void ellipse(int x, int y, int r, int res) {
        final int vertN = res + 1;
        final double TWO_PI = Math.PI * 2d;

        Integer[] verts = new Integer[vertN * 2];

        for (int i = 1; i < vertN; i ++){
            verts[i * 2]     = (int) Math.round(x + ( r * Math.cos( i * TWO_PI / res ) ));
            verts[i * 2 + 1] = (int) Math.round(y + ( r * Math.sin( i * TWO_PI / res ) ));
        }

        glPushMatrix();
        glBegin(GL_TRIANGLE_FAN);
        for(int i = 0; i < verts.length; i += 2)
            if(verts[i] != null && verts[i + 1] != null)
                glVertex2d(verts[i], verts[i + 1]);
        glEnd();
        glPopMatrix();
    }

    public static Vector2 getMousePos(){
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        glfwGetCursorPos(window, xpos, ypos);

        return new Vector2(xpos[0], ypos[0]);
    }

    public static int charCount(String str, char c){
        int count = 0;
        for(char _c : str.toCharArray())
            if(_c == c) count ++;
        return count;
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {




        new Main().run();
    }

}

/*
* Todo:
*
* - do player loss stuff
* - maybe add some sort of console commands
*
* ! SOUNDS, finish sounds, add hit, miss, attacked & enemy spawn sounds into the game (and test volume and other sounds).
*
* */