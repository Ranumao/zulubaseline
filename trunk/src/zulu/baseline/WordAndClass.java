package zulu.baseline;

/**
 * A convenience class made to gather a string and its classification.
 * @author David
 *
 */
public class WordAndClass {
	String w;
	boolean cla;
	
	/**
	 * Creates a couple string/classification.
	 * @param word
	 * @param cla
	 */
	public WordAndClass(String word, boolean cla) {
		this.w=word;
		this.cla=cla;
	}
	
}
