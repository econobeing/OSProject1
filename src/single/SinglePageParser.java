package single;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

public class SinglePageParser
{
	/** The retrieved pages to be parsed. */
	private static final LinkedList<Document> pages 
		= new LinkedList<Document>();
	
	/** The number of links per page. */
	private static int links_found = 0;
	
	private static int pages_parsed = 0;
	
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
		while(!pages.isEmpty())
		{
			//TODO: add timing info to detect how long each page
			// takes to parse.
			
			Document doc = pages.getFirst();
			pages_parsed++;
			
			//get links in the doc and give them to the page retriever.
			Elements links = doc.select("a[href]");
			for(Element link : links)
			{
				SinglePageRetriever.addURL(link.attr("abs:href"));
				links_found++;
			}
			
			//TODO: parse other stuff, words and whatnot.
		}
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
}
