package multi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MultiPageRetriever implements Runnable
{
    /** The URLs to retrieve. */
    private static final LinkedList<String> queue = new LinkedList<String>();
    
    /** The URLs that we've already retrieved. */
    private static final ArrayList<String> finished = new ArrayList<String>();
    
    /** The number of retrieved pages so far. */
    private static int retrieved = 0;
    
    /** The maximum number of pages to retrieve. */
    private static int max_pages = 0;
    
    private static boolean stop = false;
    
    
    @Override
    public void run()
    {
        while(!stop)
        {
            while(!queue.isEmpty() && retrieved < max_pages)
            {
                String url = queue.getFirst();
                try{
                    Document doc = Jsoup.connect(url).get();
                    retrieved++;
                    MultiPageParser.addPage(doc);
                    queue.removeFirst();
                } catch (IOException e) {
                    System.err.println("Could not retrieve URL: " + url);
                    System.err.println(e.getMessage());
                }
            }
            
            if(retrieved >= max_pages)
                stop = true;
            
            //TODO: maybe put a wait() in here
        }
    }
    
    /**
     * Adds the given URL to the retrieval queue. Will not add the url if it
     * has been passed in previously.
     * @param url The URL to retrieve. 
     */
    public static synchronized void addURL(final String url)
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
    public static synchronized void setMax(final int max)
    {
        if(max >= 0)
            max_pages = max;
    }
    
    /** Gets the number of pages retrieved so far. */
    public static synchronized int getRetrievedCount()
    {
        return retrieved;
    }
    
    /**
     * The page retriever is done either when: it has no URLs left to retrieve,
     * or the retrieve limit has been reached.
     * @return <code>true</code> if the page retriever is done.
     */
    public static synchronized boolean isDone()
    {
        return (retrieved >= max_pages) || (queue.isEmpty());
    }

    public static synchronized void stop()
    {
        stop = true;
    }
}
