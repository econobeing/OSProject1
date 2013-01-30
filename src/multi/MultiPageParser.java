package multi;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

public class MultiPageParser implements Runnable
{
    /** The retrieved pages to be parsed. */
    private static final LinkedList<Document> pages 
        = new LinkedList<Document>();
    
    /** The number of links per page. */
    private static int links_found = 0;
    
    private static int pages_parsed = 0;
    
    private static boolean stop = false;
    
    public static String[] image_extensions = {".png", ".gif", ".jpg", 
        ".bmp", ".jpeg"};

    @Override
    public void run()
    {
        while(!stop)
        {
            while(!pages.isEmpty())
            {
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
                
                MultiPageAnalyzer.giveBody(doc.body().text().toLowerCase());
                pages.removeFirst();
            }
            //TODO: maybe put a wait() in here
        }
    }

    /**
     * Adds a page to the parse queue.
     * @param doc The page to add to the parse queue.
     */
    public static synchronized void addPage(final Document doc)
    {
        pages.add(doc);
    }
    
    /** Returns the number of links found so far. */
    public static synchronized int getLinkCount()
    {
        return links_found;
    }
    
    /** Returns the number of pages parsed so far. */
    public static synchronized int getParseCount()
    {
        return pages_parsed;
    }
    
    public static synchronized boolean isDone()
    {
        return pages.isEmpty();
    }
    
    public static synchronized void stop()
    {
        stop = true;
    }
    
    private boolean isImage(String s)
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
