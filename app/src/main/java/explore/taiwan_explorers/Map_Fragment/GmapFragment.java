package explore.taiwan_explorers.Map_Fragment;

/**
 * Created by no_name on 2016/12/11.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

import static android.content.Context.MODE_PRIVATE;


public class GmapFragment extends SupportMapFragment implements LocationListener, OnMapReadyCallback {


    public GoogleMap mMap;
    float zoom;
    private LocationManager locMgr;
    String bestProv;
    public double a, b;
    public LatLng poi;
    boolean readyOrNot = false;
    //  Context mContext;

    SQLiteDatabase coord = null;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readyOrNot = false;

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googlemap) {
                // TODO Auto-generated method stub
                mMap = googlemap;
                afterReady();
                readyOrNot = true;
                ((MainActivity) getActivity()).setFlagAfterReaady();
            }
        });
        /*if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }*/
        //mMap = fragment.getMap();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        do {
            bestProv = locMgr.getBestProvider(criteria, true);
        } while (bestProv == null);
        //Toast.makeText(this.getActivity(),bestProv, Toast.LENGTH_LONG).show();
        //fragment.getMapAsync(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locMgr.requestLocationUpdates(bestProv, 1000, 0, this);
        //coord  = openOrCreateDatabase("coord1.db",MODE_PRIVATE,null);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locMgr.removeUpdates(this);
    }

    void afterReady(){
        if(locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            if(ContextCompat.checkSelfPermission(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                if (locMgr.getLastKnownLocation("network") == null) {
                    showAlert();
                }else {
                    //locMgr.requestSingleUpdate("network", this, null);
                    locMgr.requestSingleUpdate("network", this, null);
                    Toast.makeText(this.getActivity(), "定位中", Toast.LENGTH_LONG).show();
                    Location location = locMgr.getLastKnownLocation("network");
                    a = location.getLatitude();
                    b = location.getLongitude();
                    if (location != null) {
                        LatLng Point = new LatLng(location.getLatitude(), location.getLongitude());
                        zoom = 18;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point, zoom));
                        locMgr.removeUpdates(this);
                        try {
                            locMgr.requestLocationUpdates(bestProv,1000,0,this);
                        }catch (SecurityException e) {
                        }
                    } else {
                    }
                    //Toast.makeText(this.getActivity(),"請到空曠的地點", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(this.getActivity(),"請開啟定位服務", Toast.LENGTH_LONG).show();
        }
    }

    private void showAlert(){
        new AlertDialog.Builder(this.getActivity())
                .setTitle("請開啟服務")
                .setMessage("請開啟網路定位服務\n前往設定頁，請按照 設定->個人化->位置")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getActivity().finish();
                    }
                })
                .setNegativeButton("設定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                })
                .show();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            //locMgr.removeUpdates(this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

    @Override
    public void onLocationChanged(Location location) {
        String x = "緯度 =" + Double.toString(location.getLatitude());
        String y = "經度 =" + Double.toString(location.getLongitude());
        a = location.getLatitude();
        b = location.getLongitude();
        LatLng Point = new LatLng(location.getLatitude(),location.getLongitude());
        zoom = 18;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point,zoom));
        //mMap.setMyLocationEnabled(true);
        Toast.makeText(this.getActivity(),"目前位置", Toast.LENGTH_LONG).show();
        Toast.makeText(this.getActivity(),x+"\n"+y,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria,true);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //setUpMap();
    }


    //  @Override
   /* public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }*/
   /* @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng marker = new LatLng(-33.867, 151.206);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));

       googleMap.addMarker(new MarkerOptions().title("Hello Google Maps!").position(marker));
    }*/
    public GoogleMap getmMap(){
        return mMap;
    }

    public boolean mapReadyOrNot(){
        return readyOrNot;
    }

    /*public void setUpMap(){

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }*/

}
