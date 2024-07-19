public class Explosion {

    Vector2 pos;
    double radius, max;

    Explosion(Vector2 position, double size){
        pos = position.clone(); max = size;
    }

    public boolean draw(){
        if(radius > max)
            return true;

        Main.ellipse((int) pos.getX(), (int) pos.getY(), (int) radius, 8);

        radius += 4;
        return false;
    }

    @Override
    public String toString() {
        return "Explosion ( radius: " + radius + ", max: " + max + ", pos: " + pos + " )";
    }
}
