package com.sample.project.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/*@Entity
@Table(name = "UserDetails")*/
@RedisHash("AppUserDetails")
public class AppUserDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3404248171021242767L;

	//@Id
	/*@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UserDetailsId")*/
	//private int userDetailsId;

	//@Column(name = "EmailId")
	@Id
	private String emailId;

	//@Column(name = "Password")
	@Indexed
	private String password;

	/*public int getUserId() {
		return userDetailsId;
	}

	public void setUserId(int userDetailsId) {
		this.userDetailsId = userDetailsId;
	}*/

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
