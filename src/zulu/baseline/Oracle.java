package zulu.baseline;
import java.util.Vector;

/**
 * This interface aims at provide the few methods needed in the communication with an Oracle. You can use it for the development of a local Oracle for example, and add queries not yet supported by Zulu.
 */

public interface Oracle {
	Vector<String> alphabet=null;
	
	/**
	 * 
	 * @return The characters of the alphabet recognized by the target DFA
	 */
	Vector<String> getAlphabet();
	
	/**
	 * Execute a membership query
	 * @return True if the string belong to the target DFA, or False
	 */
	boolean membershipQuery(String word) throws ReachedLimitException;
	
	/**
	 * Submit a classification of the test sample
	 * @return The score (correctly classified strings*100/size of test set)
	 */
	public float submit(String r);
	
	/**
	 * Lock the task and get the test sample. Membership queries are not allowed after the use of this method.
	 * @return The test sample
	 */
	public Vector<String> iThinkIFound();

	//int getCountEQ();

	int getCountMQ();
}
