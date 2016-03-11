package api;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jaspertomas
 */
abstract public class BaseApi implements Runnable {

    public static Integer METHOD_GET = 1;
    public static Integer METHOD_PUT = 2;
    public static Integer METHOD_POST = 3;
    public static Integer METHOD_DELETE = 4;

    abstract public void formulateData(JSONObject json, HttpPost post);

    abstract public void processResponse(JSONObject json);

    abstract public String getUrl();

    abstract public Integer getMethod();

    abstract public void catchHttpResponseException(HttpResponseException e, JSONObject json) throws JSONException;

    abstract public void catchIOException(IOException e, JSONObject json) throws JSONException;

    abstract public void catchJSONException(JSONException e, JSONObject json);

    abstract public JSONObject convertResponseToJson(String response) throws JSONException;

    abstract public String getContextString();
//--------

    public BaseApi() {
    }

    protected JSONObject doInBackground() {
        switch (getMethod()) {
            case 1://METHOD_GET
                return doInBackgroundGet();
            case 2://METHOD_PUT
                return doInBackgroundPut();
            case 3://METHOD_POST
                return doInBackgroundPost();
            case 4://METHOD_DELETE
                return doInBackgroundDelete();
            default:
                return null;
        }
    }

    private JSONObject doInBackgroundDelete() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpDelete post = new HttpDelete(getUrl());
        JSONObject json = new JSONObject();
        String response = null;

        try {
            try {
				// setup the returned values beforehand,
                // in case something goes wrong
                json.put("success", false);
                json.put("error", "Error connecting to server.");

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = client.execute(post, responseHandler);
                if (response == null) {
                    response = "null";
                }
                System.out.println(getContextString() + " response: " + response);
                json = convertResponseToJson(response);
            } catch (HttpResponseException e) {
                catchHttpResponseException(e, json);
            } catch (IOException e) {
                catchIOException(e, json);
            }
        } catch (JSONException e) {
            catchJSONException(e, json);
        }

        return json;
    }

    private JSONObject doInBackgroundPut() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPut post = new HttpPut(getUrl());
        JSONObject json = new JSONObject();
        String response = null;

        try {
            try {
				// setup the returned values beforehand,
                // in case something goes wrong
                json.put("success", false);
                json.put("error", "Error connecting to server.");

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = client.execute(post, responseHandler);
                System.out.println(getContextString() + " response: " + response);
                json = convertResponseToJson(response);
            } catch (HttpResponseException e) {
                catchHttpResponseException(e, json);
            } catch (IOException e) {
                catchIOException(e, json);
            }
        } catch (JSONException e) {
            catchJSONException(e, json);
        }

        return json;
    }

    private JSONObject doInBackgroundGet() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet post = new HttpGet(getUrl());
        JSONObject json = new JSONObject();
        String response = null;

        try {
            try {
				// setup the returned values beforehand,
                // in case something goes wrong
                json.put("success", false);
                json.put("error", "Error connecting to server.");

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = client.execute(post, responseHandler);
                System.out.println(getContextString() + " response: " + response);
                json = convertResponseToJson(response);
            } catch (HttpResponseException e) {
                catchHttpResponseException(e, json);
            } catch (IOException e) {
                catchIOException(e, json);
            }
        } catch (JSONException e) {
            catchJSONException(e, json);
        }

        return json;
    }

    private JSONObject doInBackgroundPost() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(getUrl());
        JSONObject json = new JSONObject();
        String response = null;

        try {
            try {
				// setup the returned values beforehand,
                // in case something goes wrong
                json.put("success", false);
                json.put("error", "Error connecting to server.");

                formulateData(json, post);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = client.execute(post, responseHandler);
                System.out.println(getContextString() + " response: " + response);
                json = convertResponseToJson(response);
            } catch (HttpResponseException e) {
                catchHttpResponseException(e, json);
            } catch (IOException e) {
                catchIOException(e, json);
            }
        } catch (JSONException e) {
            catchJSONException(e, json);
        }

        return json;
    }

    protected void onPostExecute(JSONObject json) {
        processResponse(json);
    }

    public void execute() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        JSONObject json = doInBackground();
        onPostExecute(json);
    }

}
