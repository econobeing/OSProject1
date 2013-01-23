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
			for(WordCounter wc : words)
			{
				String temp = new String(body);
				int location = temp.indexOf(wc.word);
				while(location >= 0)
				{
					wc.count++;
					temp = temp.substring(location + wc.word.length(), 
							temp.length());
					location = temp.indexOf(wc.word);
				}
			}
		}
	}
}
