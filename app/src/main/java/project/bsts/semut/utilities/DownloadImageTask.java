package project.bsts.semut.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    View loading;

    public DownloadImageTask(ImageView bmImage, View loading) {
        this.bmImage = bmImage;
        if(loading != null) this.loading = loading;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        urldisplay = urldisplay.replace("push-ios", "247");
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        if(loading != null) loading.setVisibility(View.GONE);
    }
}
