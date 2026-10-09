import exceptions.ImpossibleContractException;
import models.Contract;
import models.HaulType;
import models.Ramp;
import utils.Config;
import utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Planner {
    private static Integer maxContractsFitted;
    private static Integer possibilitiesExplored;
    private static Integer possibilitiesCancelled;

	public static List<List<Ramp>> plan(Integer sh, Integer mh, Integer gap, Scanner sc) {
        List<List<Ramp>> possibilities;
        possibilities = new ArrayList<>();
        List<Ramp> ramps = new ArrayList<>();
        for (int i = 0; i < mh; i++) {
            ramps.add(new Ramp(HaulType.MH));
        }
        for (int i = 0; i < sh; i++) {
            ramps.add(new Ramp(HaulType.SH));
        }
        maxContractsFitted = 0;
        possibilitiesExplored = 0;
        possibilitiesCancelled = 0;
        if (mh == 0 && sh == 0) {
            return possibilities;
        }
        Helper.clearScreen();
        Helper.display(ramps);
        try {
//				ramps.getFirst().addContract(Contract.builder(ramps.get(0).getHaulType()).startMinute(i).build());
            recursivelyPlan(ramps, gap, possibilities, 0, 0, sc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return possibilities;
    }

    private static void recursivelyPlan(List<Ramp> ramps, Integer gap, List<List<Ramp>> possibilities, Integer contractsPlaced, Integer rampNumber, Scanner sc) {
        if (rampNumber >= ramps.size()) {
            // successful configuration
            possibilitiesExplored++;
            if (contractsPlaced > maxContractsFitted) {
//                Helper.clearScreen();
//                System.out.println("New config with maxContracts = " + contractsPlaced + " found");
                possibilities.clear();
                List<Ramp> curr = new ArrayList<>();
                for (Ramp ramp : ramps) {
                    curr.add(Ramp.of(ramp));
                }
                possibilities.add(curr);
                maxContractsFitted = contractsPlaced;
//                Helper.display(curr, true);

            }
//            if (contractsPlaced >= maxContractsFitted) {
//                List<Ramp> curr = new ArrayList<>();
//                for (Ramp ramp : ramps) {
//                    curr.add(Ramp.of(ramp));
//                }
//                possibilities.add(curr);
//                maxContractsFitted = contractsPlaced;
//            }
            return;
        }
        Ramp currRamp = ramps.get(rampNumber);

//        Integer spaceLeftOnCurrRamp = 24 * 60 - (!currRamp.getContracts().isEmpty() ? currRamp.getContracts().getLast().getEndMinute() + gap : 0);
//        Integer spaceTakenByOneOnCurrentRamp = Config.haulTypeDurationMap.get(currRamp.getHaulType()) + gap;
//        Integer maxContractsOnCurrent = spaceLeftOnCurrRamp / spaceTakenByOneOnCurrentRamp;
//        Integer maxContractsOnNextRampsCombined = 0;
//        for (int i = rampNumber + 1; i < ramps.size(); i++) {
//            Integer spaceTakenByOneOnRamp = Config.haulTypeDurationMap.get(ramps.get(i).getHaulType()) + gap;
//            maxContractsOnNextRampsCombined += (24 * 60) / spaceTakenByOneOnRamp;
//        }
//        if (maxContractsOnCurrent + maxContractsOnNextRampsCombined + contractsPlaced <= maxContractsFitted) {
//            return;
//        }


        Contract toAdd;
        Integer endTimeLast = 0;
        if (currRamp.getContracts().size() > 0) {
            Contract last = currRamp.getContracts().getLast();
            endTimeLast = last.getEndMinute();
        }
        boolean isAddedToCurrentRamp = false;

        for (int i = endTimeLast; i < 24 * 60; i += 10) {
            if (!isGreaterContractCountPossible(ramps, rampNumber, contractsPlaced, gap)) {
                isAddedToCurrentRamp = true;
                break;
            }
            try {
                toAdd = Contract.builder(currRamp.getHaulType()).startMinute(i).build();
            } catch (ImpossibleContractException e) {
                continue;
            }

//            Helper.clearScreen();
//            Helper.display(ramps);
            Helper.clearScreen();
            if (currRamp.canAddContract(toAdd, gap) && satisfiesAllOtherRamps(toAdd, ramps, gap, rampNumber)) {
                isAddedToCurrentRamp = true;
                currRamp.addContract(toAdd);
                Helper.display(ramps);
//                System.out.println("\nChecked contract " + Helper.getContractChipText(toAdd) + " on ramp " + rampNumber);
//                System.out.println("\nContract added");
                Helper.pause(sc);
                recursivelyPlan(ramps, gap, possibilities, contractsPlaced + 1, rampNumber, sc);
                Helper.clearScreen();
                Helper.display(ramps);
//                System.out.println("\nBacktracking now");
                Helper.pause(sc);
                currRamp.removeLastContract();
                Helper.clearScreen();
                Helper.display(ramps);
//                System.out.println("\nRemoved contract " + Helper.getContractChipText(toAdd) + " from ramp " + rampNumber);
                Helper.pause(sc);
            } else {
                Helper.display(ramps);
//                System.out.println("\nChecked contract " + Helper.getContractChipText(toAdd) + " on ramp " + rampNumber);
//                System.out.println("That contract couldnt be added");
                Helper.pause(sc);
            }
        }
        if (!isAddedToCurrentRamp) {
            recursivelyPlan(ramps, gap, possibilities, contractsPlaced, rampNumber + 1, sc);
        }

    }

    private static boolean satisfiesAllOtherRamps(Contract toAdd, List<Ramp> ramps, Integer gap, Integer currRampNum) {
        Integer contractStart = toAdd.getStartMinute();
        Integer contractEnd = toAdd.getEndMinute();
        List<Integer[]> rangesToCheck = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            int startRangeStart = contractStart + (i * 24 * 60) - gap;
            int startRangeEnd = contractStart + (i * 24 * 60) + gap;
            Integer[] r1 = new Integer[2];
            r1[0] = startRangeStart;
            r1[1] = startRangeEnd;
            int endRangeStart = contractEnd + (i * 24 * 60) - gap;
            int endRangeEnd = contractEnd + (i * 24 * 60) + gap;
            Integer[] r2 = new Integer[2];
            r2[0] = endRangeStart;
            r2[1] = endRangeEnd;
            rangesToCheck.add(r1);
            rangesToCheck.add(r2);
        }
        for (int i = 0; i < ramps.size(); i++) {
            if (i == currRampNum) {
                continue;
            }
            for (Contract c : ramps.get(i).getContracts()) {
                for (Integer[] range : rangesToCheck) {
                    if (c.getStartMinute() > range[0] && c.getStartMinute() < range[1]) {
                        return false;
                    }
                    if (c.getEndMinute() > range[0] && c.getEndMinute() < range[1]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isGreaterContractCountPossible(List<Ramp> ramps, Integer rampNumber, Integer contractsPlaced, Integer gap) {
        Ramp currRamp = ramps.get(rampNumber);

        Integer spaceLeftOnCurrRamp = 24 * 60 - (!currRamp.getContracts().isEmpty() ? currRamp.getContracts().getLast().getEndMinute() + gap : 0);
        Integer spaceTakenByOneOnCurrentRamp = Config.haulTypeDurationMap.get(currRamp.getHaulType()) + gap;
        Integer maxContractsOnCurrent = spaceLeftOnCurrRamp / spaceTakenByOneOnCurrentRamp;
        Integer maxContractsOnNextRampsCombined = 0;
        for (int i = rampNumber + 1; i < ramps.size(); i++) {
            Integer spaceTakenByOneOnRamp = Config.haulTypeDurationMap.get(ramps.get(i).getHaulType()) + gap;
            maxContractsOnNextRampsCombined += (24 * 60) / spaceTakenByOneOnRamp;
        }
        if (maxContractsOnCurrent + maxContractsOnNextRampsCombined + contractsPlaced <= maxContractsFitted) {
            return false;
        }
        return true;
    }

}

