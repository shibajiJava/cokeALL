package com.ibm.ko.cds.pojo.janrainIntegration;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

public class Clients {
	    private Map<String, String> other = new HashMap<String, String>();
		private String clientId;
		private BigInteger id;
		private String channel;
		private String firstLogin;
		private String lastLogin;
		
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
		public String getClientId() {
			return clientId;
		}
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		public BigInteger getId() {
			return id;
		}
		public void setId(BigInteger id) {
			this.id = id;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getFirstLogin() {
			return firstLogin;
		}
		public void setFirstLogin(String firstLogin) {
			this.firstLogin = firstLogin;
		}
		public String getLastLogin() {
			return lastLogin;
		}
		public void setLastLogin(String lastLogin) {
			this.lastLogin = lastLogin;
		}
		
		
}
