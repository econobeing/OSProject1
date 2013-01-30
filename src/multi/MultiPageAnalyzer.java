package multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import single.WordCounter;

public class MultiPageAnalyzer implements Runnable
{
    /** The words to search for and the number of occurrences. */
    private static ArrayList<WordCounter> words = new ArrayList<WordCounter>();
    
    private static final LinkedList<String> bodies = new LinkedList<String>();
    
    private static boolean stop = false;
    
    @Override
    public void run()
    {
        while(!stop)
        {
            while(!bodies.isEmpty())
            {
                String body = bodies.getFirst();
                ArrayList<String> parsed_words = parseString(body);
                for(WordCounter wc : words)
                {
                    for(String str : parsed_words)
                    {
                        if(str.equals(wc.word))
                            wc.count++;
                    }
                }
                bodies.removeFirst();
            }
        }
    }

    /** Erases the old list of search terms and loads a new one. */
    public static synchronized void giveWords(final Collection<WordCounter> the_words)
    {
        words = new ArrayList<WordCounter>(the_words);
    }
    
    public static synchronized ArrayList<WordCounter> getWordCounts()
    {
        return new ArrayList<WordCounter>(words);
    }
    
    public static synchronized void giveBody(final String the_body)
    {
        bodies.add(the_body);
    }
    
    public static ArrayList<String> parseString(String str)
    {
        final ArrayList<String> words_found = new ArrayList<String>();
        
        String[] parsed_words = str.split("[? /\\,{}.)(*&^%$#@!;+]");
        
        for(int i = 0 ; i < parsed_words.length ; i++)
        {
            if(parsed_words[i].length() != 0)
                words_found.add(parsed_words[i]);
        }
           
        return words_found;
    }
    
    public static synchronized boolean isDone()
    {
        return bodies.isEmpty();
    }
    
    public static synchronized void stop()
    {
        stop = true;
    }
}
