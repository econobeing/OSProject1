package multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import tools.WordCounter;
import tools.StopWatch;

public class MultiUI
{
    private static final int max_words = 10;
    private static final int max_pages = 50;
    private static final String url = 
            "http://css.insttech.washington.edu/~mealden/";
    //private static final String url = "http://google.com";
    
//    private static final String[] wordlist = {"2009", "25", "30", "35", "40", 
//    	"45", "70", "90", "a", "abandoned", "about", "advanced", "advertising", 
//    	"all", "an", "and", "apples", "beach", "before", "beside", "blogs", 
//    	"books", "business", "but", "calendar", "cliff", "coconut", "crawler", 
//    	"diamonds", "do", "documents", "east", "embedded", "emeralds", 
//    	"extraneous", "file", "finance", "following", "gmail", "google", 
//    	"grapes", "groups", "grove", "hollow", "html", "hut", "igoogle", 
//    	"images", "in", "interconnected", "javascript", "language", "link", 
//    	"links", "log", "lots", "maps", "marks", "more", "near", "news", "no", 
//    	"north", "northeast", "of", "on", "one", "oranges", "other", 
//    	"outcropping", "paces", "page", "pages", "palms", "photos", "privacy", 
//    	"programs", "reader", "refer", "rock", "rubies", "sandbox", "scholar", 
//    	"search", "secluded", "self-contained", "set", "sets", "settings", 
//    	"several", "shopping", "sign", "single", "sites", "six", "solutions", 
//    	"south", "southeast", "southwest", "spot", "standing", "text", "the", 
//    	"to", "tools", "types", "urls", "videos", "walk", "web", "webpages", 
//    	"which", "with", "x", "you", "you're", "youtube"};
    
    /*
     * Problem words (not being found in the right amount or at all):
     * crawler, google, sandbox, web
     */

    /**
     * @param args
     */
    public static void main(String[] args)
    {
    	//printChars("gle | search settings | sign in     advanced search   language tools adve");
    	
    	System.out.println("Multi-threaded webcrawler");
        System.out.println("Enter up to " + max_words + " words. Use a blank" +
                " entry to stop input.");
        
        try {
            BufferedReader br 
                = new BufferedReader(new InputStreamReader(System.in));
            
            ArrayList<String> words = new ArrayList<String>();
            
            for(int i = 1 ; i <= max_words ; i++)
            {
                System.out.print("Enter word #" + i + ": ");
                String s = br.readLine();
                if(s.contains(" "))
                {
                    System.out.println("Single words only, no spaces please.");
                    i--;
                    continue;
                }
                if(s.length() == 0)
                    break;
                
                s = s.toLowerCase();
                s = s.trim();
                words.add(s);
            }
            
            ArrayList<WordCounter> items = new ArrayList<WordCounter>();
            for(String s : words)
            {
                WordCounter wc = new WordCounter(s);
                items.add(wc);
            }
            
//            for(int i = 0 ; i < wordlist.length ; i++)
//            {
//            	WordCounter wc = new WordCounter(wordlist[i]);
//            	items.add(wc);
//            }
            
            MultiPageAnalyzer.giveWordsToFind(items);
            
            MultiPageRetriever.setMax(max_pages);
            MultiPageRetriever.addURL(url);
            
            Thread retriever = new Thread(new MultiPageRetriever());
            Thread parser = new Thread(new MultiPageParser());
            Thread analyzer = new Thread(new MultiPageAnalyzer());
            
            StopWatch watch = new StopWatch();
            watch.start();
            
            retriever.start();
            parser.start();
            analyzer.start();
            
            
            while(true)
            {
            	if(MultiPageRetriever.isDone() 
            			&& MultiPageParser.isDone() 
            			&& MultiPageAnalyzer.isDone())
            	{
            		MultiPageRetriever.stop();
            		MultiPageParser.stop();
            		MultiPageAnalyzer.stop();
            		break;
            	}
            }
            
            long timepassed = watch.getElapsed();
            watch.stop();
            
            //print out results
            int retrieved = MultiPageRetriever.getRetrievedCount();
            int wordsparsed = MultiPageParser.getNumWordsParsed();
            int totallinks = MultiPageParser.getLinkCount();
            
            System.out.println("\n\nPages retrieved: " + retrieved);
            System.out.println("Total words retrieved: " + wordsparsed);
            int avgwpp = (retrieved!=0) ? wordsparsed/retrieved : wordsparsed;
			System.out.println("Average words per page: " + avgwpp);
            int avgurl = (retrieved!=0) ? totallinks/retrieved : totallinks;
			System.out.println("Average URLs per page: " + avgurl);
			
			System.out.println("\n(keyword) - (Avg. hits per page) - " +
					"(Total hits)");
			items = MultiPageAnalyzer.getWordCounts();
			for(WordCounter wc : items)
			{
				float avg = (retrieved != 0) ? 
                        (float)wc.count/(float)retrieved : 0;
				System.out.format(wc.word + " - " + "%.3f" + " - " + wc.count
						+ "\n", avg);
			}
          
			float avgtime = (float)MultiPageParser.watch.getElapsed() / 1000;
			System.out.format("\nAverage parse time per page: %.3f sec\n", 
					avgtime);
			float totaltime = (float)timepassed / 1000;
			System.out.format("Total running time: %.3f sec\n", totaltime);
            
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

//    private static void printChars(String s)
//    {
//    	char arr[] = s.toCharArray();
//    	
//    	for(int i = 0 ; i < s.length() ; i++)
//    	{
//    		System.out.println(arr[i] + " - " + (int)arr[i]);
//    	}
//    }
}
