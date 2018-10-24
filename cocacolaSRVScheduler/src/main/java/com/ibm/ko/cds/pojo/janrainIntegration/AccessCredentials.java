
package com.ibm.ko.cds.pojo.janrainIntegration;


public class AccessCredentials{
   	private String accessToken;
   	private Number expires;
   	private String scopes;
   	private String type;
   	private String uid;

   	private String oauthToken;
   	private String oauthTokenSecret;
   	private String clientId;
   	private String refreshToken;
   	
 	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getOauthToken() {
		return oauthToken;
	}
	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}
	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}
	public void setOauthTokenSecret(String oauthTokenSecret) {
		this.oauthTokenSecret = oauthTokenSecret;
	}
	public String getAccessToken(){
		return this.accessToken;
	}
	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}
 	public Number getExpires(){
		return this.expires;
	}
	public void setExpires(Number expires){
		this.expires = expires;
	}
 	public String getScopes(){
		return this.scopes;
	}
	public void setScopes(String scopes){
		this.scopes = scopes;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
 	public String getUid(){
		return this.uid;
	}
	public void setUid(String uid){
		this.uid = uid;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
