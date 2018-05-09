package collector.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.fasterxml.classmate.AnnotationConfiguration;

import Const.GlobalConts;
import controller.Controller;

public class HibernateUtil {
   

   public static SessionFactory getSessionFactory(Controller controller) {
	   SessionFactory sessionFactory;
	   
	   Configuration configuration = new Configuration();
	   configuration.setProperty("hibernate.connection.url", "jdbc:sqlserver://"+GlobalConts.SERVER_NAME+"\\MSSQLSERVER;databaseName="+controller.getDbName()+";integratedSecurity=true;");
		  
	   configuration.configure();
	  /*
	   configuration.setProperty("hibernate.connection.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
	   configuration.setProperty("hibernate.connection.url", "jdbc:sqlserver://"+GlobalConts.SERVER_NAME+"\\MSSQLSERVER;databaseName="+controller.getDbName()+";integratedSecurity=true;");
	   configuration.setProperty("hibernate.connection.pool_size", "200");
	   
	   configuration.setProperty("hibernate.connection.dialect", "org.hibernate.dialect.SQLServerDialect");
	   configuration.setProperty("hibernate.connection.current_session_context_class", "thread");
	   configuration.setProperty("hibernate.connection.show_sql", "false");
	   
	   */
       /*
	   configuration.addClass (collector.entity.AgentEntity.class);
	   configuration.addClass (collector.entity.GeoPointEntity.class);
	   configuration.addClass (collector.entity.LocationEntity.class);
	   configuration.addClass (collector.entity.UserAccountEntity.class);
	   configuration.addClass (collector.entity.PostEntity.class);
	   configuration.addClass (collector.entity.SearchDetailsEntity.class);
	   configuration.addClass (collector.entity.PlatformEntity.class);
	   configuration.addClass (collector.twitter.app.SearchObject.class);
	  */
	  
	   // ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	  // SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	   
	   try {
	       /*  
		    StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		    Metadata metadata =  new MetadataSources(builder.build()).getMetadataBuilder().build();
		    sessionFactory = metadata.getSessionFactoryBuilder().build();
		    //sessionFactory = configuration.buildSessionFactory(builder.build());
		     * 
		     */
		  /*
		   StandardServiceRegistry standardRegistry = 
		            new StandardServiceRegistryBuilder().configure().build();
		  
		         Metadata metadata = 
		            new MetadataSources(standardRegistry).getMetadataBuilder().build();
		         sessionFactory = metadata.getSessionFactoryBuilder().build();*/
		   sessionFactory = configuration.buildSessionFactory();
	      } catch (Throwable e) {
	         throw new ExceptionInInitializerError(e);
	      }
	return sessionFactory;
   }
}
