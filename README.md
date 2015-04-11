<h1>Baseline algorithm</h1>

<span style="margin-left:30px;">The baseline is based on a simplified version of L*, by Dana Angluin. It is written in Java. You can download it below :<p>

<div align="center">
<img src="images/arrow_down.png" /> <a href="zulu_client/zuluBaselineSrc.jar">zuluBaselineSrc.jar</a><br></div><p>
zuluBaselineSrc.jar contains the compiled .class files and the Java source files of the algorithm.

The class with the "main" is Lstar. You can also view the <a href="extras/Javadoc" target="_blank">Javadoc for the Zulu client</a> 
for more information about its structure.<p>

You can read directly the source code <a href="http://code.google.com/p/zulubaseline/source/browse/#svn/trunk/src/zulu/baseline">here</a>.<p>

To use the baseline, create a project in your favorite integrated development environment and test and modify the baseline the way you want. 
A good way if you want to get rid of Lstar in your algorithm is to instanciate a <a href="extras/Javadoc/zulu/baseline/RemoteOracle.html">"RemoteOracle"</a> object 
as it is done in the Lstar class main function.<p>

Some commented functions are useful if you want to see the automata as it is built by the implementation of L*. Do not hesitate to uncomment them and see what happens.

<h2>Are there some components to install before running the baseline ?</h2>
<span style="margin-left:30px;">In order to RUN ONLY the baseline you need the <a href="http://www.java.com/fr/download/index.jsp">Java runtime</a>. 
If you want to modify it you need the <a href="http://java.sun.com/javase/downloads/index.jsp">Java Development Kit (JDK)</a>.  
Java SE Development Kit is enough, but if you need an IDE, choose the bundle <a href="http://java.sun.com/javase/downloads/netbeans.html">JDK with NetBeans</a> 
(click on the right on the first "Download" link).
You need the <a href="http://java.sun.com/javase/downloads/index.jsp">Java Development Kit</a> ("choose Java SE Development Kit (JDK)" in the second section, 
or any bundle) in order to rebuild and so to modify the algorithm.<p>

<br>
<h2>Execution</h2>

<div class="exempleShell">
java -jar zuluBaselineSrc.jar <font color="#FF0000">Task_Key</font>
</div><p>
where <font color="#FF0000">Task_Key</font> is the 8 digits number given at the task generation or at the challenge creation.

<br>
<h2>Description of the main files</h2>
<ul>
<li>Lstar.java : the main file, with the algorithm itself (in the constructor).</li>
<li>RemoteOracle.java : the communication interface with the server of the project. It is a child of the Oracle interface.</li>
<li>Oracle.java : the interface class for an Oracle with membership queries, getting alphabet of a target, submit your result.</li>
<li>Automaton.java : class, dedicated to the implementation of the Lstar algorithm, that produces an automaton from an observation table.</li>
<li>WordAndClass.java : class associating a string and its classification (dedicated to Lstar).</li>
<li>PrintOnScreen.java : debugging for Lstar.</li>

</ul>
For more information please see the <a href="extras/Javadoc">Javadoc for the Zulu client</a>.<p>




Unjar archive and run:
<div class="exempleShell">
jar -x zuluBaselineSrc.jar<br>
javac zulu/baseline/Lstar.java<br>
java Lstar <font color="#FF0000">Task_Key</font></div>
<p>
Jar archives are also extractable with 7-zip and other common compression utility.
You can edit and easily run the package within an integrated development environment such as <a href="http://www.eclipse.org/"><img src="images/eclipse_pos_logo_fc_xsm.jpg" alt="Eclipse" /></a> or <a href="http://www.netbeans.org/"><img src="images/netbeans-button-110x32.gif" alt="NetBeans" /></a>, under various platforms. Both are free and open-source.
<br>


<h2>Debug your program</h2>
You can process manually some queries here: <a href="askOracleHtml.php">Debugging tool</a>.

<br>
<a name="yourlanguage" /><h2>Develop a new algorithm in your own language</h2>

You can process the queries and get information directly from a wget command. The URLs you need to call are described below.

