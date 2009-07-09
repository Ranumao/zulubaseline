package zulu.baseline;

/**
 * This exception is raised when the client asks for the membership of a string in the DFA but it has already used every queries it was allowed.
	You can know how many queries you used at the task page : <a href="http://cian.univ-st-etienne.fr/zulu/problems.php">http://cian.univ-st-etienne.fr/zulu/problems.php</a>
 * @author David
 *
 */
public class ReachedLimitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3111490380852497805L;

}
