package zulu.baseline;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
  *
  * The main file, with the algorithm itself (in the constructor).
  * Proxy settings can be put in the main function.
  */
public class Lstar {
	/**
	 * An Oracle instance, a RemoteOracle for example.
	 */
	Oracle o;
	
	static final int SAMPLING_QUERIES_AMOUNT = 300;
	
	/**
	 *  Contains the membership of the words asked to the teachers 
	 */
	Hashtable<String, Boolean> words;
	
	/**
	 *  False if blue, true for red
	 */
	Hashtable<String, Boolean> prefixes;
	/**
	 *  (each suffix is true, the value doesn't counts)
	 */
	Hashtable<String, Boolean> suffixes;
	
	/**
	 * The last DFA genrated from the observation table.
	 */
	Automaton dfa;
	
	/**
	 * Instantiate L* algorithm from Dana Angluin
	 * @param o An Oracle (Teacher)
	 */
	Lstar(Oracle o){
		
		this.o=o;
		
		prefixes=new Hashtable<String, Boolean>();
		suffixes=new Hashtable<String, Boolean>();
		words=new Hashtable<String, Boolean>();
				
		// adding the empty word
		prefixes.put("", true);
		// adding each letter as a blue prefix
		for(String a:o.getAlphabet()){
			prefixes.put(a,false);
		}
		suffixes.put("",true);
		
		try{
			fillSuffix("");
			//PrintOnScreen.printTable(this);
			
			boolean out=false;
			String ce="";
			while(!out){
				Boolean newPass=false; 
				String incons, unclose;
				// consistency
				while((incons=consistency())!=null){
					addColumnWord(incons);
				}
				// closure
				while((unclose=closure())!=null){
					promote(unclose);
					newPass=true; // closure changed the table
				}
				if(newPass) continue; // consistency must be verified again
				
				ce=simulatedEQ();
				if(ce==null){
					// the automata is correct (and is consistent with counterexamples and membership queries for closure and consistency)
					out=true;
				}
				else{
					for(int i=1;i<=ce.length();i++){
						String p=ce.substring(0, i);
						addRedWord(p);
						for(String a:o.getAlphabet()){
							if(!ce.startsWith(p+a)){
								addBlueWord(p+a);
								fillPrefix(p+a);
							}
						}
					}
				}
			}
		
		}
		catch(ReachedLimitException rle){
			System.out.println("Membership queries limit reached.");
		}
		
		System.out.println("Automata found. "+(dfa.q.size())+" state(s).\n"+o.getCountMQ()+" membership query(ies).");
		//PrintOnScreen.printTable(this);
		o.submit(classify(o.iThinkIFound()));
		
		System.exit(0);
	}
	
	/**
	 * Classifies the test sample
	 * @param testWords A vector of strings to classify
	 * @return "YNNNY..." to say if they are or not in the language
	 */
	private String classify(Vector<String> testWords) {
		String res="";
		for(String w:testWords){
			res+=(dfa.test(w)?"Y":"N");
		}
		return res;
	}

	/**
	 * Use sampling in order to verify the model over several classified words
	 */
	String simulatedEQ() throws ReachedLimitException{
		System.out.println("Sampling");
		// transforming the observation table into an automata
		String alph="";
		for(int i=0;i<o.getAlphabet().size();i++){
			alph+=o.getAlphabet().get(i);
		}
		dfa=new Automaton(alph,prefixes, suffixes, words);
		int dfaLength=dfa.depth();
		System.out.println("Depth: "+dfaLength);
		String ce=null; // Counterexample
		for(int i=0;i<SAMPLING_QUERIES_AMOUNT;i++){
			WordAndClass res=ex(dfaLength+5);
			if(dfa.test(res.w)!=res.cla){
				ce=res.w;
				System.out.println("Counterexample: "+res.w);
				break;
			}
		}
		return ce;
	}
	
	/**
	 * 
	 * @param maxLength
	 * @return A random word and its classification
	 * @throws ReachedLimitException
	 */
	public WordAndClass ex(int maxLength) throws ReachedLimitException{
		//countEx++;
		String s=randomWord(maxLength);
		return new WordAndClass(s, o.membershipQuery(s));
	}
	
	
	/**
	 * Returns a random word over the alphabet
	 * @return a random word
	 */
	private String randomWord(int maxLength){
		String out="";
		int length=new Double(Math.sqrt(Math.random()*Math.pow((maxLength+1),2)-1)).intValue();
		for(int i=0;i<length;i++){
			out+=o.getAlphabet().get(new Double(Math.random()*o.getAlphabet().size()).intValue());
		}
		return out;
	}
	
