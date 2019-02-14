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

		// DMS File conversion Part

		File DMSFile = new File("D:\\Users\\1418690\\Documents\\ASOVSDMS\\DMS.txt");
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
			if(dmsLineAL.get(dmsQtyPos).equals(""))
				dmsQty = 0;
			else
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

		File ASOFile = new File("D:\\Users\\1418690\\Documents\\ASOVSDMS\\aso_full.txt");
		Scanner asoObj = new Scanner(ASOFile);
		List<String> headingASO;

		// HashMap of ASO Data
		HashMap<String, HashMap<String, Integer>> dataASO = new HashMap<String, HashMap<String, Integer>>();

		// Get the Headings in a headings ArrayList
		String asoLine;
		//asoLine = asoLine.trim();
		//headingASO = (Arrays.asList(asoLine.split(",", -1)));

		// List of ASO-data-in-a-line
		List<String> asoLineAL;

		// Position of data in List
		int asoDepotPos = 0;
		int asoUpcPos = 1;
		int asoCorpPos = 2;
		int asoQtyPos = 4;
		String asoDepot, asoUpc, asoCorp;
		int asoQty;

		// Looping through the DMS Data
		while (asoObj.hasNextLine()) {
			asoLine = asoObj.nextLine();
			asoLine = asoLine.trim();
			asoLineAL = Arrays.asList(asoLine.split("\t", -1));

			// Populating Variables
			asoDepot = asoLineAL.get(asoDepotPos);
			asoUpc = asoLineAL.get(asoUpcPos);
			asoCorp = asoLineAL.get(asoCorpPos);
			asoCorp = asoCorp.substring(asoCorp.length()-5,asoCorp.length()-1);
			asoQty = Integer.parseInt(asoLineAL.get(asoQtyPos));

			// Defining key and value pair for the depot in consideration
			String key = asoUpc + asoCorp;
			int value = asoQty;

			if (dataASO.containsKey(asoDepot)) {
				dataASO.get(asoDepot).put(key, value);
			} else {
				dataASO.put(asoDepot, new HashMap<String, Integer>());
				dataASO.get(asoDepot).put(key, value);
			}
		}
		
		/*//Printing ASODATA
		BufferedWriter writer = new BufferedWriter(new FileWriter("Hash.txt"));
				for(HashMap.Entry<String, HashMap<String, Integer>> chkaso : dataASO.entrySet())
				{
					//System.out.println(chkaso.getKey());
					writer.write(chkaso.getKey());
					writer.append('\n');
					for(HashMap.Entry<String, Integer> inrchk: chkaso.getValue().entrySet())
					{
						System.out.println(inrchk.getKey()+" "+inrchk.getValue());
					}
				}
				writer.close();
		//Printing ASODATA
*/		
		asoObj.close();
		
		//Mapping
		File depotMappingFile = new File("DepotMapping.txt");
		Scanner mappingFileObj = new Scanner(depotMappingFile);
		HashMap<String, String> mapping = new HashMap<String, String>();
		
		while(mappingFileObj.hasNext())
		{
			//System.out.println(mappingFileObj.next());
			String key = mappingFileObj.next(); //Depot ID
			System.out.print(key+" = key, ");
			String value = mappingFileObj.next(); //Depot Letters
			//System.out.println(key+" "+value);
			mapping.put(key, value);
		}
		
		//printing map
		for(HashMap.Entry<String, String> e: mapping.entrySet())
		{
			System.out.println(e.getKey()+" "+e.getValue());
		}
		//printing map
		
		//Comparison
		long mtchRecordCount = 0;
		long mismtchRecordCount = 0;
		long mtchAsoQty = 0; long mismtchAsoQty = 0;
		long mtchDmsQty = 0; long mismtchDmsQty = 0;
		
		//Create mismatch File beforehand
		String mismtchFilePath = "MismatchReport.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(mismtchFilePath));
		
		for(HashMap.Entry<String, HashMap<String, Integer>> hmEntriesDataASO: dataASO.entrySet())
		{
			if(mapping.containsKey(hmEntriesDataASO.getKey()))
			{
				String dptInAso = hmEntriesDataASO.getKey();
				String dptInDms = mapping.get(dptInAso);
				
				HashMap<String, Integer> hmEntriesDms = dataDMS.get(dptInDms);
				
				for(HashMap.Entry<String, Integer> hmEntriesAso: hmEntriesDataASO.getValue().entrySet())
				{
					
					if(hmEntriesDms.containsKey(hmEntriesAso.getKey())) //Matching DMS-Key & ASO-Key condition
					{
						int DMSQTY = hmEntriesDms.get(hmEntriesAso.getKey());
						int ASOQTY = hmEntriesAso.getValue();
						if(DMSQTY==ASOQTY)//DMSQTY==ASOQTY
						{
							mtchDmsQty += DMSQTY;
							mtchAsoQty += ASOQTY;
							mtchRecordCount++;
							continue;
						}
						else
						{
							mismtchDmsQty += DMSQTY;
							mismtchAsoQty += ASOQTY;
							mismtchRecordCount++;
							String mismtchRecord = ""+dptInDms+" "+hmEntriesAso.getKey()+" DMS="+DMSQTY+" & ASO="+ASOQTY;
							
							writer.write(mismtchRecord);
							writer.append('\n');
						}
					}
					else //Not matching DMS-Key & ASO-Key condition
					{
						mismtchRecordCount++;
						String mismtchRecord = ""+dptInDms+" - "+hmEntriesAso.getKey()+" This UPC,corpCode combination from ASO is not present in DMS, with this ASOQTY="+hmEntriesAso.getValue();
						writer.write(mismtchRecord);
						writer.append('\n');
					}
				}
			}
			else
			{
				String mismtchKey = ""+hmEntriesDataASO.getKey()+" - This key(depotId) from ASO is not present in Mapping file";
				writer.write(mismtchKey);
				writer.append('\n');
			}
		}
		System.out.println(mtchAsoQty+" from ASO is matching with DMS Qty = "+mtchDmsQty);
		System.out.println(mismtchAsoQty+" from ASO is not matching with DMS Qty = "+mismtchDmsQty);
		writer.close();
	}

}
