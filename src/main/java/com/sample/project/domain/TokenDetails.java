package com.sample.project.domain;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/*@Entity
@Table(name = "TokenDetails")*/
@RedisHash("Token")
public class TokenDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4673172872610216370L;

	// @org.springframework.data.annotation.Id
	/*
	 * @GeneratedValue(strategy = GenerationType.AUTO)
	 * 
	 * @Column(name = "ID")
	 */
	// private long id;

	// @Column(name = "TokenId")
	@org.springframework.data.annotation.Id
	private String tokenId;

	// @Column(name = "Subject")
	@Indexed
	private String subject;

	private String token;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/*
	 * public long getId() { return id; }
	 * 
	 * public void setId(long id) { this.id = id; }
	 */

}
