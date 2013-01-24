package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class URLTest
{
	//final static String url = "http://css.insttech.washington.edu/~mealden/";
    final static String url = "http://css.insttech.washington.edu/~mealden/3/";
	//^doesn't work with local html docs..., at least not Jsoup.connect().get
	
	/**
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
    	System.out.println("Fetching " + url + " ...");
    	
    	Document doc =  Jsoup.connect(url).get();
    	
    	Elements links = doc.select("a[href]");
    	Elements media = doc.select("[src]");
    	Elements imports = doc.select("link[href]");
    	
    	//print out link info
    	System.out.println("Links: (" + links.size() + ")");
    	for(Element link : links)
    		System.out.println(getLinkInfo(link));
    	
    	//print out media info
    	System.out.println("Media: (" + media.size() + ")");
    	for(Element src : media)
    		System.out.println(getMediaInfo(src));
    	
    	//print out import info
    	System.out.println("Imports: (" + imports.size() + ")");
    	for(Element link : imports)
    		System.out.println(getImportInfo(link));
    	
    	
    	//get body text.
    	String text = doc.body().text();
    	text = text.toLowerCase();

    	String word = "single";
    	int location = text.indexOf(word);
    	int count = 0;
    	System.out.println("\n\n" + text);
    	while(location != -1)
    	{
    		count++;
    		text = text.substring(location + word.length(), text.length());
    		location = text.indexOf(word);
    		System.out.println(text);
    	}
    	System.out.println("\n\nOccurrences of the word '" 
    			+ word + "': " + count);
    	
    	String tester = "one two three four five six seven. eight nine/ten";
    	ArrayList<String> words = parseString(tester);
    	for(String s : words)
    		System.out.println("'" + s + "' - " + s.length());
    }
    
    public static ArrayList<String> parseString(String str)
    {
        final ArrayList<String> words_found = new ArrayList<String>();
//        String str = "This is a sentence.  This is a question, right?  Yes!  It is.";
//        String delims = "[ .,?!]+";
//        String[] tokens = str.split(delims);

        //String[] parsed_words = str.split(" ?/\\,[]{}.)(*&^%$#@!;+");
        String[] parsed_words = str.split("[ ?/\\,{}.)(*&^%$#@!;+]");
        
        for(int i = 0 ; i < parsed_words.length ; i++)
        {
            if(parsed_words[i].length() != 0)
                words_found.add(parsed_words[i]);
        }
        
        return words_found;
    }
    
    private static String getLinkInfo(final Element link)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" * a: <");
    	sb.append(link.attr("abs:href"));
    	sb.append("> (");
    	sb.append(link.text());
    	sb.append(")");
    	return sb.toString();
    }

    private static String getMediaInfo(final Element src)
    {
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(src.tagName());
		sb.append(": <");
		sb.append(src.attr("abs:src"));
		sb.append("> ");
		sb.append(src.attr("width"));
		sb.append("x");
		sb.append(src.attr("height"));
		sb.append(" (");
		sb.append(src.attr("alt"));
		sb.append(")");
		return sb.toString();
    }
    
    private static String getImportInfo(final Element link)
    {
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(link.tagName());
		sb.append(" <");
		sb.append(link.attr("abs:href"));
		sb.append("> (");
		sb.append(link.attr("rel"));
		return  sb.toString();
    }
}
