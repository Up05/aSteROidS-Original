import org.lwjgl.opengl.GL11;

public class Vector2 {

    private double x, y;

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector){
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2 vertex(){
        GL11.glVertex2d(x, y);
        return this;
    }

    public Vector2 normalize(){
        double a = Math.sqrt(this.x*this.x + this.y*this.y);
        this.x /= a;
        this.y /= a;
        return this;
    } // from lib\gravity... (mine)

    public Vector2 setMag(double magnitude, Vector2 pos){
        this.sub(pos).normalize().mul(magnitude).add(pos);
        return this;
    }

    public Vector2 center(Vector2 size){
        return new Vector2(x + size.getX()/2, y + size.getY()/2);
    }

    public boolean equals(Vector2 vector){
        return x == vector.getX() && y == vector.getY();
    }

    public boolean isInCenterOfScreen(Vector2 size){
        int hw = Main.width / 2, hh = Main.height / 2;
        int w = (int) size.getX(), h = (int) size.getY();

        return hw >= x &&
                hw <= x + w &&
                hh >= y &&
                hh <= y + h;
    }

    public Vector2 add(double ammount){
        this.x += ammount;
        this.y += ammount;
        return this;
    }


    public Vector2 add(Vector2 vector2){
        this.x += vector2.getX();
        this.y += vector2.getY();
        return this;
    }
    public Vector2 sub(Vector2 vector2){
        this.x -= vector2.getX();
        this.y -= vector2.getY();
        return this;
    }

    public Vector2 mul(double number){
        this.x *= number;
        this.y *= number;
        return this;
    }

    public Vector2 clone(){
        return new Vector2(this);
    }

    public static Vector2 add(Vector2 vector1, Vector2 vector2){
        return new Vector2(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
    }

    public static Vector2 sub(Vector2 vector1, Vector2 vector2){
        return new Vector2(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
    }

    public static Vector2 mul(Vector2 vector, double ammount){
        return new Vector2(vector.getX() * ammount, vector.getY() * ammount);
    }


    @Override
    public String toString() {
        return "( " + String.format("%.2f,  %.2f", x, y) + " )";
    }
}
