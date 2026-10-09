package models;

import exceptions.ImpossibleContractException;
import utils.Config;

public class Contract {
	private final Integer startMinute;
	private final HaulType haulType;

	public static Contract of(Contract other) {
        return new Contract(other.haulType, other.startMinute);
	}
	private Contract(HaulType haulType, Integer startMinute) {
		this.haulType = haulType;
		this.startMinute = startMinute;
	}
	
	public Integer getStartMinute() {
		return this.startMinute;
	}
	
	public Integer getEndMinute() {
		return this.startMinute + Config.haulTypeDurationMap.get(this.haulType);
	}
	
	public static Builder builder(HaulType haulType) {
		return new Builder(haulType);
	}

    public HaulType getHaulType() {
		return this.haulType;
    }

	public static class Builder {
		private final HaulType haulType;
		private Integer startMinute;
	
		private Builder(HaulType haulType) {
			this.haulType = haulType;
		}
		
		public Builder startMinute(Integer minute) throws ImpossibleContractException {
			if (minute == null) {
				throw new ImpossibleContractException("Start minute cannot be null for contract");
			}
			if (minute < 0 || minute >= 24 * 60) {
				throw new ImpossibleContractException("Start minute for contract cannot be beyound the range [0, " + (24 * 60) + ")");
			}
			if (minute % 10 != 0) {
				throw new ImpossibleContractException("Start minute of any contract should be a multiple of 10");
			}
			this.startMinute = minute;
			return this;
		}
		
		public Contract build() throws ImpossibleContractException {
			if (this.haulType == null) {
				throw new ImpossibleContractException("Missing Haul type for contract");
			}
			if (this.startMinute == null) {
				throw new ImpossibleContractException("Start minute cannot be null for contract");
			}
			int duration = Config.haulTypeDurationMap.get(this.haulType);
			if (this.startMinute + duration > 24 * 60) {
				throw new ImpossibleContractException("Haul type " + this.haulType + " contract cannot start after " + (24 * 60 - duration) + " minute");
			}
            return new Contract(this.haulType, this.startMinute);
		}
	}
}