	/**
	 * Adds a suffix to the observation table and completes the column
	 * @param word
	 * @throws ReachedLimitException
	 */
	private void addColumnWord(String word) throws ReachedLimitException{
		suffixes.put(word, true);
		fillSuffix(word);
	}

	/**
	 * Complete the observation table for a suffix
	 * @param suf
	 * @throws ReachedLimitException
	 */
	private void fillSuffix(String suf) throws ReachedLimitException{
		for(String p:prefixes.keySet()){
			if(words.get(p+suf)==null){
				words.put(p+suf, o.membershipQuery(p+suf));
			}
		}
	}
	
	/**
	 * Complete the observation table for a prefix
	 * @param pref
	 * @throws ReachedLimitException
	 */
	private void fillPrefix(String pref) throws ReachedLimitException{
		for(String s:suffixes.keySet()){
			if(words.get(pref+s)==null){
				words.put(pref+s, o.membershipQuery(pref+s));
			}
		}
	}

	/**
	 * Executes one pass in order to improve the consistency of the model
	 * @return true if the model is consistent, or false
	 */
	String consistency(){
		System.out.println("Consistency");
		for(String rp1:prefixes.keySet()){
			if(prefixes.get(rp1)){
				for(String rp2:prefixes.keySet()){
					if(prefixes.get(rp2)){
						if(rp1.compareTo(rp2)<0){ //for all couples of red prefixes
							Enumeration<String> en=suffixes.keys();
							while(en.hasMoreElements()){
								String s=en.nextElement(); // for all suffixes
								if(sameLineInOT(rp1, rp2)){ //if the line of the 2 red prefixes is the same
									for(String a:o.getAlphabet()){ //for each letter
										if((words.get(rp1+a+s)!=words.get(rp2+a+s))){ // if the concatenation doesn't lead to the same result (membership or not)
											return a+s; // return the suffix that must be added
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null; // the table is consistent
	}
	
	/**
	 * Compare the lines in observation table for prefixes u and v
	 * @param u First prefix
	 * @param v Second prefix
	 * @return true if the lines are identical
	 */
	boolean sameLineInOT(String u, String v){
		for(String s:suffixes.keySet()){
			if(words.get(u+s)!=words.get(v+s))
				return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param word
	 * @throws ReachedLimitException
	 */
	private void addBlueWord(String word)  throws ReachedLimitException{
		if(prefixes.get(word)==null)
			prefixes.put(word,false);
		fillPrefix(word);
	}
	
	/**
	 * 
	 * @param word
	 * @throws ReachedLimitException
	 */
	private void addRedWord(String word) throws ReachedLimitException{
		prefixes.put(word, true);
		fillPrefix(word);
	}

	/**
	 * Execute one pass in order to improve the closure of the model
	 * @return True if the model is close, or False
	 */
	String closure(){
		System.out.println("Closure");
		int halt=1;
		//boolean out=true;
		// for each word s
		Enumeration<String> en=prefixes.keys();
		while(en.hasMoreElements()){
			String bp=en.nextElement();
			if(!prefixes.get(bp)){
				halt=1;
				for(String rp:prefixes.keySet()){
					if(prefixes.get(rp)){
						if(sameLineInOT(bp, rp)){
							halt=0;
							break;
						}
					}
				}
				if(halt==1){
					// the blue prefix becomes red
					return bp;
					//promote(bp);
					//out=false;
				}
			}
		}
		return null; // the table is closed
	}

	/**
	 * String turns from blue to red and addition of prefixes
	 * @param word
	 */
	private void promote(String word) throws ReachedLimitException {
		prefixes.put(word, true);
		//System.out.println("Promotion for "+word);
		for(String a:o.getAlphabet()){
			addBlueWord(word+a);
			//System.out.println("Closure adds word: "+word+a);
			fillPrefix(word+a);
		}
	}
	

	/**
	 * Main method. You can edit proxy settings in the source code here.
	 * @param args problem key
	 */
	public static void main(String[] args){
		if(args.length<1){
			System.out.println("Please create a task on the Zulu website and give the key as a parameter of the program.");
			System.exit(-1);
		}
		// If you want to force the use of a proxy
		/*System.getProperties().put("http.proxyHost", "someProxyURL");
		System.getProperties().put("http.proxyPort", "someProxyPort");
		System.getProperties().put("http.proxyUser", "someUserName");
		System.getProperties().put("http.proxyPassword", "somePassword");*/
		
		new Lstar(new RemoteOracle(Integer.parseInt(args[0])));
		//new Lstar(new TheOracle());
	}
}
