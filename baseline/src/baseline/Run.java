package baseline;

import io.mem0r1es.trank.TRanker;

public class Run {

	public static void main(String[] args) {
		System.out.println(new TRanker("University of Fribourg").entityToTRankedTypes());
	}
}
