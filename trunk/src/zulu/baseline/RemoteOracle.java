package zulu.baseline;
import java.io.*;
import java.net.*;
import java.util.Vector;

	/**
	*
	* The communication interface with the server of the project. It is a child of the Oracle interface.
	*/

public class RemoteOracle implements Oracle{
	int countMQ=0;
	String alphabet=null;
	int key=1;
	
	String absoluteBaseURL="http://labh-curien.univ-st-etienne.fr/zulu";
	
	RemoteOracle(int key){
		this.key=key;
		
		try {
			URL theUrl=new URL(absoluteBaseURL+"/oracle.php?alphabet=1&problem="+key);
			BufferedReader in = new BufferedReader(new InputStreamReader(theUrl.openStream()));
			alphabet = in.readLine();
			in.close();
		} catch (MalformedURLException e) {
			System.err.println("Problem with URL");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Problem with alphabet aquisition: "+absoluteBaseURL+"/oracle.php?alphabet=1&problem="+key);
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Alphabet: "+alphabet);
	}
	
	
	@Override
	public Vector<String> getAlphabet() {
		Vector<String> alph=new Vector<String>();
		for(int i=0;i<alphabet.length();i++){
			alph.add(alphabet.charAt(i)+"");
		}
		return alph;
	}

	@Override
	public Vector<String> iThinkIFound() {
		Vector<String> samplingWords=new Vector<String>();
		URL theUrl;
		try {
			theUrl=new URL(absoluteBaseURL+"/oracle.php?iThinkIFound=1&problem="+key);
			BufferedReader in = new BufferedReader(new InputStreamReader(theUrl.openStream()));
			    String inputLine;
			    while((inputLine = in.readLine())!=null){
			    	samplingWords.add(inputLine.trim());
			    }
			    in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(samplingWords.size()+" Ex words");
		return samplingWords;
	}

	@Override
	public boolean membershipQuery(String word) throws ReachedLimitException{
		String firstLine=null;
		
		try{
			URL theUrl=new URL(absoluteBaseURL+"/oracle.php?problem="+key+"&word="+word);
			BufferedReader in = new BufferedReader(new InputStreamReader(theUrl.openStream()));
			    firstLine = in.readLine();
			    in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(firstLine);
		if(firstLine.startsWith("Yes")){
			countMQ++;
			return true;
		}
		if(firstLine.startsWith("No")){
			countMQ++;
			return false;
		}
		if(firstLine.startsWith("Limit")) throw new ReachedLimitException();
		
		System.out.println("Error: "+firstLine);
		System.exit(0);
		return false;
	}


	@Override
	public float submit(String r) {
		URL theUrl;
		String firstLine=null;
		try {
			theUrl=new URL(absoluteBaseURL+"/oracle.php?problem="+key+"&submit="+r);
			BufferedReader in = new BufferedReader(new InputStreamReader(theUrl.openStream()));
			    firstLine = in.readLine();
			    System.out.println("Final return: "+firstLine);
			    in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	//@Override
	/*public int getCountEQ() {
		return countEx;
	}*/

	@Override
	public int getCountMQ() {
		countMQ++;
		return countMQ;
	}

}
