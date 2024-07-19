public class Enemy {

    Vector2 pos;
    final Vector2 size = new Vector2(25, 25);
    boolean dead;

    public Enemy(){
        double x, y;
        if(Math.random() > 0.5) {
            x = Math.random() * Main.width;
            y = Math.random() > 0.5 ? 0 : Main.height;
            x = x < Main.width / 2f ? x : x - 25;
            y = y == 0 ? y : y - 25;
        } else {
            x = Math.random() > 0.5 ? 0 : Main.width;
            y = Math.random() * Main.height;
            x = x == 0 ? x : x - 25;
            y = y < Main.width / 2f ? y : y - 25;
        }
        pos = new Vector2(x, y);

        new Sound(Sound.ENEMY_SPAWN).play();


    }

    public void draw(){
        Main.rect((int) pos.getX(), (int) pos.getY(), (int) size.getX(), (int) size.getY());
    }

    public void move(){
        if(Main.gameLost) return;

        Player player = EnemyManager.player;
        pos.add(Vector2.sub(player.pos, pos.center(size)).normalize().mul(1 + Stats.SCORE / 25000f > 3 + Stats.DIFFICULTY ? 3 + Stats.DIFFICULTY : 1 + Stats.SCORE / 2500f));
        pos.setY(pos.getY() + 0.5);

        if(!dead && pos.isInCenterOfScreen(size)) {
            player.takeDamage();
            new Sound(Sound.ATTACKED).play();
            if(!Main.gameLost)
                dead = true;

        }

    }

    public void die(){
        dead = true;
        Stats.SCORE += 10;
    }
}
