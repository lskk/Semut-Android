package project.bsts.semut.pojo;


import android.graphics.drawable.Drawable;

public class MainMenuObject {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Object getClassIntent() {
        return classIntent;
    }

    public void setClassIntent(Object classIntent) {
        this.classIntent = classIntent;
    }

    public String title;
    public Drawable icon;
    public Object classIntent;

}
