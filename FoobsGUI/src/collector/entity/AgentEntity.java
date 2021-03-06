package collector.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Agent")
public class AgentEntity {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO,generator="native")
   @GenericGenerator(name="native",strategy="native")
   private Long Id;

   @Column(name="agentType")
   private String agentType;

   

   @OneToOne(fetch=FetchType.EAGER,mappedBy="agentId",cascade=CascadeType.ALL)
   private UserAccountEntity userAccount;

   /**
    * @return the id
    */
   public Long getId() {
      return Id;
   }

   /**
    * @return the agentType
    */
   public String getAgentType() {
      return agentType;
   }

   /**
    * @param agentType the agentType to set
    */
   public void setAgentType(String agentType) {
      this.agentType = agentType;
   }

  

   /**
   * @return the userAccount
   */
   public UserAccountEntity getUserAccount() {
      return userAccount;
   }

   /**
    * @param userAccount the userAccount to set
    */
   public void setUserAccount(UserAccountEntity userAccount) {
      this.userAccount = userAccount;
   }

}
