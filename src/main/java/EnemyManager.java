import java.util.ArrayList;
import java.util.Collection;

public class EnemyManager {

    static Collection<Enemy> enemies = new ArrayList<>();
    static Collection<Enemy> deadEnemies = new ArrayList<>(10);

    static Player player = new Player();

    public static void init(){


    }

    public static void trySpawn(){
        if(Math.random() < 0.01 && !Main.gameLost){
            enemies.add(new Enemy());
        }
    }

    public static void drawEnemies(){
        for(Enemy enemy : enemies)
            enemy.draw();


    }

    public static void tick(){
        for(Enemy enemy : enemies){
            if(enemy.dead)
                deadEnemies.add(enemy);
            else
                enemy.move();
        }
        enemies.removeAll(deadEnemies);
        deadEnemies = new ArrayList<>(10);



    }

}
