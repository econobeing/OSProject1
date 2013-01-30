package multi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MultiPageRetriever implements Runnable
{
    /** The URLs to retrieve. */
    private static volatile LinkedList<String> queue 
    	= new LinkedList<String>();
    
    /** Set to true when the thread should stop */
    private static boolean stop = false;
    
    /** The URLs that we've already retrieved. */
    private static volatile ArrayList<String> finished 
    	= new ArrayList<String>();
    
    /** The number of retrieved pages so far. */
    private static volatile int retrieved = 0;
    
    /** The maximum number of pages to retrieve. */
    private static int max_pages = 0;    
    
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
            
           	//give other threads a chance to run
            try{
            	Thread.sleep(1);
            } catch (InterruptedException e){
            	
            }
        }
    }
    
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
            //System.out.println("added URL: " + url);
            finished.add(url);
        }
//        else 
//        {
//            System.out.println("ignored URL: " + url);
//        }
            
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
    
    /** Gets the number of pages retrieved so far. */
    public static int getRetrievedCount()
    {
        return retrieved;
    }
    
    /**
     * The page retriever is done either when: it has no URLs left to retrieve,
     * or the retrieve limit has been reached.
     * @return <code>true</code> if the page retriever is done.
     */
    public static boolean isDone()
    {
        return (retrieved >= max_pages) || (queue.isEmpty());
    }

    /** Tells the thread to stop once it reaches the end of its loop. */
    public static void stop()
    {
        stop = true;
    }
}
