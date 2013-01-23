package single;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class SinglePageRetriever 
{
	/** The URLs to retrieve. */
	private static final LinkedList<String> queue = new LinkedList<String>();
	
	/** The URLs that we've already retrieved. */
	private static final ArrayList<String> finished = new ArrayList<String>();
	
	/** The number of retrieved pages so far. */
	private static int retrieved = 0;
	
	/** The maximum number of pages to retrieve. */
	private static int max_pages = 0;
	
	/**
	 * Adds the given URL to the retrieval queue. Will not add the url if it
	 * has been passed in previously.
	 * @param url The URL to retrieve. 
	 */
	public static void addURL(final String url)
	{
		if(!finished.contains(url))
		{
			queue.add(url);
			System.out.println("added URL: " + url);
			finished.add(url);
		}
		else 
		{
			System.out.println("ignored URL: " + url);
		}
			
	}
	
	/**
	 * Sets the maximum number of pages to retrieve.
	 * @param max The maximum number of pages to retrieve.
	 */
	public static void setMax(final int max)
	{
		if(max >= 0)
			max_pages = max;
	}
	
	/**
	 * Retrieves pages from the queue until either: the queue is empty; or
	 * the max page retrieval limit is reached.
	 */
	public static void retrieve()
	{
		boolean success = false; // whether or not any pages were retrieved.
		while(retrieved < max_pages && !queue.isEmpty())
		{
			String url = queue.removeFirst();
			try {
				Document doc = Jsoup.connect(url).get();
				retrieved++;
				SinglePageParser.addPage(doc);
				success = true;
				//finished.add(url);
			} catch (IOException e) {
				System.err.println("Could not retrieve URL: " + url);
				System.err.println(e.getMessage());
			}
		}
		
		if(success)
			SinglePageParser.parse();
	}
	
	/** Gets the number of pages retrieved so far. */
	public static int getRetrievedCount()
	{
		return retrieved;
	}
	
	/** Returns <code>true</code> if the page retrieval limit 
	 * has been reached. */
	public static boolean limitReached()
	{
		return retrieved >= max_pages;
	}
}
