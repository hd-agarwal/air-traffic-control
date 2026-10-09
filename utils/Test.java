package utils;

public class Test {
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";

    public static final String BLACK = "\u001B[30m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_RESET = "\u001B[0m";





    public static final String GREEN = "\u001B[32m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String[] text = {BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE};
    public static final String[] bg = {BLACK_BACKGROUND, RED_BACKGROUND, GREEN_BACKGROUND, YELLOW_BACKGROUND, BLUE_BACKGROUND, PURPLE_BACKGROUND, CYAN_BACKGROUND, WHITE_BACKGROUND};

    public static void main(String[] args) {
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < bg.length; j++) {
                if (i == j) {
                    continue;
                }
                System.out.println(text[i] + bg[j] + "FOREGROUND " + i + " BACKGROUND " + j);
                System.out.println(ANSI_RESET + "normal");
            }
        }
    }
}
