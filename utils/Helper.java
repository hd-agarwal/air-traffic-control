package utils;

import models.Contract;
import models.HaulType;
import models.Ramp;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Helper {
    private static final boolean displayLogs = false;

    private static final String BLACK = "\u001B[1;30m";
    private static final String BLUE_BACKGROUND = "\u001B[104m"; //EAM
    private static final String CYAN_BACKGROUND = "\u001B[106m"; // AM
    private static final String YELLOW_BACKGROUND = "\u001B[103m"; // AN
    private static final String RED_BACKGROUND = "\u001B[101m"; // PM
    private static final String ANSI_RESET = "\u001B[0m";

    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";

    private static void printInColor(String text, Integer startMinutes) {
        printInColor(text, startMinutes, false);
    }

    private static void printInColor(String text, Integer startMinutes, boolean onlyTextColor) {
        if (onlyTextColor) {
            if (startMinutes < 6 * 60) {
                System.out.print(BLUE + text + ANSI_RESET);
            } else if (startMinutes < 12 * 60) {
                System.out.print(CYAN + text + ANSI_RESET);
            } else if (startMinutes < 18 * 60) {
                System.out.print(YELLOW + text + ANSI_RESET);
            } else {
                System.out.print(RED + text + ANSI_RESET);
            }
        } else {
            if (startMinutes < 6 * 60) {
                System.out.print(BLUE_BACKGROUND + BLACK + text + ANSI_RESET);
            } else if (startMinutes < 12 * 60) {
                System.out.print(CYAN_BACKGROUND + BLACK + text + ANSI_RESET);
            } else if (startMinutes < 18 * 60) {
                System.out.print(YELLOW_BACKGROUND + BLACK + text + ANSI_RESET);
            } else {
                System.out.print(RED_BACKGROUND + BLACK + text + ANSI_RESET);
            }
        }
    }

    public static void display(List<Ramp> possibility) {
        display(possibility, false);
    }

    public static void display(List<Ramp> possibility, boolean isEssential) {
        if (!isEssential && !displayLogs) {
            return;
        }
        String heading = "Ramp's Haul";
        System.out.print(heading);
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                printInColor("  0" + i + "  ", i * 60);
            } else {
                printInColor("  " + i + "  ", i * 60);

            }
        }
        System.out.println();
        for (int i = 0; i < heading.length(); i++) {
            System.out.print("-");
        }
        for (int i = 0; i <= 23; i++) {
            printInColor("------", i * 60, true);
        }
        System.out.println();
        for (Ramp ramp : possibility) {
            System.out.print(ramp.getHaulType() + "         ");
            ramp.getContracts().sort(Comparator.comparing(Contract::getStartMinute));
            int currMinute = 0;
            for (Contract c : ramp.getContracts()) {
                while (currMinute < c.getStartMinute()) {
                    System.out.print(" ");
                    currMinute += 10;
                }
                printInColor(getContractChipText(c), c.getStartMinute());
				currMinute = c.getEndMinute();
            }
            System.out.println();
        }


    }

    public static String getContractChipText(Contract c) {
        String ans = "[ ";
        ans += getFormattedTime(c.getStartMinute());
        ans += " ";
        if (c.getHaulType() == HaulType.MH) {
            ans += "   ";
        }
        ans += "to";
        ans += " ";
        if (c.getHaulType() == HaulType.MH) {
            ans += "   ";
        }
        ans += getFormattedTime(c.getEndMinute());
        ans += " ]";
        return ans;
    }

	public static String getFormattedTime(Integer minutes) {
		int hour = minutes / 60;
		String ans = "";
		if (hour < 10) {
			ans += "0" + hour;
		} else {
			ans += hour;
		}
		ans += ":";
		int minuteLeft = minutes % 60;
		if (minuteLeft < 10) {
			ans += "0" + minuteLeft;
		} else {
			ans += minuteLeft;
		}
		return ans;
	}

    public static boolean isCompatible(HaulType ramp, HaulType contract) {
        if (ramp == HaulType.SH && contract == HaulType.MH) {
            return false;
        }
        return true;
    }

	public static void clearScreen() {
        if (displayLogs) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

	}

	public static void pause(Scanner sc) {
//		sc.nextLine();
	}
}
