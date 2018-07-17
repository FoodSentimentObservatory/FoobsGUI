package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.scene.control.CheckBox;
import javassist.bytecode.Descriptor.Iterator;

public class TwitterKeywordSplitNew {

public static  ArrayList <String> createKeywordStrings (HashMap <String,ArrayList<String>> keywords, String pattern, CheckBox allCombinations) {
		
		ArrayList <String> keywordslist =  new ArrayList <String> ();
		int THRESHOLD = 350;
		StringBuilder temp = new StringBuilder();
		char quotes='"';
		
		int numbOfColumns = 0;
		HashSet <Integer> sizes = new HashSet <Integer> ();
		HashSet <String> keysWithKeywords = new HashSet <String> ();
		int keywordListength = 0;
		for (String key : keywords.keySet()) {
			if ( keywords.get(key).size()>0) {
			 keywordListength = keywords.get(key).size();
			 sizes.add(keywordListength);
			 numbOfColumns++;
			 keysWithKeywords.add(key);
			}
		}
		
		
		ArrayList <String> wordsPerTweetCompinations = new ArrayList <String>();
		//simple one word per Tweet joined with OR
		
		if (numbOfColumns==0) {
			//no keywords specified
			return null;
		}
		
		if (numbOfColumns==1) {
			for (String key : keysWithKeywords) {
			wordsPerTweetCompinations = keywords.get(key);
			}
		}
		else {
			//at the moment we will assume the lists have to be of the same length
			//check lists are the same length
			
			
			
			
			if (sizes.size()>1) {
				//no keywords specified
				System.out.println("Keyword lists not the same size!");
				return null;
			}
			
			
			if (!allCombinations.isSelected()) {
				
				wordsPerTweetCompinations = new ArrayList <String> ();
				for (int i =0; i<keywordListength;i++) {
					wordsPerTweetCompinations.add("("+pattern+")");
				}
				
				for (String key : keysWithKeywords) {
					ArrayList <String> keywordsPart = keywords.get(key);
					for (int i =0; i< wordsPerTweetCompinations.size();i++) {
						StringBuilder tempString = new StringBuilder();
						if (keywordsPart.get(i).contains(" "))  {
							tempString.append(quotes);
							tempString.append(keywordsPart.get(i));
							tempString.append(quotes);
						}
						else {
							tempString.append(keywordsPart.get(i));
						}
						String word = wordsPerTweetCompinations.get(i).replaceAll(key, tempString.toString());
						wordsPerTweetCompinations.set(i, word);
					}
				}
				
				
			}
			
			else {
				wordsPerTweetCompinations = new ArrayList <String> ();
				
				//calculate required number of combinations
                int sizeOfCombinations = 1;
                
                
				for (String key : keysWithKeywords) {
					sizeOfCombinations = sizeOfCombinations*keywords.get(key).size();
				}
				for (int i =0; i<sizeOfCombinations;i++) {
					wordsPerTweetCompinations.add("("+pattern+")");
				}
				
				int keyCounter = 1;
				
                
				
				for (String key : keysWithKeywords) {
					ArrayList <String> keywordsPart = keywords.get(key);
					
					int repeat = 1;
					
					for (int i=0;i<keysWithKeywords.size()-keyCounter;i++) {
			              repeat = repeat *keywordListength;  	
			        }
					int index = 0;
					int step =0;
					for (int i =0; i< wordsPerTweetCompinations.size();i++) {
						
						StringBuilder tempString = new StringBuilder();
						if (keywordsPart.get(index).contains(" "))  {
							tempString.append(quotes);
							tempString.append(keywordsPart.get(index));
							tempString.append(quotes);
						}
						else {
							tempString.append(keywordsPart.get(index));
						}
						String word = wordsPerTweetCompinations.get(i).replaceAll(key, tempString.toString());
						wordsPerTweetCompinations.set(i, word);
						
						step++;
						if (step ==repeat) {
							if (index + 1 == keywordsPart.size()) {
								index=0;
							}
							else {
							index++;
							}
							step=0;
						}
						
					}
					keyCounter++;
				}
				
			}
			
			
		}
		
		//don't add quotes;
		boolean addquotes = true;
		if (numbOfColumns>1) {
			addquotes=false;
		}
		
		
		for (int i=0 ; i <wordsPerTweetCompinations.size();i++ ) {
			
		String word =  wordsPerTweetCompinations.get(i).trim();
		
		//System.out.println("Processing"+word);
		
		if (word.length()>0) {
			//word is phrase
		if (word.contains(" ")&&addquotes) {
			if (temp.length()==0) {
			//first phrase in the string
				
				temp.append(quotes);
				temp.append(word);
				temp.append(quotes);
			}
			else {
				
				if ((temp + word).length()+6 < THRESHOLD) {
					temp.append(" OR ");
					temp.append(quotes);
					temp.append(word);
					temp.append(quotes);
					
				}
				else  {
					//start new entry
					keywordslist.add(temp.toString());
					temp= new StringBuilder();
					temp.append(quotes);
					temp.append(word);
					temp.append(quotes);
				}
				
			}

		}
		
		else {
			//single word
			
			if (temp.length()==0) {
				//first word in the string
				
				temp.append(word);
				
					
				}
				else {
					
					
					if ((temp + word).length() +4 < THRESHOLD) {
						temp.append(" OR ");
						
						temp.append(word);
						
					}
					
					else  {
						//start new entry
						keywordslist.add(temp.toString());
						temp= new StringBuilder();
						
						temp.append(word);
						
					}
					
				}
			
		}
		
		}

		}
		
		keywordslist.add(temp.toString());
	
		return keywordslist;
	}
	
