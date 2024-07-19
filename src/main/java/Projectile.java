
import static org.lwjgl.opengl.GL11.*;

public class Projectile {

    Vector2 pos, vel;
    public int index = -1;

    boolean created = true,
            dead = false,
            shouldDraw = true;
    boolean collided = false;

    Explosion explosion = null;

    public Projectile(){ created = false; }

    public Projectile(Vector2 position, Vector2 velocity, int index){
        pos = position.clone();
        vel = velocity.clone();
        this.index = index;
    }

    public Projectile move(){
        pos.add(vel);
        return this;
    }

    public Projectile draw(){
        if(explosion != null)
            dead = explosion.draw();
        if(!shouldDraw) return this;
        glBegin(GL_LINE_STRIP);
            glColor4f(1f, 1f, 1f, 0.5f);
            pos.vertex();
            glColor4f(1f, 1f, 1f, 1f);
            vel.clone().normalize().mul(20).add(pos).vertex();
        glEnd();
        return this;
    }

    public Projectile offscreen(){
        if(explosion != null)
            return this;

        if(pos.getX() < 0 || pos.getY() < 0 || pos.getX() > Main.width || pos.getY() > Main.height) {
            Stats.MISSED ++;
            explosion = new Explosion(pos, 31);
            new Sound(Sound.MISS).play();
        }
        return this;
    }

    public boolean isDead(){
        return dead;
    }

    public Projectile hasCollided(){

        if(explosion != null)
            return this;

        pos.add(Vector2.mul(vel, 10));
        for(Enemy enemy : EnemyManager.enemies){
            final double px = pos.getX(), py = pos.getY(), rx = enemy.pos.getX(), ry = enemy.pos.getY(), rw = enemy.size.getX(), rh = enemy.size.getY();

            if(px >= rx &&         // right of the left edge AND
               px <= rx + rw &&    // left of the right edge AND
               py >= ry &&         // below the top AND
               py <= ry + rh )
            {

                enemy.die();
                explosion = new Explosion(pos, 47);
                collided = true;
                shouldDraw = false;
                new Sound(Sound.HIT).play();
//                System.out.println("collided"); // idk it just doesn't detect it and i can't be bothered to be able to think right now!
            }
        }
        pos.sub(Vector2.mul(vel, 10));
        return this;
    }


    @Override
    public String toString() {
        if(created)
            return "proj: { " + index + "; " + pos + "   " + vel + " } ";
        else return "proj: { NULL } "; // oh yeah this isn't C++, oopsie
    }


}
