package project.bsts.semut.connections.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;


public abstract class ConnectionHandler {

    protected static AsyncHttpClient client = new AsyncHttpClient();
    protected Context mContext;
    protected IConnectionResponseHandler responseHandler;

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, AsyncHttpClient mClient) {
        mClient.get(getAbsoluteUrl(url), params, responseHandler);


    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, AsyncHttpClient mClient) {
        mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, StringEntity params, AsyncHttpResponseHandler responseHandler, AsyncHttpClient mClient, Context context) {
        mClient.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
    }
    public abstract String getAbsoluteUrl(String relativeUrl);
}