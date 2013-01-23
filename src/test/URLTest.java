package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class URLTest
{
	final static String url = "http://css.insttech.washington.edu/~mealden/";
    
	//final static String url = "/home/econobeing/Desktop/blah.html";
	//^doesn't work with local html docs..., at least not Jsoup.connect().get
	
	/**
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
    	
    	System.out.println("Fetching " + url + " ...");
    	
    	Document doc =  Jsoup.connect(url).get();
    	
    	Elements links = doc.select("a[href]");
    	Elements media = doc.select("[src]");
    	Elements imports = doc.select("link[href]");
    	
    	System.out.println("Links: (" + links.size() + ")");
    	for(Element link : links)
    	{
    		System.out.println(" * a: <" + link.attr("abs:href") + "> (" 
    				+ link.text() + ")");
    	}
    				
    	
    	System.out.println("Media: (" + media.size() + ")");
    	
    	
    	System.out.println("Imports: (" + imports.size() + ")");
    	
    }

}
