package zulu.baseline;

/**
 * Convenience class you can use in order to understand and/or edit the L* algorithm.
 * @author David
 *
 */
public class PrintOnScreen {
	/**
	 * Print the current observation table.
	 * @param currentAlgorithm The Lstar instance.
	 */
	public static void printTable(Lstar currentAlgorithm){
		System.out.println("<-----Table printing----->");
		// show headline
		System.out.print("          ");
		for(String s:currentAlgorithm.suffixes.keySet()){
			if(s.equals(""))
				System.out.print("£"+"    ");
			else System.out.print(s+"    ");
		}
		System.out.println();
		// print line
		for(int i=0;i<9+5*currentAlgorithm.suffixes.size();i++){
			System.out.print("-");
		}
		System.out.println();
		
		// print red table
		for(String p:currentAlgorithm.prefixes.keySet()){
			if(currentAlgorithm.prefixes.get(p)){
				System.out.print(p);
				// inlining
				for(int i=9;i>p.length();i--){
					System.out.print(" ");
				}
				System.out.print("|");
				for(String s:currentAlgorithm.suffixes.keySet()){
					System.out.print((currentAlgorithm.words.get(p+s)?1:0)+"    ");
				}
				System.out.println();
			}
		}
		// print line
		for(int i=0;i<9+5*currentAlgorithm.suffixes.size();i++){
			System.out.print("-");
		}
		System.out.println();
		// print blue table
		for(String p:currentAlgorithm.prefixes.keySet()){
			if(!currentAlgorithm.prefixes.get(p)){
				System.out.print(p);
				for(int i=9;i>p.length();i--){
					System.out.print(" ");
				}
				System.out.print("|");
				for(String s:currentAlgorithm.suffixes.keySet()){
					System.out.print((currentAlgorithm.words.get(p+s)?1:0)+"    ");
				}
				System.out.println();
			}
		}
		System.out.println("<-----End of table printing----->");
	}
}
