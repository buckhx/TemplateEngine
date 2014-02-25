import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
* Templating Engine that can be used to format string
* "${this} sentence" -> "that sentence"
*
* @author Buck Heroux
*/
public class TemplateEngine {

	private static String EscapeSet = "[\\\\.\\[\\{\\(\\)\\}\\]\\*\\+\\?\\^\\$\\|]";
	private static String KeyString = "^\\w+$";
	private static String DefaultOpenString = "${";
	private static String DefaultCloseString = "}";

	private Map<String,String> vars;
	private Pattern templatePattern;
	private String openString;
	private String closeString;
  	private String stripString;

  	/**
  	* Constructor that uses the default escape parameters ${, }
  	*/
	public TemplateEngine() {
		this(DefaultOpenString, DefaultCloseString);
	}

	/**
	* @param openString String that initializes a template sequence
	* @param closeString String that closes a template sequence
	* @param throws IllegalArgumentException if open or close string is empty or contains '\'
	*/
	public TemplateEngine(String openString, String closeString) {
		if (!ValidTemplate(openString, closeString))
			throw new IllegalArgumentException("Open or close string is not valid");
		this.openString = Regify(openString);
		this.closeString = Regify(closeString);
   		this.stripString = "(^"+this.openString+"|"+this.closeString+"$)";
		this.vars = new HashMap<String,String>();
		this.templatePattern = Pattern.compile(this.openString+"\\w+"+this.closeString);
	}

	/**
	* Adds a key-value to the templating engine
	* @param key Key to template in the string. Key must be a word string that matches ^\w+$
	* @param value Value to be replaced
	* @throws IllegalArgumentException if the key is in an invalid format
	*/
	public void add(String key, String value) {
		if (!key.matches(KeyString))
			throw new IllegalArgumentException("Key must match a KeyString Template: "+KeyString);
		this.vars.put(key, value);
	}

	/**
	* @param key Key to be removed from the engine
	* @return If the key was removed.
	*/
	public boolean remove(String key) {
		if (!this.vars.containsKey(key))
			return false;
		this.vars.remove(key);
		return true;
	}

	/**
	* @param input the string to be templated
	* @return the templated string
	* @throws IllegalStateException if a key in the input string does not exist in the engine.
	*/
	public String format(String input) {
		Matcher matcher = this.templatePattern.matcher(input);
		while (matcher.find()) {      
    		String key = matcher.group();
      		key = key.replaceAll(this.stripString, ""); //strip Open and Close string
			if (!this.vars.containsKey(key))
				throw new IllegalStateException("TemplateEngine does not contain key: "+key);
			input = input.replaceAll(this.openString+key+this.closeString, this.vars.get(key));
		}
		return input;
	}
  
  	/**
  	* This method takes a string and returns the regex
  	* representation of it ${} => \$\{\}
  	* @param value value to be turned into a regex patter
  	* @return the string changed into a regex format
  	*/
	private static String Regify(String value) {
		Pattern pattern = Pattern.compile(EscapeSet);
		Matcher matcher = pattern.matcher(value);
		while (matcher.find()) {
			String match = matcher.group();
    		String search = "\\"+match;
    		String replacement = "\\\\\\"+match;
			value = value.replaceAll(search, replacement);
		}
		return value;
	}

	/**
	* Used to validate the template rules
	* @param one input
	* @param two input
	* @return if the inputs matched the validity rules
	*/
	private static boolean ValidTemplate(String one, String two) {
    	if (one.length() < 1 || two.length() < 1)
      		return false;
    	if (one.contains("\\") || two.contains("\\"))
      		return false;
    	return true;
  	}

  	public static void main(String[] args) {
		TemplateEngine engine = new TemplateEngine();
		engine.add("one", "ONE");
		engine.add("two", "TWO");
	    try {
	     	engine.add("${two}", "TWO"); //throws exception
	     	throw new AssertionError("bad key");
	    } catch (IllegalArgumentException e) {}
	    String out = engine.format("${one} is now ${two} and ${${${two}}}");
		if (!out.equals("ONE is now TWO and ${${TWO}}"))
			throw new AssertionError(out);
	    try {
	     	engine.format("${three} is now ${two} and ${${two}}"); //throws exception
	     	throw new AssertionError("non existant key");
	    } catch (IllegalStateException e) {}
	    engine = new TemplateEngine("aa","bb");
	    engine.add("xx","yy");
	    out = engine.format("aaxxbb");
	    if (!out.equals("yy"))
	    	throw new AssertionError(out);
	    // I should test more of these
	    System.out.println("Success!");
	}
}


