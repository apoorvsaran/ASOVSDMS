import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Arrays;
import java.util.Scanner;

public class Comparison {

	public static void main(String[] args) throws FileNotFoundException {
		
		File f = new File("C:\\Users\\Apoorv\\Downloads\\Office\\ASO.txt");
		
		int upcPos=1;
		int corpPos=4;
		int qtyPos=5;
		
		Scanner dmsObj = new Scanner(f);
		List<String> headings;
		ArrayList<List<String>> dataDMS = new ArrayList<List<String>>();
		
		String s = dmsObj.nextLine();
		headings = (Arrays.asList(s.split(",")));
		int l = 0;
		
		while(dmsObj.hasNextLine())
		{
			s = dmsObj.nextLine();
			s = s.trim();
			l = s.length();
			dataDMS.add(Arrays.asList(s.split(",", -1)));
		}
		
		HashMap<String, Integer> hmDMS = new HashMap<String, Integer>();
		for(List<String> g: dataDMS)
		{
			if(g.get(0).equalsIgnoreCase("BR"))
			{
				String d = ""+Integer.parseInt(g.get(upcPos)) + g.get(corpPos);
				if(g.get(qtyPos).equals(""))
				{
					hmDMS.put(d, 0);
					System.out.println(d+" "+0);
				}
				else
				{
					hmDMS.put(d, Integer.parseInt(g.get(qtyPos)));
					System.out.println(d+" "+Integer.parseInt(g.get(qtyPos)));
				}
			}
		}
		
		File ASOFile = new File("C:\\Users\\Apoorv\\Downloads\\Office\\ASO.txt");
		Scanner asoObj = new Scanner(ASOFile);
		
		//First Line Headings
		List<String> headingASO;
		
		//
		ArrayList<List<String>> dataASO = new ArrayList<List<String>>();
		
		//HashMap
		HashMap<String, Integer> hmASO = new HashMap<String, Integer>();
		
		String asoLine = asoObj.nextLine();
		headingASO = Arrays.asList(asoLine.split(",",-1));
		
		while(asoObj.hasNextLine())
		{
			String st = asoObj.nextLine();
			st = st.trim();
			dataASO.add(Arrays.asList(st.split(",",-1)));
		}
		
		
		for(List<String> ls: dataASO)
		{
			String temp = ls.get(destPosASO);
			int len = temp.length();
			String corpCode = temp.substring(len-5, len-1);
			String key = ls.get(upcPosASO) +  corpCode;
			hmASO.put(key, ls.get(qtyPosASO));
		}

	}

}
