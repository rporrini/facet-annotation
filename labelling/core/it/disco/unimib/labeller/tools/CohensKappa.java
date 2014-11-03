package it.disco.unimib.labeller.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class CohensKappa {
 
	public double kappa(List<List<Integer>> scores) {
		
		Map<Integer, Map<Integer, Integer>> table = new HashMap<Integer, Map<Integer, Integer>>();
 
		int tableSize = 0;
 
		for (int i = 0; i < scores.get(0).size(); i++) {
			int first = scores.get(0).get(i);
			int second = scores.get(1).get(i);
 
			if (!table.containsKey(first)) {
				table.put(first, new HashMap<Integer, Integer>());
			}
 
			if (!table.get(first).containsKey(second)) {
				table.get(first).put(second, 0);
			}
 
			table.get(first).put(second, table.get(first).get(second) + 1);
 
			if (first > tableSize) {
				tableSize = first + 1;
			}
 
			if (second > tableSize) {
				tableSize = second + 1;
			}
		}
 
		boolean DEBUG = false;
		if (DEBUG) {
			System.out.println(table);
		}
 
 
		Map<Integer, Integer> sumRows = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sumCols = new HashMap<Integer, Integer>();
 
		for (Map.Entry<Integer, Map<Integer, Integer>> entry : table.entrySet()) {
			int rowNumber = entry.getKey();
			int sumRow = 0;
 
			for (Map.Entry<Integer, Integer> rowEntry : entry.getValue().entrySet()) {
				int colNumber = rowEntry.getKey();
				int value = rowEntry.getValue();
 
				sumRow += value;
 
				if (!sumCols.containsKey(colNumber)) {
					sumCols.put(colNumber, 0);
				}
 
				sumCols.put(colNumber, sumCols.get(colNumber) + value);
			}
 
			sumRows.put(rowNumber, sumRow);
		}
		int sumTotal = 0;
		for (Integer rowSum : sumRows.values()) {
			sumTotal += rowSum;
		}
 
		if (DEBUG) {
			System.out.println("Row sums: " + sumRows);
			System.out.println("Col sums: " + sumCols);
			System.out.println("Total: " + sumTotal);
			System.out.println("table size: " + tableSize);
		}
 
		int sumDiagonal = 0;
		for (int i = 1; i <= tableSize; i++) {
			int value = 0;
			if (table.containsKey(i) && table.get(i).containsKey(i)) {
				value = table.get(i).get(i);
			}
 
			sumDiagonal += value;
		}
		double p = (double) sumDiagonal / (double) sumTotal;
		double peSum = 0;
		for (int i = 1; i <= tableSize; i++) {
			if (sumRows.containsKey(i) && sumCols.containsKey(i)) {
				peSum += (double) sumRows.get(i) * (double) sumCols.get(i);
			}
		}
		double pe = peSum / (sumTotal * sumTotal);
		return (p - pe) / (1.0d - pe);
	}
}
