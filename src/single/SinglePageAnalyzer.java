package single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import tools.WordCounter;


public class SinglePageAnalyzer
{
	/** The words to search for and the number of occurrences. */
	private static ArrayList<WordCounter> words = new ArrayList<WordCounter>();
	
	/** The queue of words that haven't been examined yet. */
	private static final LinkedList<String> to_examine 
		= new LinkedList<String>();
	
	/** Erases the old list of search terms and loads a new one. */
	public static void giveWordsToFind(final Collection<WordCounter> the_words)
	{
		words = new ArrayList<WordCounter>(the_words);
	}
	
	/** 
	 * Gets a copy of the list of words the analyzer was looking for.
	 * Corresponds to the list passed into giveWordsToFind(), except with
	 * their count values modified to reflect how many times the word has
	 * been encountered.
	 */
	public static ArrayList<WordCounter> getWordCounts()
	{
		return new ArrayList<WordCounter>(words);
	}
	
	/** Adds the words in the given Collection to the queue of words to
	 * be examined. */
	public static void giveWordsToExamine(final Collection<String> the_words)
	{
//		for(String s : the_words)
//			to_examine.push(s);
		to_examine.addAll(the_words);
	}
	
	/** Analyzes all the words currently in the queue to see if they match any
	 * of the words that are being looked for. */
	public static void analyze()
	{
		while(!to_examine.isEmpty())
		{
			String aword = to_examine.removeFirst();
			for(WordCounter wc : words)
			{
				if(aword.equals(wc.word))
					wc.count++;
			}
		}
	}
	
}
