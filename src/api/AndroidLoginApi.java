/*controller: android
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
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import utils.DataProcessHelper;
import utils.DateHelper;
import utils.MyEncryptionHelper;

import app.Constants;
import app.LoginFrame;

public class AndroidLoginApi extends BaseAndroidLoginApi{
	public AndroidLoginApi() {
	}

	String contextString="AndroidLoginApi";
	public String getContextString(){return contextString;}

	public static void demo(String username,String password)
	{
		AndroidLoginApi api = new AndroidLoginApi();
		api.setUsername(username);//possible values:
		api.setPassword(password);//possible values:
		api.execute();
	}
	@Override public JSONObject convertResponseToJson(String response) throws JSONException
	{
		return new JSONObject(response);
	}
	@Override
	public void processResponse(JSONObject json) {
		
		//save records
		//save username, save password as entered into login page
		try {
			DataProcessHelper.process(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LoginFrame.getInstance().onLoginSuccess();
	}

	@Override
	public void formulateData(JSONObject json, HttpPost post) {
		JSONObject mainobj=new JSONObject();
		try {
	        //add access token to data package
	        accessToken=Constants.accessToken+username+Constants.accessToken2+DateHelper.toString(new Date());
	        accessToken=MyEncryptionHelper.encrypt(accessToken);
			mainobj.put("access_token", accessToken.toString());
			
			//add username to data package
			mainobj.put("username", username);
			mainobj.put("password", password);

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
	
	
}
