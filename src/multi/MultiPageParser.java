package multi;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tools.StopWatch;

import java.util.ArrayList;
import java.util.LinkedList;

public class MultiPageParser implements Runnable
{
	/** What we use to time how long it takes to parse everything. */
	public static StopWatch watch = new StopWatch();
	
    /** The retrieved pages to be parsed. */
    private static volatile LinkedList<Document> pages 
        = new LinkedList<Document>();
    
    /** The number of links found so far. */
    private static int links_found = 0;
    
    /** The number of pages parsed so far. */
    private static int pages_parsed = 0;
    
    /** The number of words that have been parsed from the pages. */
    private static int words_parsed = 0;
    
    /** Whether or not the thread should stop. */
    private static boolean stop = false;
    
    /** A list of common image extensions, used to ignore links to images. */
    private static final String[] image_extensions = {".png", ".gif", ".jpg", 
        ".bmp", ".jpeg"};

    @Override
    public void run()
    {
    	watch.start();
    	watch.pause();
    	
        while(!stop)
        {
            while(!pages.isEmpty())
            {
            	watch.unpause();
            	
                Document doc = pages.getFirst();
                pages_parsed++;
                
                //get links in the doc and give them to the page retriever.
                Elements links = doc.select("a[href]");
                for(Element link : links)
                {
                    String url = link.attr("abs:href");
                    if(!isImage(url))
                        MultiPageRetriever.addURL(url);
                    links_found++;
                }
                
                MultiPageAnalyzer.giveWordsToExamine(
                		parseString(doc.body().text().toLowerCase()));
                
                watch.pause();
                
                pages.removeFirst();
            }
            	
            //give another thread a chance to do something
            try{
            	Thread.sleep(1);
            } catch (InterruptedException e){
            	
            }
        }
    }

    /**
     * Adds a page to the parse queue.
     * @param doc The page to add to the parse queue.
     */
    public static void addPage(final Document doc)
    {
        pages.add(doc);
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
    
    /** Returns the number of words parsed from the pages. */
    public static int getNumWordsParsed()
    {
    	return words_parsed;
    }
    
    /** Whether or not the page queue is empty. Should tell us whether or not
     * the thread is ready to be stopped. */
    public static boolean isDone()
    {
        return pages.isEmpty();
    }
    
    /** Tells the thread to stop once it finishes its current iteration. */
    public static void stop()
    {
        stop = true;
    }
    
    /**
     * Takes an input string and parses individual words out of it. Used on
     * the body of a page.
     * @param str The page body text to parse words from.
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
     * Checks to see if the given URL has a common image extension at the end.
     * @param s The URL to check the extension of.
     * @return <code>true</code> if the URL has an image extension.
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
