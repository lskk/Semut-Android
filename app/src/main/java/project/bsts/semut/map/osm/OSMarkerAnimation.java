package project.bsts.semut.map.osm;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class OSMarkerAnimation {
    public void animate(final MapView mapView, final Marker marker, final GeoPoint finalPosition, final long duration) {
        LatLngInterpolator interpolator = new LatLngInterpolator.LinearFixed();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            animateMarkerToGB(mapView, marker, finalPosition, interpolator, duration);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            animateMarkerToHC(mapView, marker, finalPosition, interpolator, duration);
        } else {
            animateMarkerToICS(mapView, marker, finalPosition, interpolator, duration);
        }
    }

    static void animateMarkerToGB(final MapView mapView, final Marker marker, final GeoPoint finalPosition, final LatLngInterpolator latlngInterpolator, final long durationInMs) {
        final GeoPoint startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latlngInterpolator.interpolate(v, startPosition, finalPosition));
                if (marker.isInfoWindowOpen()) {
                    marker.closeInfoWindow();
                    marker.showInfoWindow();
                }
                mapView.invalidate();

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    static void animateMarkerToHC(final MapView mapView, final Marker marker, final GeoPoint finalPosition, final LatLngInterpolator latlngInterpolator, final long durationInMs) {
        final GeoPoint startPosition = marker.getPosition();

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = animation.getAnimatedFraction();
                GeoPoint newPosition = latlngInterpolator.interpolate(v, startPosition, finalPosition);
                marker.setPosition(newPosition);
                if (marker.isInfoWindowOpen()) {
                    marker.closeInfoWindow();
                    marker.showInfoWindow();
                }
                mapView.invalidate();
            }
        });
        valueAnimator.setFloatValues(0, 1); // Ignored.
        valueAnimator.setDuration(durationInMs);
        valueAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    static void animateMarkerToICS(final MapView mapView, final Marker marker, GeoPoint finalPosition, final LatLngInterpolator latlngInterpolator, final long durationInMs) {
        TypeEvaluator<GeoPoint> typeEvaluator = new TypeEvaluator<GeoPoint>() {
            @Override
            public GeoPoint evaluate(float fraction, GeoPoint startValue, GeoPoint endValue) {
                if (marker.isInfoWindowOpen()) {
                    marker.closeInfoWindow();
                    marker.showInfoWindow();
                }
                mapView.invalidate();
                return latlngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, GeoPoint> property = Property.of(Marker.class, GeoPoint.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(durationInMs);
        animator.start();
    }
}