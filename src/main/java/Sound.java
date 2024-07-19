import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Sound {

    public static final int
        BACKGROUND_MUSIC = 0,
        HIT = 1,
        MISS = 2,
        ATTACKED = 3,
        ENEMY_SPAWN = 4;

    public static ArrayList<Clip> sfx = new ArrayList<>();

    public static void init() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String[] paths = new String[] {
            "bg.wav",
            "hit.wav",
            "miss.wav",
            "attacked.wav",
            "enemy_spawn.wav",
        };

        for(String path : paths) {
            URL url = Main.instance.getClass().getResource(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip;
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            sfx.add(clip);
        }

    }

    private int index;
    private boolean loop = false;

    public Sound(int sfxType){
        index = sfxType;
    }

    public Sound play(){
//        Clip current = sfx.get(index);
//        if(current.isRunning())
//            current.stop();
//        current.setFramePosition(0);
//        current.start();
//        if(loop) current.loop(Clip.LOOP_CONTINUOUSLY);
        return this;
    }

    public Sound stop(){
        Clip current = sfx.get(index);
        current.stop();
        return this;
    }

    public Sound setLooping(boolean bool){
        loop = bool;
        return this;
    }

    public static void setMasterVolume(double volume){
        for(Clip clip : sfx) {
            if (volume < 0f || volume > 1f)
                throw new IllegalArgumentException("Volume not valid: " + Stats.MASTER_VOLUME);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(Stats.MASTER_VOLUME));
        }
    }



}
