package single;

import java.util.ArrayList;
import java.util.Collection;

public class SinglePageAnalyzer
{
	/** The words to search for. */
	private static ArrayList<String> words = new ArrayList<String>();
	
	/** Erases the old list of search terms and loads a new one. */
	public static void giveWords(final Collection<String> the_words)
	{
		words = new ArrayList<String>(the_words);
	}
}
