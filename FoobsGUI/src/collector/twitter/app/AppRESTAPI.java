package collector.twitter.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import collector.db.DAO;
import collector.entity.PlatformEntity;
import collector.entity.SearchDetailsEntity;
import collector.entity.SearchLeafNodeEntity;
import collector.entity.SearchSubNodeEntity;
import collector.main.Config;
import collector.twitter.BaseRESTAPI;
import controller.Controller;
import db.DbDataManager;
import db.SearchInfo;
import scenes.AbstractSearchProgress;
import scenes.ContinuedSearchProgress;
import status.SearchInfoBarVBox;
import twitter4j.GeoLocation;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import utils.FileUtils;

public class AppRESTAPI extends BaseRESTAPI {

	private Controller controller ;
	
	public AppRESTAPI(Controller controller) {
		super(controller);
		this.controller = controller;	
	}

	
	protected void connectToTwitter(Controller controller) {

		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setTweetModeExtended(true);
			cb.setApplicationOnlyAuthEnabled(true);
			cb.setOAuthConsumerKey(controller.getTwitterAppConsumerKey());
			cb.setOAuthConsumerSecret(controller.getTwitterAppConsumerSecret());
			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();
			twitter.getOAuth2Token();
		} catch (TwitterException e) {
			System.out.println(e.getErrorMessage());
		}
	}

	//
	public void saveTweets(Set<Status> tweets, SearchObject so) {
		PlatformEntity twitter = DAO.getPlatfromBasedOnName("Twitter",controller);

		System.out.println("Saving request received for " + tweets.size() + " tweets");

		int count = 0;

		ArrayList threads = new ArrayList();
		Set<Set<Status>> chunks = new HashSet();
		Set<Status> temp_chunk = new HashSet();

		for (Status tweet : tweets) {
			count++;
			temp_chunk.add(tweet);
			if (count % 1000 == 0) {
				chunks.add(temp_chunk);
				//System.out.println("Cutting off at: "+count+ " "+temp_chunk.size());
				temp_chunk= new HashSet();
			}

		}
		chunks.add(temp_chunk);
		
		System.out.println("Processed tweets : "+count);

		System.out.println("Workload split in " + chunks.size() + " chunks");

		
		
		int i =0;
		for (Set<Status> chunk : chunks) {
			i++;
			
			// count++;
			// if (count%1000==0) {
			// System.out.print(".");
			// }
			System.out.println("Saving : "+i );
			System.out.println("Saving : "+chunk.size() + "tweets in one transaction" );
			System.out.println("Start saving at at " + LocalDateTime.now());
			DAO.saveTweetChunks(chunk,so,twitter,controller);
			System.out.println("Finished saving at at " + LocalDateTime.now());
			
		 
		}	
			
			
			/*
			SessionFactory factory = HibernateUtil.getSessionFactory();
			
			SaveTweetsThread thread = new SaveTweetsThread(chunk, twitter, searchDetails,factory);
			// start the thread

			threads.add(thread);

			thread.start();
			
		}

		
		System.out.println("Waiting for threads to finish");
		for (int i = 0; i < threads.size(); i++)
			try {
				((Thread) threads.get(i)).join();
			} catch (InterruptedException e) {
				System.out.println("Threading issue");
				e.printStackTrace();
			}
			*/
        
	}

	/**
	 * 
	 * @param searches 
	 * @param controller 
	 * @param searches
	 * @param continuedSearchProgress 
	 */

	public void searchKeywordListGeoCodedMultipleSearches( ArrayList<SearchObject> searches, Controller controller, SearchInfoBarVBox infoBar) {

		List<Object[]> result = new ArrayList();
		// prepare list of QUERIES from SearchOBJECT list

		
		/*
		for (SearchObject search : searches) {
			Query query = new Query();

			String queryString = search.getKeywords();

			System.out.println("\n---------------GENRATING SEARCH QUERY-----------");
			System.out.println("Search ID: " + search.getUniqueID());
			System.out.println("Keywords: " + queryString);

			infoBar.write("\n---------------GENRATING SEARCH QUERY-----------");
			infoBar.write("Search ID: " + search.getUniqueID());
			infoBar.write("Search ID: " + search.getUniqueID());
			
			query.setQuery(queryString);
			query.setGeoCode(new GeoLocation(search.getLocationId().getGeoPoint().getLatitude(),
					search.getLocationId().getGeoPoint().getLongitude()), search.getRadius(), Unit.km);
			System.out.println(query);
			// NO point bothering with any other number as we will
			// search for all we can get
			query.setCount(100);
			query.setLang("en");

			// create a tuple with query and
			Object temp[] = new Object[2];
			temp[0] = search;
			temp[1] = query;
			result.add(temp);
		}*/
		
		HashMap <String,ArrayList <SearchLeafNodeEntity>> map = controller.newSearchObject.getLeafSearches();
		
		
			
			 for (int i = 0; i<searches.size();i++) {
				 Query query = new Query();

				 String queryString = searches.get(i).getLeafNode().getKeywords();
					
				 System.out.println("\n---------------GENRATING SEARCH QUERY-----------");
				 System.out.println("Search ID: " + searches.get(i).getLeafNode().getLeafSearchLabel());
				 System.out.println("Keywords: " + queryString);

					infoBar.write("\n---------------GENRATING SEARCH QUERY-----------");
					infoBar.write("Search ID: " + searches.get(i).getLeafNode().getLeafSearchLabel());
					infoBar.write("Keywords: " + queryString);
	 
					query.setQuery(queryString);
					
					int subSearchID =searches.get(i).getLeafNode().getSearchSubNodeId();
					System.out.println("Setting location for subsearchID " +subSearchID);
					for (SearchSubNodeEntity node : controller.newSearchObject.getSubSearches()) {
						if (node.getId()==subSearchID) {
							query.setGeoCode(new GeoLocation(node.getLocationId().getRadius().getLatitude(),
									node.getLocationId().getRadius().getLongitude()), node.getLocationId().getRadius().getRadius(), Unit.km);
						}
					}
					
					System.out.println(query);
					// NO point bothering with any other number as we will
					// search for all we can get
					query.setCount(100);
					query.setLang("en");

					// create a tuple with query and
					Object temp[] = new Object[2];
					temp[0] = searches.get(i);
					temp[1] = query;
					result.add(temp);
			 
			 }
		

		searchMultiple(result,infoBar);
	}

	/***
	 * This should be moved to utils
	 * 
	 */
	public  <T> Stream<List<T>> batches(List<T> source, int length) {
	    if (length <= 0)
	        throw new IllegalArgumentException("length = " + length);
	    int size = source.size();
	    if (size <= 0)
	        return Stream.empty();
	    int fullChunks = (size - 1) / length;
	    return IntStream.range(0, fullChunks + 1).mapToObj(
	    		queriesWithSearchDeatils -> source.subList(queriesWithSearchDeatils * length, queriesWithSearchDeatils == fullChunks ? size : (queriesWithSearchDeatils + 1) * length));
	}
	
	/**
	 * 
	 * @param queriesWithSearchDeatils
	 */

	public void searchMultiple(List<Object[]> queriesToPerform, SearchInfoBarVBox infoBar) {
		
		
		// ------------------------------TO BE MOVED-----------
		// !!! this needs to be moved to connect to twitter and pulled from
		// config file
		int RATE_LIMIT = 450;
		// !!! this needs to be moved to connect to twitter and pulled from
		// config file
		long WINDOW_LENGHT = 900000;
		 batches(queriesToPerform, RATE_LIMIT-1).forEach(
	        		queriesWithSearchDeatils -> {
		// -----------------------------------------

		// ------------------------------UTILITY -----------
		boolean newsearch = true;
		long searchStart = 0;
		int requestCounter = 0;

		String resource = "/search/tweets";
		QueryResult result = null;
		// to be removed
		Set<Status> tweets = new HashSet<Status>();
		// ------------------------------END UTILITY -----------

		// to be part of torrage
		Set uniqueTweets = new HashSet();
		List listAllTweets = new ArrayList();

		// ------------------------------STORRAGE (values for storring
		// results)-----------
		// REMEBER one search might have been split in two searches with the
		// same id because
		// of the the keyword list being too long so dont use search id as key
		// in map
		// use the unique id
		HashMap searchWindowResults = new HashMap();

		// to save memory I dont think we need to keep track of all tweets from
		// all searches but we can keep the stats
		HashMap allWindowsResults = new HashMap();

		for (int i = 0; i < queriesWithSearchDeatils.size(); i++) {

			SearchObject so = (SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0];

			searchWindowResults.put(so.getLeafNode().getLeafSearchLabel(), new HashSet());

			allWindowsResults.put(so.getLeafNode().getLeafSearchLabel(), 0);

		}

		// initialise hasmaps with all the searches, use searchID as key

		// ------------------------------END STORRAGE -----------
        boolean firstRun = true; 
        ArrayList <String> previousSearchesXML = new ArrayList <String> ();
        String tempItem = "";
		
        //Split queries into chunks < RATe LIMIT so they can be completed at rate one chunk per window
        
        //Problem that needs to be fixed here is when we have more tasks (i.e. search queries than the rate limit) search window will not execute. We need to split it
        
       
        
        while (true) {

			// check when new search started
			if (newsearch) {
				System.out.println("\n------------NEW SEARCH WINDOW------------ ");
				System.out.println("Starting new search window at " + LocalDateTime.now());
				infoBar.write("\n------------NEW SEARCH WINDOW------------ ");
				infoBar.write("Starting new search window at " + LocalDateTime.now());
				searchStart = System.currentTimeMillis();
				// reset counter
				requestCounter = 0;
				newsearch = false;
			}

			try {

				if (requestCounter == RATE_LIMIT || (requestCounter + queriesWithSearchDeatils.size()) > RATE_LIMIT) {

					for (Object[] search : queriesWithSearchDeatils) {
						SearchObject so = (SearchObject) search[0];
						String idOfSearch = so.getLeafNode().getLeafSearchLabel();
						System.out.println("\n------------SAVING END OF SEARCH WINDOW RESULTS------------ ");
						System.out.println("Search ID:  " + so.getLeafNode().getLeafSearchLabel());
						System.out.println("Group ID: " + so.getLeafNode().getSearchSubNodeId());
						
						infoBar.write("\n------------SAVING END OF SEARCH WINDOW RESULTS------------ ");
						infoBar.write("Search ID:  " +  so.getLeafNode().getLeafSearchLabel());
						infoBar.write("Group ID: " + so.getId());
						saveTweets((HashSet) searchWindowResults.get(idOfSearch), so);

						// reset window results storage variable
						((HashSet) searchWindowResults.get(idOfSearch)).clear();
					}

					System.out.println("\n------------SEARCH WINDOW END------------ ");
					System.out.println("\n Rate limit reached at " + LocalDateTime.now());
					System.out.println("Request Calls made " + requestCounter);
					System.out.println("Next loop needs  " + queriesWithSearchDeatils.size() + " requests");
					System.out.println("Waiting for rate limit to reset.");
					
					infoBar.write("\n------------SEARCH WINDOW END------------ ");
					infoBar.write("\n Rate limit reached at " + LocalDateTime.now());
					infoBar.write("Request Calls made " + requestCounter);
					infoBar.write("Next loop needs  " + queriesWithSearchDeatils.size() + " requests");
					infoBar.write("Waiting for rate limit to reset.");
					long currTime = System.currentTimeMillis();
					long searchLenght = currTime - searchStart;
					System.out.println("Search window took ~ : " + searchLenght / 60000
							+ "minutes (if output 0 then less than 1 minute)");
					
					infoBar.write("Search window took ~ : " + searchLenght / 60000
							+ "minutes (if output 0 then less than 1 minute)");
					if (WINDOW_LENGHT - searchLenght + 3000 <= 0) {
						System.out.println("We wasted  ~ " + ((WINDOW_LENGHT - searchLenght) / 60000)
								+ "minutes waiting for tweets to save");
						infoBar.write("We wasted  ~ " + ((WINDOW_LENGHT - searchLenght) / 60000)
								+ "minutes waiting for tweets to save");
					} else

					{
						System.out.println("Will wait for: ~ " + ((WINDOW_LENGHT - searchLenght) / 60000)
								+ "minutes (if output = 0 then the search will resume in less than a minute)");
						infoBar.write("Will wait for: ~ " + ((WINDOW_LENGHT - searchLenght) / 60000)
								+ "minutes (if output = 0 then the search will resume in less than a minute)");

						Thread.sleep(WINDOW_LENGHT - searchLenght + 3000);
						System.out.println("Resuming search at " + LocalDateTime.now());
						infoBar.write("Resuming search at " + LocalDateTime.now());
						newsearch = true;
						continue;
					}
				}

			} catch (InterruptedException e) {
				System.out.println("Limit exceded wait Thread in searchAPI broken.");
				System.out.println(e.getMessage());
				infoBar.write("Limit exceded wait Thread in searchAPI broken.");
				infoBar.write(e.getMessage());
			}
	
			// loop to perform all searches
			try {
				// System.out.println("------TASKS REMAINING ----------");
				// System.out.println(queriesWithSearchDeatils.size());
				String searchnote = "";
				for (int i = 0; i < queriesWithSearchDeatils.size(); i++) {
					
					// OPTIONAL show progress
					if (requestCounter % 20 == 0) {
						System.out.println("");
					}
					System.out.print(".");

					Query query = (Query) ((Object[]) queriesWithSearchDeatils.get(i))[1];
					SearchObject so = (SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0];

					/*
					if (!searchnote.equals(so.getNote())&&firstRun) {
						if (!searchnote.equals("")) {
							tempItem = tempItem+"</FirstTweetIDsFromPreviousSearch>";
							previousSearchesXML.add(tempItem);
							tempItem="";
						}
						searchnote = so.getNote();
						tempItem = tempItem + "----- Cache report for search "+searchnote+"\n";
						tempItem = tempItem+ "<FirstTweetIDsFromPreviousSearch>";
						
					}*/
					
					requestCounter++;
					result = twitter.search(query);
                    
                    
					tweets.addAll(result.getTweets());

					// System.out.println("Result size for search id " +
					// so.getUniqueID() + " common id " + so.getId()
					// + " : " + result.getTweets().size());

					// ------------------------------STOPPING CONDITION (no more
					// Tweets for one search)-----------
					if (result.getTweets().size() == 0) {
						System.out.println("\n------------COMPLETED SEARCH ALERT------------ ");
						System.out.println("Search ID:  " +  so.getLeafNode().getLeafSearchLabel());
						System.out.println("Group ID: " + so.getLeafNode().getSearchSubNodeId());
						System.out.println("No more Tweets found in this search. Marking as complete...");
						System.out.println("Tweets found in this search window: "
								+ ((HashSet) searchWindowResults.get(  so.getLeafNode().getLeafSearchLabel())).size());

						((SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0]).setCompleted(true);
					}
					// ------------------------------END STOPPING CONDITION (no
					// more Tweets)-----------
					
					

					long maxTweetID = -1;
					
					
					
					
					System.out.println("Cached ID " + so.getLastKonwnCachedID());
					for (Status tweet : tweets) {
						
						//find the max tweet in the first batch of results for all searches
						if (firstRun) {
							
							if (tweet.getId()>maxTweetID ) {
								maxTweetID=tweet.getId(); 
							}
							
						}
						if (tweet.getId() < so.getLastKonwnID()) {
							((SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0])
									.setLastKonwnID(tweet.getId());

						}

						// check if this search contains any ID that we already
						// know
						if (so.getLastKonwnCachedID() != 0 && tweet.getId() <= so.getLastKonwnCachedID()) {
							System.out.println("\n------------BEGINING OF CACHED SEARCH FOUND ------------ ");
							System.out.println("Search ID:  " +  so.getLeafNode().getLeafSearchLabel());
							System.out.println("Group ID: " + so.getLeafNode().getSearchSubNodeId());
							System.out.println("Tweets " + so.getLastKonwnCachedID()
									+ " found -> will not search further in the past, so marking as complete");
							((SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0]).setCompleted(true);
							break;
						}

						((HashSet) searchWindowResults.get(so.getUniqueID())).add(tweet);

					}
					
					if (firstRun) {
						tempItem = tempItem +"<ID>" + maxTweetID + "</ID>";
					}
					

					// System.out.println("Search ID: "+so.getUniqueID());
					// System.out.println("Tweet ID :
					// "+((SearchObject)((Object[])
					// queriesWithSearchDeatils.get(i))[0]).getLastKonwnID());

					long lastRetrievedID = ((SearchObject) ((Object[]) queriesWithSearchDeatils.get(i))[0]).getLastKonwnID();
				//	System.out.println ("for search " + so.getNote() + " with unique ID " + so.getUniqueID());
				//	System.out.println ("Setting last known id (this will be decreased by 1)" + lastRetrievedID);
					
					query.setMaxId(lastRetrievedID - 1);

					// KEEP STATS

					int previouscount = (int) allWindowsResults.get(so.getUniqueID());
					allWindowsResults.put(so.getUniqueID(), previouscount + tweets.size());

					// RESET tweets
					tweets.clear();

					// System.out.println("Request counter: " + requestCounter);
					// System.out
					// .println("Unique Tweets: " + ((HashSet)
					// searchWindowResults.get(so.getUniqueID())).size());
					// System.out.println("All Tweets: " + (int)
					// allWindowsResults.get(so.getUniqueID()));

				}

			} catch (TwitterException e) {
				System.out.println(e.getErrorMessage());

				/*
				 * If rate limit exceeded send to sleep for 15 mins Not ideal as
				 * we should probably record the time of the the last search in
				 * the persistance storage, but this way at least when the rate
				 * limit is exceeded in one run and tool is restarted and hits
				 * the rate limit it will wait until the limiti is restored WE
				 * ARE HOWEVER RISKING OF WAITING FOR MORE THAN WHAT WE NEED TO
				 * IN THE FIRST SLEEP Possible solution might be to inspect
				 * error headers to look for a clue how long need to wait for
				 */
				if (e.exceededRateLimitation()) {
					requestCounter = RATE_LIMIT;
				}
			}

			// MAke SURE Completed gets removed from to do list
			List<Object[]> temp = new ArrayList(queriesWithSearchDeatils);
			for (Object[] search : temp) {
				SearchObject so = (SearchObject) search[0];

				// THREAD FOR SAVING NEEDS TO BE CALLED HERE OTHERWISE
				// IF FINISHED BEFORE WINDOW RATE LIMIT EXCEEDED
				// THE BATCH OF RESULTS FROM THE SEARCH THAT COMPLETED LAST WILL
				// NEVER BE SAVED!!!
				// System.out.println("Should be saving current results now ..
				// ");

				if (so.isCompleted()) {
					String idOfSearch = so.getUniqueID();
					
					if (!((HashSet) searchWindowResults.get(idOfSearch)).isEmpty()) {
					System.out.println("\n------------SAVING LAST BATCH OF SEARCH RESULTS------------ ");
					System.out.println("Search ID:  " + so.getUniqueID());
					//System.out.println("Note:  " + so.getNote());
					System.out.println("Note:  " + so.getLastKonwnID());
					
					System.out.println("Group ID: " + so.getId());
					
					saveTweets((HashSet) searchWindowResults.get(idOfSearch), so);
					}
					// remove from execution queue because completed
					queriesWithSearchDeatils.remove(search);
					// remove results so it does not get saved when rate limit
					// by other searches is completed
					searchWindowResults.remove(idOfSearch);
				}

			}

			

			/*
			if (firstRun) {
			tempItem = tempItem+"</FirstTweetIDsFromPreviousSearch>";
			previousSearchesXML.add(tempItem);
			
			//print xml in file to be included in the config next time searchis ran
			FileUtils.saveCacheReport ( previousSearchesXML);
			}
			*/
			firstRun=false;
			
			// IF all searches completed then exit
						if (queriesWithSearchDeatils.size() == 0) {
							System.out.println("ALL searches in a search chunk COMPLETED ... ");
							break;
						}
			
		}
        		});

	}

	public Set<Status> search(Query query, int numberOfTweets) {
		String resource = "/search/tweets";
		QueryResult result = null;
		Set<Status> tweets = new HashSet<Status>();
		int prevTweetSize = tweets.size();
		long lastId = Long.MAX_VALUE;
		query.setLang("en");
		while (tweets.size() < numberOfTweets) {
			if (numberOfTweets - tweets.size() > 100) {
				// this is the maximum that could be retrieved at once
				query.setCount(100);
			} else {
				query.setCount(numberOfTweets - tweets.size());
			}

			try {
				if (decrementAndCheckRemaining(resource)) {
					result = twitter.search(query);
					prevTweetSize = tweets.size();
					tweets.addAll(result.getTweets());
					// This will ensure that if duplicates are inserted then no
					// more queries
					if (prevTweetSize + result.getTweets().size() > tweets.size() || result.getTweets().size() == 0) {
						break;
					}
					for (Status tweet : tweets) {
						if (tweet.getId() < lastId) {
							lastId = tweet.getId();
						}
					}
					query.setMaxId(lastId - 1);
				} else {
					System.out.println("Twitter Limit exceeded for " + resource + ", wait for "
							+ getSecondsUntilResetForResource(resource) + " seconds");
					Thread.sleep(getSecondsUntilResetForResource(resource) * 1000);
				}
			} catch (TwitterException e) {
				System.out.println(e.getErrorMessage());
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

		return tweets;
	}

	public Set<Status> showTweetsByUser(String userHandle, int numberOfTweets) {
		String resource = "/statuses/user_timeline";
		ResponseList<Status> statuses = null;
		Set<Status> tweets = new HashSet<Status>();
		int prevTweetSize = tweets.size();
		Paging paging = new Paging();
		int page = 1;
		boolean protectedUser = false;
		try {
			protectedUser = getUserByName(userHandle).isProtected();
		} catch (NullPointerException e) {
			System.out.println("No user fount by: " + userHandle);
			return tweets;
		}

		if (!protectedUser) {
			while (tweets.size() < numberOfTweets) {
				paging.setPage(page);
				if (numberOfTweets - tweets.size() > 200) {
					paging.setCount(200);
				} else {
					paging.setCount(numberOfTweets - tweets.size());
				}
				try {
					if (decrementAndCheckRemaining(resource)) {
						statuses = twitter.getUserTimeline(userHandle, paging);
						prevTweetSize = tweets.size();
						tweets.addAll(statuses);
						// This will ensure that if duplicates are inserted then
						// no more queries
						if (prevTweetSize + statuses.size() > tweets.size() || statuses.size() == 0) {
							break;
						}
					} else {
						System.out.println("Twitter Limit exceeded for " + resource + ", wait for "
								+ getSecondsUntilResetForResource(resource) + " seconds");
						Thread.sleep(getSecondsUntilResetForResource(resource) * 1000);
					}
				} catch (TwitterException e) {
					System.out.println(e.getErrorMessage());
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}

				page++;
			}
		} else {
			System.out.println(userHandle + " is protected");
		}

		return tweets;
	}

	public Set<Status> searchList(List<String> words, int numberOfTweets) {
		Query query = new Query();
		String queryString = words.stream().map(Object::toString).collect(Collectors.joining("\" OR \""));
		queryString = "\"" + queryString + "\"";
		System.out.println(queryString);
		query.setQuery(queryString);
		return search(query, numberOfTweets);
	}

	public Set<Status> searchRepliesToUser(String userHandle, int numberOfTweets) {
		Query query = new Query();
		query.setQuery("to:" + userHandle);
		return search(query, numberOfTweets);
	}

	public Set<Status> searchMentionsOfUser(String userHandle, int numberOfTweets) {
		Query query = new Query();
		query.setQuery("@" + userHandle);
		return search(query, numberOfTweets);
	}

	public Set<Status> searchKeywordListGeoCoded(List<String> words, int numberOfTweets, GeoLocation centroid,
			double radius, Unit unit) {

		Query query = new Query();
		String queryString = words.stream().map(Object::toString).collect(Collectors.joining("\" OR \""));
		queryString = "\"" + queryString + "\"";
		System.out.println(queryString);
		query.setQuery(queryString);
		query.setGeoCode(centroid, radius, unit);

		return search(query, numberOfTweets);
	}

	public Set<Status> searchExactString(String string, int numberOfTweets) {
		Query query = new Query();
		query.setQuery("\"" + string + "\"");
		return search(query, numberOfTweets);
	}

	public Set<Status> searchHashTag(String hashtag, int numberOfTweets) {
		Query query = new Query();
		query.setQuery("#" + hashtag);
		return search(query, numberOfTweets);
	}

	public User getUserByName(String twitterHandle) {
		User user = null;
		try {
			user = twitter.showUser(twitterHandle);
		} catch (TwitterException e) {
			e.getStackTrace();
		}
		return user;
	}
}


