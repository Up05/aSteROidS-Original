import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

@Deprecated
public class Text {

    public String text;
    int w, h;

    int id;

    Color color = new Color(255, 255, 255, 255);
    int fontSize = 16;
    boolean shouldCenter = true;

    Font font = new Font("Lucida Console", Font.PLAIN, fontSize);

    public Text(String text) {
        this.text = text;
    }

    public Text setShouldCenter(boolean shouldCenter) {
        this.shouldCenter = shouldCenter;
        return this;
    }

    public Text setColor(Color color){
        this.color = color;
        return this;
    }

    public Text setFontSize(int size){
        fontSize = size;
        return this;
    }

    public Text setText(String text){
        this.text = text;
        return this;
    }

    public Text update(){

        w = text.length() * fontSize;
        h = fontSize * 2 * (1 + Main.charCount(text, '\n'));

        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.setFont(font);

        short lineCount = 1;
        for(String line : text.split("\n"))
            g.drawString(line, 0, lineCount ++ * fontSize);

        int thevalueofthisis4butsureiguess = bufferedImage.getColorModel().getNumComponents();

        byte[] data = new byte[thevalueofthisis4butsureiguess * w * h];
        bufferedImage.getRaster().getDataElements(0, 0, w, h, data);

        ByteBuffer pixels = BufferUtils.createByteBuffer(data.length);
        pixels.put(data);
        pixels.rewind();

        id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        pixels.clear();
        return this;
    }

    public void draw(int x, int y){
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        if(shouldCenter)
            Shapes.image(x - w/4, y, w, h, id);
        else
            Shapes.image(x, y, w, h, id);

//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    }

}
