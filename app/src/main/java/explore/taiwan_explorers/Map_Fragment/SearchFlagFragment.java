package explore.taiwan_explorers.Map_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2016/12/29.
 */

public class SearchFlagFragment extends SupportMapFragment {
    public GoogleMap mMap;
    double lat;
    double lon;
    String tit;
    String con;

    public void setLat(double la){
        lat = la;
    }

    public void setLon(double lo){
        lon = lo;
    }

    public void setString(String ti, String co){
        tit = ti;
        con = co;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googlemap) {
                // TODO Auto-generated method stub
                mMap = googlemap;
                LatLng Point = new LatLng(lat, lon);
                float zoom = 18;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point, zoom));
                MarkerOptions marker = new MarkerOptions();
                LatLng poi = new LatLng(lat, lon);
                marker.position(poi);
                marker.title(tit);
                marker.snippet(con);
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                marker.visible(true);
                mMap.addMarker(marker);
            }
        });

    }
}
