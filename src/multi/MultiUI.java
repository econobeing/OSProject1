package multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import single.WordCounter;

public class MultiUI
{
    private static final int max_words = 10;
    private static final int max_pages = 50;
    private static final String url = 
            "http://css.insttech.washington.edu/~mealden/";

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
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
            
            //TODO: give search terms to SinglePageAnalyzer probably. And
            // start retrieving pages.
            ArrayList<WordCounter> items = new ArrayList<WordCounter>();
            for(String s : words)
            {
                WordCounter wc = new WordCounter(s);
                items.add(wc);
            }
//            SinglePageAnalyzer.giveWords(items);
//            
//            SinglePageRetriever.setMax(max_pages);
//            SinglePageRetriever.addURL(url);
//            SinglePageRetriever.retrieve();
//            
//            items = SinglePageAnalyzer.getWordCounts();
//            for(WordCounter wc : items)
//            {
//                System.out.println(wc.word + " - " + wc.count);
//            }
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }


}
