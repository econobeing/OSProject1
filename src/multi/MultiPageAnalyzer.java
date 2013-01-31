package multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import tools.WordCounter;

public class MultiPageAnalyzer implements Runnable
{
    /** The words to search for and the number of occurrences. */
    private static ArrayList<WordCounter> words = new ArrayList<WordCounter>();

    /** The incoming words that are to be examined. */
    private static volatile LinkedList<String> to_examine 
    	= new LinkedList<String>();
    
    /** Set to true when the thread should stop */
    private static boolean stop = false;
    
    @Override
    public void run()
    {
        while(!stop)
        {
        	while(!to_examine.isEmpty())
        	{
        		String aword = to_examine.getFirst();
        		for(WordCounter wc : words)
        		{
        			if(aword.equals(wc.word))
        				wc.count++;
        			
        			//TODO: catch null pointer exception on if statement
        		}
        		to_examine.removeFirst();
        	}
            
        	//give another thread a chance to do something
            try{
            	Thread.sleep(1);
            } catch (InterruptedException e){
            	
            }
        }
    }

    /** Erases the old list of search terms and loads a new one. */
    public static void giveWordsToFind(final Collection<WordCounter> the_words)
    {
        words = new ArrayList<WordCounter>(the_words);
    }
    
    /** Gets a list of WordCounter objects that the page analyzer was looking
     * for. Corresponds to the list given to giveWordsToFind(), except with
     * the counts updated. */
    public static ArrayList<WordCounter> getWordCounts()
    {
        return new ArrayList<WordCounter>(words);
    }
    
    /** Adds the given Collection of words to the to_examine queue. */
    public static void giveWordsToExamine(final Collection<String> the_words)
    {
        //addAll() was giving an index out of bounds exception
        for(String s : the_words)
            to_examine.add(s);
    }
    
    /**
     * Lets us know if the queue of words to examine is empty.
     * @return <code>true</code> if the page analyzer has no words left to
     * examine.
     */
    public static boolean isDone()
    {
        return to_examine.isEmpty();
    }
    
    /** Tells the thread to stop once it reaches the end of its current 
     * iteration. */
    public static void stop()
    {
        stop = true;
    }
}