<h3>Obtain the alphabet of the target</h3>
<span style="margin-left:30px;">2-letters problems have ab as their alphabet. 3-letters problems have <i>abc</i> as their alphabet... and so on.
If you don't want to fix it in your program you can ask for the alphabet at<div class="exempleURL"> http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=<font color="#FF0000">PROBLEM-KEY</font>&alphabet=1</div> where <font color="#FF0000">PROBLEM-KEY</font> is the 8-digit number that you can find <a href="problems.php">here</a> (you need to be logged in).
<p>
<h3>Ask for a string</h3>
<div class="exempleURL">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=<font color="#FF0000">PROBLEM-KEY</font>&word=<font color="#FF0000">STRING-YOU-ASK-FOR</font></div>
You obtain there "Yes" if the string belongs to the automaton, or "No" if it doesn't. If the limit is reached the response is "Limit" (ie if you have used up your credit!).
<p>
Example: If you want to know if "0110101" belong to toy task 1 (key 00000001) :<br>
<div class="exempleURL"><a href="http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&word=0110101">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&word=0110101</a></div>
<p>
<h3>Get the 1800 strings to classify</h3>
At this time you can't ask more membership queries (the target is "locked").
<div class="exempleURL">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=<font color="#FF0000">PROBLEM-KEY</font>&iThinkIFound=1</div>
<p>
Example: If you want to obtain the words to classify for toy task 1 (key 00000001) :<br>
<div class="exempleURL"><a href="http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&iThinkIFound=1">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&word=0110101</a></div>
Please note that this toy task have fewer words to classify than regular tasks.

<h3>Get the membership queries amount limit</h3>
Please count yourself how many query you have used if you need this information.
<div class="exempleURL">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=<font color="#FF0000">PROBLEM-KEY</font>&limit=1</div>
<p>
Example: If you want the query amount limit for toy task 1 (key 00000001) :<br>
<div class="exempleURL"><a href="http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&limit=1">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=00000001&word=0110101</a></div>
Please note that toy tasks have no limit.

<p>
<h3>Submit your classification</h3>
<div class="exempleURL">http://labh-curien.univ-st-etienne.fr/zulu/oracle.php?problem=<font color="#FF0000">PROBLEM-KEY</font>&submit=<font color="#FF0000">YNNNYNNYNNN</font></div>

You can use <i>wget</i> in order to obtain the answers of membership queries and the 1800 strings.

<h3>wget call exemple</h3>
<div class="exempleShell">
wget -O responseFile http://labh-curien.univ-st-etienne.fr/zulu/oracle.php\?problem\=01\&word=0110101
</div>


<br>
<h2>Troubleshooting: if you use a Proxy</h2>
You should be concerned by the following only if your environment is not correctly configured.<p>
<h3>Command Line JVM Settings</h3>

The proxy settings are given to the JVM via command line arguments:

<div class="exempleShell">
$ java -Dhttp.proxyHost=proxyhostURL<br>
-Dhttp.proxyPort=proxyPortNumber<br>
-Dhttp.proxyUser=someUserName<br>
-Dhttp.proxyPassword=somePassword javaClassToRun<br>
</div>

<h3>Setting System Properties in Code</h3>

Add the following lines in your Java code so that JVM uses the proxy to make HTTP calls. This would, of course, require you to recompile your Java source. (The other methods do not require any recompilation.):<br>
<div class="exempleShell">
System.getProperties().put("http.proxyHost", "someProxyURL");<br>
System.getProperties().put("http.proxyPort", "someProxyPort");<br>
System.getProperties().put("http.proxyUser", "someUserName");<br>
System.getProperties().put("http.proxyPassword", "somePassword");<br>
</div>

<br>
<a name="perl"><h2>PERL interface for Zulu</h2></a>
It provides a majority vote program written in PERL in order to provide an example of the use of the Zulu functions with a script language.
<a href="zulu_client/PERL_interface.pl">Download</a>.<p>

The 5 functions provided :<p>
<i>zuluMQ</i>
makes a membership query
<p>
<i>zuluLimit</i>
returns how many queries are allowed
<p>
<i>zuluAlphabet</i>
returns the symbols allowed for the target
<p>
<i>zuluIThinkIFound</i>
asks for the test set. No zuluMQ calls are allowed since therefore.
<p>
<i>zuluSubmit</i>
sends your answers for the test set classification.
