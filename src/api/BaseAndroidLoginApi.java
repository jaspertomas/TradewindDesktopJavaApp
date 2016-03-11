/*usage:
AndroidLoginApi api = new AndroidLoginApi(context);
api.setAccessToken("");//possible values:
api.setUsername("");//possible values:
api.setPassword("");//possible values:
api.setMessageLoading("message here...");
api.execute();

controller: android
name: login
description: 
return type:  (None)
method: POST
path: /android/login
Parameters:
access_token
username
password

*/
package api;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import utils.UrlHelper;

import app.Constants;
import app.Constants;
public abstract class BaseAndroidLoginApi extends BaseApi{
	String contextString="BaseAndroidLoginApi";
	public String getContextString(){return contextString;}
	String accessToken;//options: []
	String username;//options: []
	String password;//options: []
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public BaseAndroidLoginApi() {
	}

	@Override
	public void formulateData(JSONObject json, HttpPost post) {
		JSONObject mainobj=new JSONObject();
		try {
			if(accessToken!=null)mainobj.put("access_token", accessToken.toString());
			if(username!=null)mainobj.put("username", username.toString());
			if(password!=null)mainobj.put("password", password.toString());
			StringEntity se = new StringEntity(mainobj.toString());
			post.setEntity(se);
			
     // setup the request headers
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");
     
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override public String getUrl() {
		String url=Constants.getServerUrl()+"/android/login";
		return UrlHelper.escapeUrl(url);	}
	@Override public Integer getMethod() {
		return BaseApi.METHOD_POST;
	}
	@Override
	public void processResponse(JSONObject json) {
            System.out.println(json);
        }
	@Override public JSONObject convertResponseToJson(String response) throws JSONException
	{
		return new JSONObject();
	}


}