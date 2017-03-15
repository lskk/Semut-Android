package project.bsts.semut.utilities;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;


public class CustomDrawable {
    public static Drawable create(Context context, GoogleMaterial.Icon icon, int size, int colorID){
        return new IconicsDrawable(context)
                .color(context.getResources().getColor(colorID))
                .sizeDp(size)
                .icon(icon);
    }

}
