import static org.lwjgl.opengl.GL11.*;

public class Shapes {

    private static boolean imagesEnabled = false;

    public static void debugPoint(double x, double y){
        glColor3b((byte) 0, (byte) 127, (byte) 0);
        glBegin(GL_POINTS);
            glVertex2d(x, y);
        glEnd();
        glColor3b((byte) 127, (byte) 127, (byte) 127);
    }

    public static void debugLine(double x, double y, double x2, double y2){
        glColor3b((byte) 0, (byte) 127, (byte) 0);
        glBegin(GL_LINE_STRIP);
        glVertex2d(x, y);
        glVertex2d(x2, y2);
        glEnd();
        glColor3b((byte) 127, (byte) 127, (byte) 127);

    }

    public static void image(int x, int y, int w, int h, int textureId){
        if(!imagesEnabled){
            glEnable(GL_TEXTURE_2D);
            imagesEnabled = true;
        }

        final int x2 = x + w, y2 = y + h;

        glBindTexture(GL_TEXTURE_2D, textureId);
        glBegin(GL_QUADS);
        glTexCoord2i(0, 0);
        glVertex2i(x , y );
        glTexCoord2i(1, 0);
        glVertex2i(x2, y );
        glTexCoord2i(1, 1);
        glVertex2i(x2, y2);
        glTexCoord2i(0, 1);
        glVertex2i(x , y2);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void character(int x, int y, int w, int h, double tx, double ty, double tw, double th, int textureId){
        if(!imagesEnabled){
            glEnable(GL_TEXTURE_2D);
            imagesEnabled = true;
        }

        final int x2 = x + w, y2 = y + h;
        final double tx2 = tx + tw, ty2 = ty + th;

        glBindTexture(GL_TEXTURE_2D, textureId);
        glBegin(GL_QUADS);
        glTexCoord2d(tx, ty);
        glVertex2i(x , y );
        glTexCoord2d(tx2, ty);
        glVertex2i(x2, y );
        glTexCoord2d(tx2, ty2);
        glVertex2i(x2, y2);
        glTexCoord2d(tx, ty2);
        glVertex2i(x , y2);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
