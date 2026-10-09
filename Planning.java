import models.Ramp;
import utils.Helper;

import java.util.*;
class Planning {
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter number of SH ramps:");
			int sh = sc.nextInt();
			System.out.println("Enter number of MH ramps:");
			int mh = sc.nextInt();
			int gap = 0;
			do {
				System.out.println("Enter minimum gap between 2 takeoffs / 2 landings or 1 takeoff and 1 landing (multiple of 10 in minutes)");
				gap = sc.nextInt();
			} while (gap % 10 != 0 || gap < 0);
			List<List<Ramp>> possibilities = Planner.plan(sh, mh, gap, sc);
			for (List<Ramp> possibility: possibilities) {
				Helper.display(possibility, true);
				System.out.println("\nPress enter to view next");
				sc.nextLine();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
