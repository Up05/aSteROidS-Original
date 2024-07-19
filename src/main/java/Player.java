import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class Player {

    Vector2 pos = new Vector2(Main.width / 2f, Main.height / 2f);
    Projectile[] projs = new Projectile[32];

    int health = 10;
    TextFast healthText;

    boolean dead = false;

    public Player(){
        updateText();

    }

    public void draw(){
        glBegin(GL_LINE_STRIP);
            pos.vertex();
            Main.getMousePos().setMag(40, pos).vertex();
        glEnd();

        healthText.draw();

        for (Projectile proj : projs)
            if (proj != null)
                if(proj.move().hasCollided().draw().offscreen().isDead()) {
                    projs[proj.index] = null;
                }

    }

    public void shoot(){
        if(Main.gameLost) return;

        for(int i = 0; i < projs.length; i ++)
            if(projs[i] == null) {
                projs[i] = new Projectile(pos, Main.getMousePos().sub(pos).mul(0.01), i);
                Stats.SHOT ++;
                break;
            }

    }

    public void takeDamage() {
        if(Main.gameLost) return;

        health -= 1;
        updateText();
        if(health <= 0)
            lose();
    }

    public void lose(){
        dead = true;
        Main.gameLost = true;


    }

    public void updateText(){
        healthText = new TextFast("Health " + health + "/10", 16, Main.height - 32).setSpaceBetweenChars(10);

    }
}
