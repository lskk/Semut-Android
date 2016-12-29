package project.bsts.semut.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import project.bsts.semut.connection.Channel;
import project.bsts.semutservice.LoginReply;
import project.bsts.semutservice.LoginRequest;
import project.bsts.semutservice.UsersManagementGrpc;

/**
 * Created by hynra on 12/29/16.
 */

public class UserManagement {
    private ManagedChannel mChannel;
    public UserManagement(){
        mChannel = Channel.buildChannel();
    }

    public String login(String email, String pass){
        String res = "";
        try {
            UsersManagementGrpc.UsersManagementBlockingStub stub = UsersManagementGrpc.newBlockingStub(mChannel);
            LoginRequest message = LoginRequest.newBuilder().setEmail(email).setPassword(pass).build();
            LoginReply reply = stub.login(message);
            res = reply.getResponse();
        } catch (Exception e) {
            e.getCause();
            res =  e.getMessage();
            JSONObject object = new JSONObject();
            try {
                object.put("success", false);
                object.put("message", "Server tidak merespon atau sedang gangguan, Silahkan coba beberapa saat lagi");
                res = object.toString();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        try {
            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return res;
    }
}
