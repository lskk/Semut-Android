package project.bsts.semut.connections.rest;


import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import project.bsts.semut.R;
import project.bsts.semut.setup.Constants;

public class RequestRest extends ConnectionHandler {

    protected static AsyncHttpClient mClient = new AsyncHttpClient();
    private String TAG = this.getClass().getSimpleName();


    public RequestRest(Context context, IConnectionResponseHandler handler) {
        this.mContext = context;
        this.responseHandler = handler;
    }


    @Override
    public String getAbsoluteUrl(String relativeUrl) {
        return Constants.REST_BASE_URL + relativeUrl;
    }


    public void login(String user, String pin){
        RequestParams params = new RequestParams();
        params.put("Email", user);
        params.put("Password", pin);
        Log.i(TAG, params.toString());
        post(Constants.REST_USER_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG, "Sending request");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Success");
                responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, e, errorResponse);
                Log.e(TAG, "Failed");
                responseHandler.onFailure(String.valueOf(statusCode));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG, "Disconnected");
            }

        }, mClient);
    }
}
