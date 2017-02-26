package project.bsts.semut.ui;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import project.bsts.semut.R;

public class MapCircle {

    GoogleMap mMap;
    GroundOverlay overlay;
    LatLng location;
    int distances;
    GradientDrawable drawable;
    Context context;
    private Bitmap backgroundImage;

    public MapCircle(Context context, GoogleMap mMap){
        this.mMap = mMap;
        this.context = context;

    }

    public void add(LatLng location, int distances){
        this.location = location;
        this.distances = distances;
        drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.radar_background);
        drawable.setColor(context.getResources().getColor(R.color.rippleBG));
        float d = Resources.getSystem().getDisplayMetrics().density;
        int width = (int) (2 * d);
        drawable.setStroke(width, Color.DKGRAY);
        backgroundImage = drawableToBitmap(drawable);
        overlay = mMap.addGroundOverlay(new
                GroundOverlayOptions()
                .position(location, distances)
                .transparency(0.8f)
                .image(BitmapDescriptorFactory.fromBitmap(backgroundImage)));

    }

    public void setPosition(LatLng pos){
        overlay.setPosition(pos);
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
