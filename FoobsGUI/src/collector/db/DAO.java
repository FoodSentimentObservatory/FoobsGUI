package collector.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import collector.entity.AgentEntity;
import collector.entity.PlatformEntity;
import collector.entity.PostEntity;
import collector.entity.SearchDetailsEntity;
import collector.entity.UserAccountEntity;
import controller.Controller;
import twitter4j.Status;

public class DAO {


	public static void saveTweetChunks( Set<Status> tweets, SearchDetailsEntity searchDetails, PlatformEntity platformEntity, Controller controller) {
        Session session = controller.getSessionFactory().getCurrentSession();
        
        Transaction transaction = session.beginTransaction();
		
        try {
        for (Status tweet : tweets) {
        
		UserAccountEntity userAccount = null;
		 UserAccountEntity basicUser = new UserAccountEntity(tweet.getUser());
		/*
		String hql = "from UserAccountEntity uae where uae.platformAccountId=:paid and uae.platformId=:pid";
        
		  
			  
		List<UserAccountEntity> results = session.createQuery(hql, UserAccountEntity.class)
				.setParameter("paid", basicUser.getPlatformAccountId())
				.setParameter("pid", platformEntity)
				.getResultList();
		if (results.size() > 0) {
			userAccount = results.get(0);
		}
		*/
		
			// new user to the system, so initialise it
			basicUser.setPlatformId(platformEntity);
			AgentEntity agent = new AgentEntity();
			agent.setAgentType("Person");
			basicUser.setAgentId(agent);
		
		
		
		session.saveOrUpdate(basicUser);

      
        	 PostEntity post = new PostEntity(tweet);
        	 if (post.getLocationId()!=null) {
        		 session.saveOrUpdate(post.getLocationId());
        		 session.saveOrUpdate(post.getLocationId().getGeoPoint());
        	 }
                    post.setHasCreator(basicUser);
                    post.setSearchDetailsId(searchDetails);
                    session.saveOrUpdate(post);
		  }            
                    
        
                    session.getTransaction().commit();
        } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace();
        } finally {
                    session.close();
        }
}
	
	
	
	
	
	
	public static void saveTweetMultithread( Status tweet, SearchDetailsEntity searchDetails, PlatformEntity platformEntity,SessionFactory factory) {
        Session session = factory.getCurrentSession();
        
        UserAccountEntity userAccount = null;
        UserAccountEntity basicUser = new UserAccountEntity(tweet.getUser());
		Transaction transaction = session.beginTransaction();
		
		
		String hql = "from UserAccountEntity uae where uae.platformAccountId=:paid and uae.platformId=:pid";
        
		  try {
			  
		List<UserAccountEntity> results = session.createQuery(hql, UserAccountEntity.class)
				.setParameter("paid", basicUser.getPlatformAccountId())
				.setParameter("pid", platformEntity)
				.getResultList();
		if (results.size() > 0) {
			userAccount = results.get(0);
		}
		
		
			// new user to the system, so initialise it
			basicUser.setPlatformId(platformEntity);
			AgentEntity agent = new AgentEntity();
			agent.setAgentType("Person");
			basicUser.setAgentId(agent);
		 
		
		
		session.saveOrUpdate(basicUser);

      
        	 PostEntity post = new PostEntity(tweet);
        	 if (post.getLocationId()!=null) {
        		 session.saveOrUpdate(post.getLocationId());
        		 session.saveOrUpdate(post.getLocationId().getGeoPoint());
        	 }
                    post.setHasCreator(basicUser);
                    post.setSearchDetailsId(searchDetails);
                    session.saveOrUpdate(post);
                    session.getTransaction().commit();
        } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace();
        } finally {
                    session.close();
        }
}
	
	
	public static void saveTweet(UserAccountEntity user, Status tweet, SearchDetailsEntity searchDetails, Controller controller) {
        Session session = controller.getSessionFactory().getCurrentSession();
     
        Transaction transaction = session.beginTransaction();

        try {
        	 PostEntity post = new PostEntity(tweet);
        	 if (post.getLocationId()!=null) {
        		 session.saveOrUpdate(post.getLocationId());
        		 session.saveOrUpdate(post.getLocationId().getGeoPoint());
        	 }
                    post.setHasCreator(user);
                    post.setSearchDetailsId(searchDetails);
                    session.saveOrUpdate(post);
                    session.getTransaction().commit();
        } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace();
        } finally {
                    session.close();
        }
}
	public static void saveSearchDetails(SearchDetailsEntity searchDetails, Controller controller){
				Session session = controller.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
		
				try {
					session.saveOrUpdate(searchDetails.getLocationId());
					session.saveOrUpdate(searchDetails.getLocationId().getGeoPoint());
					session.saveOrUpdate("collector.entity.SearchDetailsEntity",searchDetails);
					session.getTransaction().commit();
				} catch (Exception e) {
					transaction.rollback();
					e.printStackTrace();
				} finally {
					session.close();
				}
			}
 //this is identical to next one -> check 
	public static UserAccountEntity getUserAccountByIdAndPlatformMutithread(String platformAccountId,
			PlatformEntity platformEntity, Controller controller) {
		UserAccountEntity userAccount = null;
		Session session = controller.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		String hql = "from UserAccountEntity uae where uae.platformAccountId=:paid and uae.platformId=:pid";
		
		try {

			List<UserAccountEntity> results = session.createQuery(hql, UserAccountEntity.class)
					.setParameter("paid", platformAccountId)
					.setParameter("pid", platformEntity)
					.getResultList();
			if (results.size() > 0) {
				userAccount = results.get(0);
			}
		}
		finally {
			transaction.commit();
			session.close();
		}
		
		return userAccount;

	}
	
	// MARKED for deletion? see above
	public static UserAccountEntity getUserAccountByIdAndPlatform(String platformAccountId,
			PlatformEntity platformEntity, Controller controller) {
		UserAccountEntity userAccount = null;
		Session session = controller.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		String hql = "from UserAccountEntity uae where uae.platformAccountId=:paid and uae.platformId=:pid";
		try {

			List<UserAccountEntity> results = session.createQuery(hql, UserAccountEntity.class)
					.setParameter("paid", platformAccountId)
					.setParameter("pid", platformEntity)
					.getResultList();
			if (results.size() > 0) {
				userAccount = results.get(0);
			}
		} finally {
			session.close();
		}
		return userAccount;

	}

	public static UserAccountEntity saveOrUpdateUserAccountMultithread(UserAccountEntity userAccount, Controller controller) {

		Session session = controller.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		try {
			session.saveOrUpdate(userAccount);
			session.getTransaction().commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
		finally {
			
			session.close();
		}

		return userAccount;
	}
	
	
	public static UserAccountEntity saveOrUpdateUserAccount(UserAccountEntity userAccount, Controller controller) {

		Session session = controller.getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();

		try {
			session.saveOrUpdate(userAccount);
			session.getTransaction().commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return userAccount;
	}



	public static PlatformEntity insertPlatform(PlatformEntity platform, Controller controller) {

		Session session = controller.getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(platform);
			session.getTransaction().commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return platform;
	}

	public static ArrayList<SearchDetailsEntity> getAllSearches( Controller controller) {
		ArrayList <SearchDetailsEntity> results = null;
		//Session session = controller.getSessionFactory().getCurrentSession();
		Session session = controller.getSessionFactory().openSession();
		session.beginTransaction();
		String hql = "from SearchDetailsEntity search ORDER BY search.startOfSearch Desc";
		try {
			
			Query q = session.createQuery(hql,SearchDetailsEntity.class);
			
			results = (ArrayList<SearchDetailsEntity>) q.getResultList();
			
		} finally {
			session.close();
		}
		return results;
	}


	public static PlatformEntity getPlatfromBasedOnName(String name, Controller controller) {
		PlatformEntity platform = null;
		//Session session = controller.getSessionFactory().getCurrentSession();
		Session session = controller.getSessionFactory().openSession();
		session.beginTransaction();
		String hql = "from PlatformEntity platform where platform.forumName=:name";
		try {
			System.out.println(PlatformEntity.class.getName());
			
			Query q = session.createQuery(hql,PlatformEntity.class);
			q.setParameter("name", name);
			
			List<PlatformEntity> results = q.getResultList();
			if (results.size() > 0) {
				platform = results.get(0);
			}
		} finally {
			session.close();
		}
		return platform;
	}

	

	
	
	public static void saveTweet(UserAccountEntity user, Status tweet, Controller controller) {
		Session session = controller.getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();

		try {
			PostEntity post = new PostEntity(tweet);
			post.setHasCreator(user);
			session.saveOrUpdate(post);
			session.getTransaction().commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	/*
	public static void saveNewsArticle(NewsArticle newsarticle) {
		PlatformEntity platform = getPlatfromBasedOnName(newsarticle.getPlatform());
		if (platform == null) {
			System.out.println(newsarticle.getPlatform()+" is not present in platforms, adding.");
			platform = new PlatformEntity("newspaper", newsarticle.getPlatform(), "");
			platform = insertPlatform(platform);
		}
		
		UserAccountEntity userAccount = getUserAccountByIdAndPlatform(newsarticle.getPlatform(), platform);
		if (userAccount == null) {
				AgentEntity agent = new AgentEntity();
				agent.setAgentType("News Publisher");
				// TO DO  - need to check if this account already created
				userAccount = new UserAccountEntity (newsarticle.getPlatform());
				userAccount.setAgentId(agent);
				userAccount.setPlatformId(platform);
		}
		
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();

		try {		
			PostEntity post = new PostEntity(newsarticle);
			//TO DO check if agent already exist - i.e. get it from user account
			//for newspapers account id is the same as platform id - assumption no two newspapers are called the same (probably needs to be handled better in the future - the problem is that the post will need to come with a user account id (like Tweet) but Lexis does not give us this id)
			post.setSearchDetailsId(newsarticle.getSearchDetailsEntity());
			post.setHasCreator(userAccount);
			
			//WE dont have any ids for articles but we will create our own#
			UUID id = UUID.randomUUID();
			String uniqueArticleID = id.toString()+"_system_generated";
			post.setPlatformPostID(uniqueArticleID);
			
			session.saveOrUpdate(post);
			session.getTransaction().commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	*/
	
}
