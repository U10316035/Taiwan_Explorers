package explore.taiwan_explorers.Map_Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    Button target;

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
        return inflater.inflate(R.layout.fragment_gmaps_target, container, false);
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
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                final LatLng Point = new LatLng(lat, lon);
                final float zoom = 18;
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

                target = (Button)view.findViewById(R.id.buttonTarget);
                target.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point, zoom));
                    }
                });
            }
        });

    }
}
