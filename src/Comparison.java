import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Arrays;
import java.util.Scanner;

public class Comparison {

	public static void main(String[] args) throws FileNotFoundException {

		// DMS File conversion Part

		File DMSFile = new File("C:\\Users\\Apoorv\\Downloads\\Office\\ASO.txt");
		Scanner dmsObj = new Scanner(DMSFile);
		List<String> headingDMS;

		// HashMap of DMS Data
		HashMap<String, HashMap<String, Integer>> dataDMS = new HashMap<String, HashMap<String, Integer>>();

		// Get the Headings in a headings ArrayList
		String dmsLine = dmsObj.nextLine();
		dmsLine = dmsLine.trim();
		headingDMS = (Arrays.asList(dmsLine.split(",", -1)));

		// List of DMS-data-in-a-line
		List<String> dmsLineAL;

		// Position of data in List
		int dmsDepotPos = 0;
		int dmsUpcPos = 1;
		int dmsCorpPos = 4;
		int dmsQtyPos = 5;
		String dmsDepot, dmsUpc, dmsCorp;
		int dmsQty;

		// Looping through the DMS Data
		while (dmsObj.hasNextLine()) {
			dmsLine = dmsObj.nextLine();
			dmsLine = dmsLine.trim();
			dmsLineAL = Arrays.asList(dmsLine.split(",", -1));

			// Populating Variables
			dmsDepot = dmsLineAL.get(dmsDepotPos);
			dmsUpc = dmsLineAL.get(dmsUpcPos);
			dmsCorp = dmsLineAL.get(dmsCorpPos);
			dmsQty = Integer.parseInt(dmsLineAL.get(dmsQtyPos));

			// Defining key and value pair for the depot in consideration
			String key = dmsUpc + dmsCorp;
			int value = dmsQty;

			if (dataDMS.containsKey(dmsDepot)) {
				dataDMS.get(dmsDepot).put(key, value);
			} else {
				dataDMS.put(dmsDepot, new HashMap<String, Integer>());
				dataDMS.get(dmsDepot).put(key, value);
			}
		}
		dmsObj.close();

		// ASO File Conversion Part

		File ASOFile = new File("C:\\Users\\Apoorv\\Downloads\\Office\\ASO.txt");
		Scanner asoObj = new Scanner(ASOFile);
		List<String> headingASO;

		// HashMap of DMS Data
		HashMap<String, HashMap<String, Integer>> dataASO = new HashMap<String, HashMap<String, Integer>>();

		// Get the Headings in a headings ArrayList
		String asoLine = asoObj.nextLine();
		asoLine = asoLine.trim();
		headingDMS = (Arrays.asList(asoLine.split(",", -1)));

		// List of DMS-data-in-a-line
		List<String> asoLineAL;

		// Position of data in List
		int asoDepotPos = 0;
		int asoUpcPos = 1;
		int asoCorpPos = 4;
		int asoQtyPos = 5;
		String asoDepot, asoUpc, asoCorp;
		int asoQty;

	}

}
