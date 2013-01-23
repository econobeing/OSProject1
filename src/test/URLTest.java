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
    	
    	String tester = "one two three four five six seven eight";
    	ArrayList<String> words = parseWords(tester);
    	for(String s : words)
    		System.out.println("'" + s + "' - " + s.length());
    }
    
    private static ArrayList<String> parseWords(String str)
    {
    	ArrayList<String> words = new ArrayList<String>();
    	
    	int location = str.indexOf(" ");
    	while(location >= 0)
    	{
    		String extracted = str.substring(0,location);
    		words.add(extracted);
    		str = str.substring(location+1);
    		location = str.indexOf(" ", 0);
    	}
    	if(str.length() > 0)
    		words.add(str);

    	return words;
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
