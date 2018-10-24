
package com.ko.cds.pojo.janrainIntegration;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class Profiles{
	private Map<String, String> other = new HashMap<String, String>();
   	private AccessCredentials accessCredentials;
   	private String domain;
   	private List<Followers> followers;
   	private List<Following> following;
   	private List<Friends> friends;
   	private BigInteger id;
   	private String identifier;
   	private Profile profile;
   	private String provider;
   	private String remote_key;
   	@JsonAnyGetter
	public Map<String, String> any() {
		return other;
	}

	@JsonAnySetter
	public void set(String name, String value) {
		other.put(name, value);
	}

	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}
	
 	public AccessCredentials getAccessCredentials(){
		return this.accessCredentials;
	}
	public void setAccessCredentials(AccessCredentials accessCredentials){
		this.accessCredentials = accessCredentials;
	}
 	public String getDomain(){
		return this.domain;
	}
	public void setDomain(String domain){
		this.domain = domain;
	}
 	public List<Followers> getFollowers(){
		return this.followers;
	}
	public void setFollowers(List<Followers> followers){
		this.followers = followers;
	}
 	public List<Following> getFollowing(){
		return this.following;
	}
	public void setFollowing(List<Following> following){
		this.following = following;
	}
 	public List<Friends> getFriends(){
		return this.friends;
	}
	public void setFriends(List<Friends> friends){
		this.friends = friends;
	}
 	public BigInteger getId(){
		return this.id;
	}
	public void setId(BigInteger id){
		this.id = id;
	}
 	public String getIdentifier(){
		return this.identifier;
	}
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
 	public Profile getProfile(){
		return this.profile;
	}
	public void setProfile(Profile profile){
		this.profile = profile;
	}
 	public String getProvider(){
		return this.provider;
	}
	public void setProvider(String provider){
		this.provider = provider;
	}
 	public String getRemote_key(){
		return this.remote_key;
	}
	public void setRemote_key(String remote_key){
		this.remote_key = remote_key;
	}
}
