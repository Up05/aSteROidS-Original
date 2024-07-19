import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

public class TextFast {

    public String text;
    public static int w, h;
    static int id = GL11.glGenTextures();

    public static BufferedImage font;

    public static void init(){
        if(font == null){
            try {
                font = ImageIO.read(new File("C:\\Users\\Augustas\\Desktop\\c_Java\\Graphics\\Pong-LWJGL\\Lucida Console.png"));

                w = font.getWidth();
                h = font.getHeight();

                int colorComps = font.getColorModel().getNumComponents();

                byte[] data = new byte[colorComps * w * h];
                font.getRaster().getDataElements(0, 0, w, h, data);

                ByteBuffer pixels = BufferUtils.createByteBuffer(data.length);
                pixels.put(data);
                pixels.rewind();

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
                pixels.clear();


            } catch (Exception e) {
                System.out.println("Lucida Console.bff doesn't exist, for some reason???");
                System.out.println(new File("Lucida Console.bmp").getAbsolutePath());
            }
        }
    }

    int x, y;
    int cs = 16; // character spacing
    boolean shouldCenter = false, shouldCenterY;
    TextFast(String text, int x, int y){
        this.text = text; this.x = x; this.y = y;
    }

//    public TextFast update(){ return this; }
    public TextFast setSpaceBetweenChars(int space){
        cs = space; return this;
    }
    public TextFast setCentered(boolean centered){
        shouldCenter = centered; return this;
    }
    public TextFast setCenteredY(boolean centered){
        shouldCenterY = centered; return this;
    }

    public void draw(){
        int _y = y;
        if(shouldCenterY) _y -= 8; // technically incorrect, but I don't plan on doing this with multiple lines.
        int index = 0;
        for(int i = 0; i < text.length(); i ++){

            if(text.charAt(i) == '\r') continue;
            if(text.charAt(i) == '\n') {
                index = 0;
                _y += 16;
                continue;
            }

            final int charCode = (int) text.charAt(i) - 32;
            double
                tx = (charCode % 16 / 16f),
                ty = (Math.floor(charCode / 16f) / 16f);

            int _x = x + index * cs;
            if(shouldCenter) _x -= text.length() / 2 * cs;

            Shapes.character(_x, _y, 16, 16, tx, ty, 0.0625, 0.0625, id);
            index ++;
        }
    }

    public TextFast undraw(){
        int _y = y;
        if(shouldCenterY) _y -= 8;
        GL11.glColor3f(0.05f, 0.05f, 0.05f);
        int _w = (cs) * text.length();
        if(!shouldCenter)
            Main.rect(x,             _y, _w, 16);
        else
            Main.rect(x - _w / 2, _y, _w, 16);
        GL11.glColor3f(1f, 1f, 1f);
        return this;
    }

}
