import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.Scanner;

public class Comparison {

	public static void main(String[] args) throws IOException {
		
		// Mapping
		File depotMappingFile = new File("DepotMapping.txt");
		Scanner mappingFileObj = new Scanner(depotMappingFile);
		HashMap<String, String> mapping = new HashMap<String, String>();

		while (mappingFileObj.hasNext()) {
			String key = mappingFileObj.next(); // Depot ID
			String value = mappingFileObj.next(); // Depot Letters
			mapping.put(key, value);
		}
		
		// Create mismatch File beforehand
		String qtyFilePath = "QtyReport.txt";
		BufferedWriter qtyWriter = new BufferedWriter(new FileWriter(qtyFilePath));
		qtyWriter.write("DEPO DMSQTY ASOQTY");
		qtyWriter.append('\n');
		HashMap<String, ArrayList<Long>> qtyHM = new HashMap<String, ArrayList<Long>>();

		// DMS File conversion Part

		File DMSFile = new File("D:\\Users\\1418690\\Documents\\1502 work\\DMS.txt");
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
			if (dmsLineAL.get(dmsQtyPos).equals(""))
				dmsQty = 0;
			else
				dmsQty = Integer.parseInt(dmsLineAL.get(dmsQtyPos));

			// Defining key and value pair for the depot in consideration
			String key = dmsUpc + dmsCorp;
			if(key.length()!=12)
			{
				System.out.println(key+" - "+"Make sure the UPC is of 8 digits along with the '0's and CORP code is of 4 digits");
			}
			int value = dmsQty;

			if (dataDMS.containsKey(dmsDepot)) {
				dataDMS.get(dmsDepot).put(key, value);
				qtyHM.get(dmsDepot).set(0, qtyHM.get(dmsDepot).get(0)+value);
			} else {
				dataDMS.put(dmsDepot, new HashMap<String, Integer>());
				dataDMS.get(dmsDepot).put(key, value);
				qtyHM.put(dmsDepot, new ArrayList<Long>());
				qtyHM.get(dmsDepot).add(0L);
				qtyHM.get(dmsDepot).add(0L);
				qtyHM.get(dmsDepot).set(0, qtyHM.get(dmsDepot).get(0)+value);
			}
		}
		

		dmsObj.close();

		// ASO File Conversion Part
		File ASOFile = new File("D:\\Users\\1418690\\Documents\\1502 work\\ASO_data_1502 (4)\\ASO_data_1502.txt");
		Scanner asoObj = new Scanner(ASOFile);
		String asoLine;
		List<String> headingASO;

		// HashMap of ASO Data
		HashMap<String, HashMap<String, Integer>> dataASO = new HashMap<String, HashMap<String, Integer>>();

		// Get the Headings in a headings ArrayList
		asoLine = asoObj.nextLine();
		asoLine = asoLine.trim();
		headingASO = (Arrays.asList(asoLine.split("\t", -1)));
		
		// List of ASO-data-in-a-line
		List<String> asoLineAL;

		// Position of data in List
		int asoDepotPos = 0;
		int asoUpcPos = 1;
		int asoCorpPos = 2;
		int asoQtyPos = 4;
		String asoDepot, asoUpc, asoCorp;
		int asoQty;
		

		// Looping through the ASO Data
		int ct = 0;
		while (asoObj.hasNextLine()) {
			asoLine = asoObj.nextLine();
			asoLine = asoLine.trim();
			
			//check
			ct++;
			//check
			
			asoLineAL = Arrays.asList(asoLine.split("\t", -1));

			//Populating Variables
			asoDepot = asoLineAL.get(asoDepotPos);
			asoUpc = asoLineAL.get(asoUpcPos);
			asoCorp = asoLineAL.get(asoCorpPos);
			asoCorp = asoCorp.substring(asoCorp.length() - 5, asoCorp.length() - 1);
			if (asoLineAL.get(asoQtyPos).equals(""))
				asoQty = 0;
			else
				asoQty = Integer.parseInt(asoLineAL.get(asoQtyPos));
			
			// Defining key and value pair for the depot in consideration
			String key = asoUpc + asoCorp;
			if(key.length()!=12)
				System.out.println(key);
			int value = asoQty;

		
			if (dataASO.containsKey(asoDepot)) {
				dataASO.get(asoDepot).put(key, value);
				qtyHM.get(mapping.get(asoDepot)).set(1, qtyHM.get(mapping.get(asoDepot)).get(1)+value);
			} else {
				dataASO.put(asoDepot, new HashMap<String, Integer>());
				dataASO.get(asoDepot).put(key, value);
				qtyHM.get(mapping.get(asoDepot)).set(1, qtyHM.get(mapping.get(asoDepot)).get(1)+value);
			}
			
		}
		
		asoObj.close();

		// Comparison
		long mtchRecordCount = 0;
		long mismtchRecordCount = 0;
		long mtchAsoQty = 0;
		long mismtchAsoQty = 0;
		long mtchDmsQty = 0;
		long mismtchDmsQty = 0;
		long AsoSum = 0;
		long DmsSum = 0;
		long temp = 0;
		long temp1 = 0;

		// Create mismatch File beforehand
		String mismtchFilePath = "MismatchReport.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(mismtchFilePath));
		writer.write("DEPO UPC CORP DMSQTY ASOQTY");
		writer.append('\n');
		
