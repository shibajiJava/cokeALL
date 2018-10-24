package com.ko.cds.request.csr;

import javax.ws.rs.WebApplicationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.utils.CSRConstants;

public class TagJSON {
	TagObject tagObject = null;
	/*
	public TagObject deserialize(JsonParser jsonparser,DeserializationContext deserializationcontext)
			throws IOException, JsonProcessingException, BadRequestException {
		 JSONObject jo;
		 TagObject tagObject = null;
		 if(jsonparser.getText() != null){
			 tagObject = new TagObject();
			try {
				String json = jsonparser.getText();
				jo = new JSONObject(json);
				 tagObject.setName(jo.getString("name"));
				 tagObject.setValue(jo.getString("value"));
			} catch (JSONException e) {
				
				e.printStackTrace();
				throw new BadRequestException(e.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT);
			}
		 }
			return tagObject;
	}*/
	public TagJSON(String json) throws BadRequestException {
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
				tagObject = new TagObject();
				jo = new JSONObject(json);
				tagObject.setName(jo.getString("name"));
				tagObject.setValue(jo.getString("value"));
		 }
		} catch (JSONException e) {
			e.printStackTrace();
			//throw new BadRequestException(e.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT);
			throw new WebApplicationException(CSRConstants.TAG_OBJECT_ERROR);
		}
	}
	
 public TagObject getDTO(){
	 return tagObject;
 }
}
