package single;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tools.StopWatch;

import java.util.ArrayList;
import java.util.LinkedList;

public class SinglePageParser
{
	public static StopWatch watch = new StopWatch();
	
	/** The retrieved pages to be parsed. */
	private static final LinkedList<Document> pages 
		= new LinkedList<Document>();
	
	/** The number of links per page. */
	private static int links_found = 0;
	
	/** The number of pages that have been parsed. */
	private static int pages_parsed = 0;
	
	/** How many words we have extracted from the pages. */
	private static int words_parsed = 0;
	
	/** A list of common image file extensions. Used to prevent sending
	 * images to SinglePageRetriever. */
	private static final String[] image_extensions = {".png", ".gif", ".jpg", 
        ".bmp", ".jpeg"};
	
	/**
	 * Adds a page to the parse queue.
	 * @param doc The page to add to the parse queue.
	 */
	public static void addPage(final Document doc)
	{
		pages.add(doc);
	}
	
	/**
	 * Parses the pages in the queue.
	 */
	public static void parse()
	{
		boolean success = false;
		int oldlinksfound = links_found;
		
		while(!pages.isEmpty())
		{
			watch.unpause();
			
			success = true;
			Document doc = pages.removeFirst();
			pages_parsed++;
			
			//get links in the doc and give them to the page retriever.
			Elements links = doc.select("a[href]");
			for(Element link : links)
			{
				//SinglePageRetriever.addURL(link.attr("abs:href"));
				String url = link.attr("abs:href");
                if(!isImage(url))
                	SinglePageRetriever.addURL(url);
				links_found++;
			}
			
			SinglePageAnalyzer.giveWordsToExamine(
					parseString(doc.body().text().toLowerCase()));
			SinglePageAnalyzer.giveWordsToExamine(
			        parseString(doc.title().toLowerCase()));
			
			watch.pause();
		}
		if(success)
			SinglePageAnalyzer.analyze();
		if(oldlinksfound < links_found)
			SinglePageRetriever.retrieve();
	}
	
	/** Returns the number of links found so far. */
	public static int getLinkCount()
	{
		return links_found;
	}
	
	/** Returns the number of pages parsed so far. */
	public static int getParseCount()
	{
		return pages_parsed;
	}
	
	/** Returns the number of words parsed so far. */
	public static int getNumWordsParsed()
	{
		return words_parsed;
	}
	
	/**
	 * Takes in a string containing the body of a web page and parses the words
	 * out of it.
	 * @param str The web page body text to parse words out of.
	 * @return The list of parsed words.
	 */
	private static ArrayList<String> parseString(String str)
	{
		final ArrayList<String> wordlist = new ArrayList<String>();
      
		String regex = "[? /\\,{}.)(*&^%$#@!;+©";
		regex += (char)160; //the char in front of "advanced" and "language"
		regex += "]";
      
		String[] parsed_words = str.split(regex);
      
		for(int i = 0 ; i < parsed_words.length ; i++)
		{
			if(parsed_words[i].length() != 0)
			{
        	  	wordlist.add(parsed_words[i]);
              	words_parsed++;
          	}
      	}
         
      	return wordlist;
	}
	
	/** 
	 * Takes in a URL and checks to see if it has an image extension.
	 * @param s The URL to look at.
	 * @return <code>true</code> if the URL contains a common image extension.
	 */
	private static boolean isImage(String s)
    {
        int loc = s.lastIndexOf(".");
        if(loc == -1)
            return false;
        
        String ext = s.substring(loc);
        
        for(int i = 0 ; i < image_extensions.length ; i++)
        {
            if(image_extensions[i].equals(ext))
                return true;
        }
        
        return false;
    }
}
