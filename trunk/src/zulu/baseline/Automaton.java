package zulu.baseline;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

	/**
	*
	* A class, dedicated to the implementation of the Lstar algorithm, that produces an automaton from an observation table.
	*/
public class Automaton {
	//[letter][source]
	int[][] transitionTable;
	int initialState=0;
	int[] finalStates;
	String alphabet;
	Vector<String> q;
	
	/**
	 * Creates an empty automata
	 * @param alphabet
	 * @param finalStates
	 */
	public Automaton(String alphabet, int[] finalStates){
		q=new Vector<String>();
		this.alphabet=alphabet;
		transitionTable=new int[alphabet.length()][100];
		finalStates=new int[100];
	}
	
	/**
	 * Creates an automata from an observation table (Lstar)
	 * @param alph The alphabet
	 * @param prefixes The prefixes table
	 * @param suffixes The suffixes table
	 * @param words The words table
	 */
	public Automaton(String alph, Hashtable<String, Boolean> prefixes,
			Hashtable<String, Boolean> suffixes,
			Hashtable<String, Boolean> words) {
		q=new Vector<String>();
		this.alphabet=alph;
		transitionTable=new int[alphabet.length()][prefixes.size()];//maximum
		finalStates=new int[prefixes.size()];//maximum
		
		String[] redPrefixes=new String[prefixes.size()];
		int index=0;
		Enumeration<String> en=prefixes.keys();
		
		while(en.hasMoreElements()){
			redPrefixes[index++]=en.nextElement();
		}
		for(int i=0;i<redPrefixes.length;i++){
			if(prefixes.get(redPrefixes[i])){
				boolean add=true;
				for(int j=i+1;j<redPrefixes.length;j++){
					if(prefixes.get(redPrefixes[j])){
						if(sameLineInOT(redPrefixes[i], redPrefixes[j], prefixes, suffixes, words)){
							add=false;
						}
					}
				}
				if(add){
					q.add(redPrefixes[i]);
					//System.out.println("Ajout de "+redPrefixes[i]);
					finalStates[q.size()-1]=words.get(redPrefixes[i])?1:0;
					if(redPrefixes[i].equals(""))
						initialState=q.size()-1;
				}
			}
		}
		for(int j=0;j<q.size();j++){
			for(int i=0;i<alphabet.length();i++){
				for(int k=0;k<q.size();k++){
					if(sameLineInOT(q.get(k),q.get(j)+alphabet.charAt(i),prefixes,suffixes,words/*redWords, blueWords, redArray, blueArray, columnWords*/)){
						transitionTable[i][j]=k;
					}
				}
			}
		}
		//printTransitionTable();
	}

	private boolean sameLineInOT(String a, String b,
			Hashtable<String, Boolean> prefixes,
			Hashtable<String, Boolean> suffixes,
			Hashtable<String, Boolean> words) {
		for(String s:suffixes.keySet()){
			if(words.get(a+s)!=words.get(b+s))
				return false;
		}
		return true;
	}

	/**
	 * Prints the automata's transition table
	 */
	void printTransitionTable(){
		System.out.print("         ");
		for(int i=0;i<alphabet.length();i++){
			System.out.print(" | "+alphabet.charAt(i));
		}
		System.out.println();
		System.out.println("--------------------------------");
		for(int i=0;i<q.size();i++){
			System.out.print(q.get(i));
			for(int j=q.get(i).length();j<8;j++){
				System.out.print(" ");
			}
			System.out.print((finalStates[i]==1)?"F":"-");
			for(int j=0;j<alphabet.length();j++){
				System.out.print(" | "+transitionTable[j][i]);
			}
			System.out.println();
		}
	}
	
	
	/**
	 * Tests a word over the automata
	 * @param w The word to test
	 * @return True if the word is part of the language of the automata, or false
	 */
	boolean test(String w){
		int state=initialState;
		for(int c=0;c<w.length();c++){
			state=transitionTable[charToIndice(w.charAt(c))][state];
		}
		return finalStates[state]==1;
	}
	
	
	private int charToIndice(char c) {
		for(int i=0;i<alphabet.length();i++){
			if(alphabet.charAt(i)==c){
				return i;
			}
		}
		System.out.println("Erreur fatale - charToIndice: "+c);
		System.exit(-1);
		return -1;
	}

	/**
	 * Automaton depth
	 * @return the automaton depth
	 */
	public int depth(){
		int result=0;
		int[] stateDepth=new int[finalStates.length+1];
		for(int i=0;i<=finalStates.length;i++)
			stateDepth[i]=-1;
		stateDepth[initialState]=0;
		recDepth(initialState, stateDepth);
		for(int i=1;i<=finalStates.length;i++)
			result=Math.max(result, stateDepth[i]);
		return result;
	}

	private void recDepth(int state, int[] stateDepth) {
		int dest;
		for(int i=0;i<alphabet.length();i++){
			dest=transitionTable[i][state];
			if(stateDepth[dest]==-1){
				stateDepth[dest]=stateDepth[state]+1;
				recDepth(dest, stateDepth);
			}
			else{
				if(stateDepth[state]+1 < stateDepth[dest]){
					stateDepth[dest]=stateDepth[state]+1;
					recDepth(dest, stateDepth);
				}
			}
		}
	}

}
