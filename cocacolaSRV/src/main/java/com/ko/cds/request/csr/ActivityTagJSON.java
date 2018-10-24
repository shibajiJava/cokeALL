package com.ko.cds.request.csr;

import javax.ws.rs.WebApplicationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.activity.SearchActivityTagObject;
import com.ko.cds.utils.CSRConstants;

public class ActivityTagJSON {
	SearchActivityTagObject tagObject = null;

	public ActivityTagJSON(String json) throws BadRequestException {
			JSONObject jo;
			// TagObject tagObject = null;
			try {
			 if(json != null){
				if(json.contains("[")){
					JSONArray jsonArray = new JSONArray(json);
					if(jsonArray.length() > 1){
						throw new WebApplicationException(CSRConstants.TAG_SEARCH_LIMIT_DESC);
					}
					//we should parse the multiple json object here ...
				}
					tagObject = new SearchActivityTagObject();
					jo = new JSONObject(json);
					if(jo.has("name")){
						if(jo.getString("name").length()>0){
							tagObject.setName(jo.getString("name"));
						}
					}
					if(jo.has("value")){
						if(jo.getString("value").length()>0){
							tagObject.setValue(jo.getString("value"));
						}
						
					}
					
			 }
			} catch (JSONException e) {
				e.printStackTrace();
				throw new WebApplicationException(CSRConstants.TAG_OBJECT_ERROR);
			}
		}
	
public SearchActivityTagObject getDTO(){
	 return tagObject;
 }

}