	public static  ArrayList <String> createKeywordAndPhrasesStrings (List<String> keywords) {
	  
	  ArrayList <String> keywordslist =  new ArrayList <String> ();
		int THRESHOLD = 330;
		StringBuilder temp = new StringBuilder();
		char quotes='"';

		for (int i=0 ; i <keywords.size();i++ ) {
			String word =  keywords.get(i).trim();
			
			if (word.length()>0) {
				//word is phrase only
				if (word.contains(" ") && !word.contains("+")) {
					if (temp.length()==0) {
					//first phrase in the string	
						temp.append(quotes);
						temp.append(word);
						temp.append(quotes);
					}
					else {	
						if ((temp + word).length()+6 < THRESHOLD) {
							temp.append(" OR ");
							temp.append(quotes);
							temp.append(word);
							temp.append(quotes);				
						}
						else  {
							//start new entry
							keywordslist.add(temp.toString());
							temp= new StringBuilder();
							temp.append(quotes);
							temp.append(word);
							temp.append(quotes);
						}				
					}
				}//if word contains only words that do not need to be one after the other in text
				else if(word.contains("+") && !word.contains(" ")){
					//String newWord = word.replace("+", " ");
					int count = StringUtils.countMatches(word, "+");			
					String[] parts = word.split("\\+");
					StringBuilder fullLine = new StringBuilder();
					
					for (int n=0; n<=count; n++) {
						String stringSection = parts[n];
						
						String stringSectionReady = cleanString(stringSection);
						fullLine.append(stringSectionReady);
						if (n!=count) {
							fullLine.append(" ");
						}
					}
					String fullString = fullLine.toString();
					if (temp.length()==0) {
						//first word in the string
						temp.append("(");
						temp.append(fullString);
						temp.append(")");
						
						}else {
							if ((temp + word).length() +6 < THRESHOLD) {
								temp.append(" OR ");
								temp.append("(");
								temp.append(fullString);
								temp.append(")");
								
							}else  {
								//start new entry
								keywordslist.add(temp.toString());
								temp= new StringBuilder();
								temp.append("(");
								temp.append(fullString);
								temp.append(")");
							}
						}
		
				}//if the word contains a mixture of both
				else if (word.contains("+") && word.contains(" ")){
					int count = StringUtils.countMatches(word, "+");			
					String[] parts = word.split("\\+");
					StringBuilder fullLine = new StringBuilder();
					
					for (int n=0; n<=count; n++) {
						String stringSection = parts[n];
						
						String stringSectionReady = cleanString(stringSection);
						fullLine.append(stringSectionReady);
						if (n!=count) {
							fullLine.append(" ");
						}
					}
					String fullString = fullLine.toString();
					
					if (temp.length()==0) {
						//first word in the string	
						temp.append("(");
						temp.append(fullString);
						temp.append(")");
						
						}else {
							if ((temp + word).length()+2 < THRESHOLD) {
								temp.append(" OR ");	
								temp.append("(");
								temp.append(fullString);
								temp.append(")");
								
							}else  {
								//start new entry
								keywordslist.add(temp.toString());
								temp= new StringBuilder();
								temp.append("(");
								temp.append(fullString);	
								temp.append(")");
							}
						}
			
				}else {
					//single word			
					if (temp.length()==0) {
						//first word in the string				
						temp.append(word);
						
						}else {
							if ((temp + word).length() +4 < THRESHOLD) {
								temp.append(" OR ");	
								temp.append(word);
								
							}else  {
								//start new entry
								keywordslist.add(temp.toString());
								temp= new StringBuilder();		
								temp.append(word);
								
							}
						}
					}
			}
		}
		
		keywordslist.add(temp.toString());
	
		return keywordslist;
	}

	//function to check if the sub strings are phrases or single words
	public static String cleanString(String word) {
		  String newString;
		  if (word.contains(" ")) {
			   newString = '"'+word+'"'; 
		  }else {
			  newString = word;
		  }
		  return newString;
	}
	
}
