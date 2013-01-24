package single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;


public class SinglePageAnalyzer
{
	/** The words to search for and the number of occurrences. */
	private static ArrayList<WordCounter> words = new ArrayList<WordCounter>();
	
	private static final LinkedList<String> bodies = new LinkedList<String>();
	
	/** Erases the old list of search terms and loads a new one. */
	public static void giveWords(final Collection<WordCounter> the_words)
	{
		words = new ArrayList<WordCounter>(the_words);
	}
	
	public static ArrayList<WordCounter> getWordCounts()
	{
		return new ArrayList<WordCounter>(words);
	}
	
	public static void giveBody(final String the_body)
	{
		bodies.add(the_body);
	}
	
	public static void analyze()
	{
		while(!bodies.isEmpty())
		{
			String body = bodies.removeFirst();
			ArrayList<String> parsed_words = parseString(body);
			for(WordCounter wc : words)
			{
			    for(String str : parsed_words)
			    {
			        if(str.equals(wc.word))
			            wc.count++;
			    }
			}
		}
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
}