		// Create mismatch File beforehand
		String dmsNotPresentFilePath = "DMSNotHavingReport.txt";
		BufferedWriter dmsWriter = new BufferedWriter(new FileWriter(dmsNotPresentFilePath));
		dmsWriter.write("DEPO UPC CORP ASOQTY STATUS");
		dmsWriter.append('\n');

		// Matching-Mismatching for the depots
		HashMap<String, ArrayList<Long>> depotAndQty = new HashMap<String, ArrayList<Long>>();
		int c=0;
		for (HashMap.Entry<String, HashMap<String, Integer>> hmEntriesDataASO : dataASO.entrySet()) {
			
			if (mapping.containsKey(hmEntriesDataASO.getKey())) {
				String dptInAso = hmEntriesDataASO.getKey(); // 5012068051738,
																// 5012068052124,
																// 5012068052810...
				String dptInDms = mapping.get(dptInAso); // ASO Depot ID
															// converted to
															// BB,BD,DV...

				HashMap<String, Integer> hmEntriesDms = dataDMS.get(dptInDms);
				depotAndQty.put(dptInDms, new ArrayList<Long>()); // Depot(Key)
																	// and
																	// ArrayList<MatchingQty,
																	// MisMatchingQty
																	// from ASO,
																	// MisMatchingQty
																	// from DMS>

				depotAndQty.get(dptInDms).add(0L); // Initialising for matching
													// Qty
				depotAndQty.get(dptInDms).add(0L); // Initialising for
													// misMatching Qty from ASO
				depotAndQty.get(dptInDms).add(0L); // Initialising for
													// misMatching Qty from DMS

				
				
				for (HashMap.Entry<String, Integer> hmEntriesAso : hmEntriesDataASO.getValue().entrySet()) {

					if (hmEntriesDms.containsKey(hmEntriesAso.getKey())) // Matching
																			// DMS-Key
																			// &
																			// ASO-Key
																			// condition
					{
						int DMSQTY = hmEntriesDms.get(hmEntriesAso.getKey());
						int ASOQTY = hmEntriesAso.getValue();
						DmsSum += DMSQTY;
						AsoSum += ASOQTY;
						if (DMSQTY == ASOQTY)// DMSQTY==ASOQTY
						{
							mtchDmsQty += DMSQTY;
							mtchAsoQty += ASOQTY;
							mtchRecordCount++;
							temp = depotAndQty.get(dptInDms).get(0);
							depotAndQty.get(dptInDms).set(0, temp + ASOQTY); // setting
																				// Matching
																				// sum
																				// Qty
							continue;
						} else {
							mismtchDmsQty += DMSQTY;
							mismtchAsoQty += ASOQTY;
							mismtchRecordCount++;

							temp = depotAndQty.get(dptInDms).get(1);
							depotAndQty.get(dptInDms).set(1, temp + ASOQTY); // setting
																				// Qty
																				// from
																				// ASO

							temp1 = depotAndQty.get(dptInDms).get(2);
							depotAndQty.get(dptInDms).set(2, temp1 + DMSQTY); // setting
																				// Qty
																				// from
																				// DMS

							String mismtchRecord = "" + dptInDms + " " + hmEntriesAso.getKey().substring(0, 8)
									+ " " + hmEntriesAso.getKey().substring(8, 12) + " " + DMSQTY + " "
									+ ASOQTY;

							writer.write(mismtchRecord);
							writer.append('\n');
						}
					} else // Not matching DMS-Key & ASO-Key condition
					{
						mismtchRecordCount++;
						String mismtchRecord = "" + dptInDms + " " + hmEntriesAso.getKey().substring(0, 8)
								+ " " + hmEntriesAso.getKey().substring(8, 12)
								+ " "
								+ hmEntriesAso.getValue() + " Not_present_in_DMS_but_in_ASO";
						dmsWriter.write(mismtchRecord);
						dmsWriter.append('\n');
					}
				}
			} else {
				String mismtchKey = "" + hmEntriesDataASO.getKey()
						+ " - This key(depotId) from ASO is not present in Mapping file";
				writer.write(mismtchKey);
				writer.append('\n');
			}
		}
		System.out.println(mtchAsoQty + " from ASO is matching with DMS Qty = " + mtchDmsQty);
		System.out.println(mismtchAsoQty + " from ASO is not matching with DMS Qty = " + mismtchDmsQty);
		System.out.println("ASO-SUM = " + AsoSum);
		System.out.println("DMS-SUM = " + DmsSum);
		for (HashMap.Entry<String, ArrayList<Long>> daq : depotAndQty.entrySet()) {
			System.out.print(daq.getKey() + " - Matching=" + daq.getValue().get(0));
			if (daq.getValue().get(1) != 0)
				System.out.print(", Qty in ASO =" + daq.getValue().get(1));
			if (daq.getValue().get(2) != 0)
				System.out.print(", Qty in DMS =" + daq.getValue().get(2));
			System.out.println();
		}
		for(HashMap.Entry<String, ArrayList<Long>> hm: qtyHM.entrySet())
		{
			qtyWriter.write(""+hm.getKey()+" "+hm.getValue().get(0)+" "+hm.getValue().get(1));
			qtyWriter.append('\n');
		}
		qtyWriter.close();
		writer.close();
		dmsWriter.close();
	}
	
}
