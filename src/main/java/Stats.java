public class Stats {

    public static boolean shouldDraw = false;

    public static int
        TIME_PLAYED,
        SCORE,
        SHOT,
        MISSED,
        ROUND_ID = (int) (Math.random() * Integer.MAX_VALUE),
        FRAME_COUNT,
        DIFFICULTY = 1;

    public static double MASTER_VOLUME = 0.5;

    private static long time_started = System.currentTimeMillis();

    public static void calcTimePast(){
        TIME_PLAYED = (int) (System.currentTimeMillis() - time_started);
    }

    public static String formatTime(int time){
        final int
            ms  = time % 1000,
            sec = time / 1000 % 60,
            min = time / 1000 / 60 % 60;
//            hour= time / 1000 / 60 / 60 % 24

        return String.format("%02d:%02d:%03d", min, sec, ms);
    }

    public static void update(){
        if(Main.gameLost) return;
        SCORE += DIFFICULTY;

        calcTimePast();

    }

    public static void draw(){
        if(shouldDraw)
        new TextFast(
                "Time played: " + formatTime(TIME_PLAYED) +
                "\nScore: " + SCORE +
                "\nShots fired: " + SHOT +
                "\nShots hit/kills: " + (SHOT - MISSED) +
                "\nShots missed: " + MISSED +
                "\n" +
                "\nRound ID: " + ROUND_ID +
                "\nFrame count: " + FRAME_COUNT +
                "\n\nVolume: " + Math.floor(MASTER_VOLUME * 1000) / 10 + '%' +
                "\nDifficulty: " + DIFFICULTY,
                5, 5
        ).setSpaceBetweenChars(10).draw();
    }
    private static long pauseTime;
    public static void onPauseStart(){
        pauseTime = System.currentTimeMillis();
    }

    public static void onPauseEnd(){
        time_started += (System.currentTimeMillis() - pauseTime);
    }
}
