package project.bsts.semut.fragments.map;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.api.IGeoPoint;

import java.util.ArrayList;

import project.bsts.semut.R;
import project.bsts.semut.adapters.CctvListAdapter;
import project.bsts.semut.fragments.CctvListFragment;
import project.bsts.semut.map.osm.MapUtilities;
import project.bsts.semut.map.osm.MarkerClick;
import project.bsts.semut.map.osm.OsmMarker;
import project.bsts.semut.pojo.mapview.CctvMap;

public class CctvMapFragment extends Fragment implements Marker.OnMarkerClickListener {

    private ArrayList<CctvMap> list = new ArrayList<CctvMap>();
    private MapView mMapView;
    private MapUtilities mapUitilities;
    private OsmMarker osmMarker;
    private IMapController mapController;
    private MarkerClick markerClick;
    private RelativeLayout markerDetailLayout;

    public CctvMapFragment(){

    }

    public void setData(ArrayList<CctvMap> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View convertView = inflater.inflate(R.layout.fragment_cctv_map, container, false);
        mMapView = (MapView)convertView.findViewById(R.id.maposm);
        markerDetailLayout = (RelativeLayout)convertView.findViewById(R.id.markerdetail_layout);
        markerDetailLayout.setOnClickListener(view -> {
            if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.setVisibility(View.GONE);
        });
        markerClick = new MarkerClick(getActivity(), markerDetailLayout);

        mapUitilities = new MapUtilities(mMapView);
        osmMarker = new OsmMarker(mMapView);
     //   mapController = mapUitilities.init();
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setMultiTouchControls(true);
        mapController = mMapView.getController();
        mapController.setZoom(25);

        for(int i = 0; i < list.size(); i++){
            Marker marker = osmMarker.add(list.get(i));
            marker.setOnMarkerClickListener(this);
            mapController.animateTo(marker.getPosition());
        }

        ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
        for(int i = 0; i < mMapView.getOverlays().size(); i++){
            if(mMapView.getOverlays().get(i) instanceof Marker ) {
                Marker marker = (Marker) mMapView.getOverlays().get(i);
                geoPoints.add(marker.getPosition());
            }
        }

        zoomToBounds(computeArea(geoPoints));


        return convertView;
    }


    public void zoomToBounds(final BoundingBox box) {
        if (mMapView.getHeight() > 0) {
            mMapView.zoomToBoundingBox(box, true);
        } else {
            ViewTreeObserver vto = mMapView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    mMapView.zoomToBoundingBox(box, true);
                    ViewTreeObserver vto2 = mMapView.getViewTreeObserver();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        vto2.removeGlobalOnLayoutListener(this);
                    } else {
                        vto2.removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    public BoundingBox computeArea(ArrayList<GeoPoint> points) {
        double nord = 0, sud = 0, ovest = 0, est = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) == null) continue;
            double lat = points.get(i).getLatitude();
            double lon = points.get(i).getLongitude();
            if ((i == 0) || (lat > nord)) nord = lat;
            if ((i == 0) || (lat < sud)) sud = lat;
            if ((i == 0) || (lon < ovest)) ovest = lon;
            if ((i == 0) || (lon > est)) est = lon;
        }

        return new BoundingBox(nord, est, sud, ovest);

    }


    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markerClick.checkMarker(marker);
        return false;
    }
}
