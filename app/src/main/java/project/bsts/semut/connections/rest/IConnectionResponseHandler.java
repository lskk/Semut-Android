package project.bsts.semut.connections.rest;

public interface IConnectionResponseHandler {
    public void onSuccessJSONObject(String pResult);
    public void onFailure(String e);
}