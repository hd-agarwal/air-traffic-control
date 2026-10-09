package models;

import utils.Helper;

import java.util.*;
public class Ramp {
	private final HaulType haulType;
	private final List<Contract> contracts;

	public static Ramp of(Ramp other) {
		Ramp obj = new Ramp(other.getHaulType());
		for (int i = 0; i < other.contracts.size(); i++) {
			obj.contracts.add(Contract.of(other.contracts.get(i)));
		}
		return obj;
	}

	public Ramp(HaulType haulType) {
		this.haulType = haulType;
		this.contracts = new ArrayList<>();
	}

	public boolean removeLastContract() {
		if (this.contracts.isEmpty()) {
			return false;
		}
		this.contracts.removeLast();
		return true;
	}
	
	public boolean addContract(Contract contract, Integer gap) {
		if (this.canAddContract(contract, gap)) {
			this.contracts.add(contract);
			return true;
		}
		return false;
	}

	public boolean canAddContract(Contract contract, Integer gap) {
		if (!Helper.isCompatible(this.haulType, contract.getHaulType())) {
			return false;
//			throw new IllegalArgumentException("Contract with haul type: " + contract.getHaulType() + " cannot be placed on ramp with haul type: " + this.haulType);
		}
		Integer startMinute = contract.getStartMinute();
		Integer endMinute = contract.getEndMinute();
		if (endMinute > 24 * 60 || endMinute < 0 || startMinute < 0 || startMinute > 24 * 60) {
			return false;
//			throw new IllegalArgumentException("Contract should lie within the day's minutes");
		}
		for (Contract existing: this.contracts) {
			if (
					(startMinute >= existing.getStartMinute() && startMinute < existing.getEndMinute()) ||
							(endMinute > existing.getStartMinute() && endMinute <= existing.getEndMinute())
			) {
				return false;
//				throw new IllegalArgumentException("Contract cannot fit as it overlaps with existing contracts on this ramp");
			}
			if (startMinute >= existing.getEndMinute() && (startMinute - existing.getEndMinute() < gap || (24 * 60 - endMinute + existing.getStartMinute() < gap))) {
				return false;
//				throw new IllegalArgumentException("Cannot add contract to ramp as its gap with an existing contract is less than " + gap + " minutes");
			}
			if (endMinute <= existing.getStartMinute() && (existing.getStartMinute() - endMinute < gap || (24 * 60 - existing.getEndMinute() + startMinute < gap))) {
				return false;
//				throw new IllegalArgumentException("Cannot add contract to ramp as its gap with an existing contract is less than " + gap + " minutes");
			}

		}
		return true;
	}
	
	public boolean addContract(Contract contract) {
		return this.addContract(contract, 0);
	}

    public HaulType getHaulType() {
        return this.haulType;
    }

	public List<Contract> getContracts() {
		List<Contract> toReturn = new ArrayList<>();
		for (Contract c: this.contracts) {
			toReturn.add(Contract.of(c));
		}
		return toReturn;
	}
}
