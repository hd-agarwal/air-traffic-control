package utils;

import models.HaulType;

import java.util.Map;

import static java.util.Map.entry;

public class Config {
	public static final Map<HaulType, Integer> haulTypeDurationMap = Map.ofEntries(
		entry(HaulType.SH, 3 * 60),
		entry(HaulType.MH, 4 * 60)
	);
}
