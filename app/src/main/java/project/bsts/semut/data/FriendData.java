package project.bsts.semut.data;

import java.io.Serializable;

/**
 * Created by asepmoels on 5/28/15.
 */
public class FriendData implements Serializable {
    private String name;
    private String email;
    private int id;

    private int relationID;
    private boolean isRequest;
    private boolean needToAdd;

    public FriendData(int id, String name, String email){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    // setter dan getter

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIsRequest(boolean isRequest) {
        this.isRequest = isRequest;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRelationID(int relationID) {
        this.relationID = relationID;
    }

    public int getRelationID() {
        return relationID;
    }

    public void setNeedToAdd(boolean needToAdd) {
        this.needToAdd = needToAdd;
    }

    public boolean isNeedToAdd() {
        return needToAdd;
    }
}
