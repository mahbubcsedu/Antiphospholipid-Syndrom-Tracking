package com.emmes.aps.medication.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MedicationContent {

	public static List<MedItem> ITEMS = new ArrayList<MedItem>();
	public static Map<String, MedItem> ITEM_MAP = new HashMap<String, MedItem>();

	static {
		// Add 3 sample items.
		//addItem(new MedItem("1", "Med  1", "Once", "Daily"));
		//addItem(new MedItem("2", "Med  2", "Twice", "Daily"));
		//addItem(new MedItem("3", "Med  3", "Thrice", "Daily"));
		addItem(new MedItem("1", "Med  1"));
		addItem(new MedItem("2", "Med  2"));
		addItem(new MedItem("3", "Med  3"));
	}

	private static void addItem(MedItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static class MedItem {
		public String id;
		public String name;
		//public String dose;
		//public String freq;

		public MedItem(String id, String name
				//, String dose, String freq
				) {
			this.id = id;
			this.name = name;
			//this.dose = dose;
			//this.freq = freq;
		}
	}

}
