package explore.taiwan_explorers;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import explore.taiwan_explorers.DBHelper.DBHelper;
import explore.taiwan_explorers.Map_Fragment.FlagFragment;
import explore.taiwan_explorers.Map_Fragment.GmapFragment;
import explore.taiwan_explorers.DBHelper.DBHelper;
import explore.taiwan_explorers.Map_Fragment.SearchFlagFragment;
import explore.taiwan_explorers.Photo_Album.chooseAct_fragment;
import explore.taiwan_explorers.Photo_Album.gallery;
import explore.taiwan_explorers.Travel_Diary.MainDiaryFragment;
import explore.taiwan_explorers.Travel_Diary.NewNote;
import explore.taiwan_explorers.share.editUploadFragment;
import explore.taiwan_explorers.share.share_fragment;
//import explore.taiwan_explorers.Map_Fragment.NoteFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GmapFragment map = new GmapFragment();
    // FragmentTransaction fragmentTransaction;
    //protected static final int MENU_Flag = Menu.FIRST;
    // private  SQLiteDatabase coord;// = openOrCreateDatabase("coord1.db",MODE_PRIVATE,null);
    private SQLiteDatabase coord = null;
    private String m_Title = "";
    private String m_Text = "";
    Menu menu;
    private MainDiaryFragment diary = new MainDiaryFragment();
    private FlagFragment myFlag = new FlagFragment();
    private editUploadFragment eF = new editUploadFragment();
    Marker marker;

    public int ScreenWidth;
    public int ScreenHeight;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            // TODO: Consider calling

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestLocationPermission(); // 詢問使用者開啟權限
        }/*else
            Toast.makeText(this,"定位中", Toast.LENGTH_LONG).show();*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        ScreenWidth = dm.widthPixels;
        ScreenHeight = dm.heightPixels;

        coord = openOrCreateDatabase("coord1.db",MODE_PRIVATE,null);
        String str = "CREATE TABLE IF NOT EXISTS table01(_id INTEGER PRIMARY KEY AUTOINCREMENT,tit TEXT,txt TEXT,lat DOUBLE,lon DOUBLE)";
        coord.execSQL(str);
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, myFlag).commit();
        fm.beginTransaction().replace(R.id.content_frame, map).commit();
        myFlag.setFlagSQL(coord);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        final int REQUEST_FINE_LOCATION_PERMISSION = 102;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
            }
            else {
                // 啟動地圖與定位元件

            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int REQUEST_WRITE_STORAGE = 112;
            int hasPermission = checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int REQUEST_READ_STORAGE = 113;
            int hasPermission = checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_STORAGE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("離開")
                .setMessage("確定要離開程式?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            finish();
                    }}).setNegativeButton("否", null).show();

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            LinearLayout lila1 = new LinearLayout(this);
            lila1.setOrientation(LinearLayout.VERTICAL);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("登錄坐標");

            final EditText title = new EditText(this);
            final EditText input = new EditText(this);
            TextView text1 = new TextView(MainActivity.this);
            text1.setText("　　標題");
            TextView text2 = new TextView(MainActivity.this);
            text2.setText("　　內文");

            lila1.addView(text1);
            lila1.addView(title);

            lila1.addView(text2);
            lila1.addView(input);

            builder.setView(lila1);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Title = title.getText().toString();
                    m_Text = input.getText().toString();
                    // String str1 = "<font color=\"#3F51B5\">"+ m_Title +"</font>\n" +" <font color=\"#ffcc00\"> " + m_Text +"</font>";

                    // String str2 = String.format("<font color=\"#d40000\">%s",m_Text);


                    if(!m_Title.trim().equals("")&&m_Title.indexOf("'")<=-1){
                        String str = "INSERT INTO table01 (tit,txt,lat,lon) values ('"+m_Title+"','"+ m_Text+"',"+map.a+","+map.b+")";
                        coord.execSQL(str);
                        MarkerOptions marker = new MarkerOptions();
                        map.poi = new LatLng(map.a,map.b);
                        marker.position(map.poi);
                        marker.title(m_Title);
                        marker.snippet(m_Text);
                        marker.visible(true);
                        map.mMap.addMarker(marker);

                        Toast.makeText(getApplicationContext(), m_Title + "已記錄", Toast.LENGTH_LONG).show();


                        myFlag.fhelper.insert("標題 : " + " " + m_Title + "\n" + "描述 : " + " " + m_Text);

                        myFlag.cursor.requery();
                        myFlag.cursorAdapter.notifyDataSetChanged();

                    }else if(m_Title.indexOf("'")>-1){
                        Toast.makeText(getApplicationContext(), "不可包含單引號", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "請輸入標題", Toast.LENGTH_LONG).show();
                    }



                    /*Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);
                    Cur.moveToPosition(0);
                    //map.poi = new LatLng(Cur.getDouble(3),Cur.getDouble(4));
                    Toast.makeText(getApplicationContext(),Cur.getDouble(3) + "333已記錄", Toast.LENGTH_LONG).show();
                    Cur.moveToPosition(0);
                    //map.poi = new LatLng(Cur.getDouble(3),Cur.getDouble(4));
                    Toast.makeText(getApplicationContext(),Cur.getString(1) + "333已記錄", Toast.LENGTH_LONG).show();*/
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // m_Title = title.getText().toString();
                    // m_Text = input.getText().toString();
                    //Toast.makeText(,m_Title, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });

            builder.show();
        } /*else if (id == R.id.removeall) {
            removeAllData(myNote.helper,myNote.cursor,myNote.cursorAdapter,"清除日記內容");

        }*/else if (id == R.id.removeallflag) {
            removeAllData(myFlag.fhelper,myFlag.cursor,myFlag.cursorAdapter,"清除所有旗標");

        } else if (id == R.id.exit) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("離開此程式")
                    .setMessage("你確定要離開？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }else if (id == R.id.share) {
            menu.clear();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, eF).commit();
        }else if (id == R.id.action_new) {

            Intent int1 = new Intent();
            int1.setClass(this, NewNote.class);
            startActivity(int1);
            diary.setIsLeave();
            /*Intent intent = new Intent(getActivity(), NewNote.class);
            startActivity(intent);*/

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        // FragmentManager fm = getFragmentManager();
        /// fragmentTransaction=getSupportFragmentManager().beginTransaction();
        // map = new GmapFragment();
        int id = item.getItemId();

        //  if (id == R.id.nav_camara) {
          /*  if(!map.getmMap().equals(null)){
            }*/
        menu.clear();
        // fragmentTransaction.replace(R.id.map,new FlagFragment());
        // fm.beginTransaction().replace(R.id.content_frame, new FlagFragment()).commit();
        if (id == R.id.nav_gallery) {


            //fragmentTransaction.replace(R.id.content_frame,map);
            //  fragmentTransaction.addToBackStack(null);
            //  fragmentTransaction.commit();
            fm.beginTransaction().replace(R.id.content_frame, map).commit();
            onResume();
            menu.clear();
            getMenuInflater().inflate(R.menu.main, menu);
        } else if (id == R.id.flag) {
            menu.clear();
            getMenuInflater().inflate(R.menu.flag, menu);
            myFlag.setSelect(0);
            fm.beginTransaction().replace(R.id.content_frame, myFlag).commit();
        } else if (id == R.id.photo) {
                menu.clear();
                fm.beginTransaction().replace(R.id.content_frame, new chooseAct_fragment()).commit();
        }else if (id == R.id.share) {
                menu.clear();
                getMenuInflater().inflate(R.menu.share, menu);
                fm.beginTransaction().replace(R.id.content_frame, new share_fragment()).commit();
        }else if (id == R.id.exit) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("離開此程式")
                    .setMessage("你確定要離開？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } else if (id == R.id.diary) {
            /*Intent int1 = new Intent();
            int1.setClass(MainActivity.this, gallery.class);
            startActivity(int1);*/
            menu.clear();
            diary.setNotSelect();
            fm.beginTransaction().replace(R.id.content_frame, diary).commit();
            getMenuInflater().inflate(R.menu.menu_diary_main, menu);
            /*menu.clear();
            fm.beginTransaction().replace(R.id.content_frame, diary).commit();
            menu.removeItem(R.id.action_settings);
            getMenuInflater().inflate(R.menu.note, menu);*/
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://taiwan_explorers.explore/main"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://explore.taiwan_explorers/http://taiwan_explorers.explore/main")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setFlagAfterReaady(){
        if(map.mapReadyOrNot()) {
            Cursor Cur = coord.rawQuery("SELECT * FROM table01", null);
            for (int i = 0; i < Cur.getCount(); i++) {
                //Toast.makeText(this,i+"", Toast.LENGTH_LONG).show();
                Cur.moveToPosition(i);
                MarkerOptions marker = new MarkerOptions();
                map.poi = new LatLng(Cur.getDouble(3), Cur.getDouble(4));
                marker.position(map.poi);
                marker.title(Cur.getString(1));
                marker.snippet(Cur.getString(2));
                marker.visible(true);
                map.mMap.addMarker(marker);
            }
            map.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(final LatLng arg0) {
                    // TODO Auto-generated method stub
                    LinearLayout lila1 = new LinearLayout(MainActivity.this);
                    lila1.setOrientation(LinearLayout.VERTICAL);


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("登錄坐標");

                    final EditText title = new EditText(MainActivity.this);
                    final EditText input = new EditText(MainActivity.this);
                    TextView text1 = new TextView(MainActivity.this);
                    text1.setText("　　標題");
                    TextView text2 = new TextView(MainActivity.this);
                    text2.setText("　　內文");

                    lila1.addView(text1);
                    lila1.addView(title);

                    lila1.addView(text2);
                    lila1.addView(input);

                    builder.setView(lila1);


                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Title = title.getText().toString();
                            m_Text = input.getText().toString();
                            // String str1 = "<font color=\"#3F51B5\">"+ m_Title +"</font>\n" +" <font color=\"#ffcc00\"> " + m_Text +"</font>";

                            // String str2 = String.format("<font color=\"#d40000\">%s",m_Text);


                            if(!m_Title.trim().equals("")&&m_Title.indexOf("'")<=-1){
                                String str = "INSERT INTO table01 (tit,txt,lat,lon) values ('"+m_Title+"','"+ m_Text+"',"+arg0.latitude+","+arg0.longitude+")";
                                coord.execSQL(str);
                                MarkerOptions marker = new MarkerOptions();
                                map.poi = new LatLng(arg0.latitude,arg0.longitude);
                                marker.position(map.poi);
                                marker.title(m_Title);
                                marker.snippet(m_Text);
                                marker.visible(true);
                                map.mMap.addMarker(marker);

                                Toast.makeText(getApplicationContext(), m_Title + "已記錄", Toast.LENGTH_LONG).show();
                                myFlag.fhelper.insert("標題 : " + " " + m_Title + "\n" + "描述 : " + " " + m_Text);
                                myFlag.cursor.requery();
                                myFlag.cursorAdapter.notifyDataSetChanged();

                                map.mMap.addMarker(marker);

                            }else if(m_Title.indexOf("'")>-1){
                                Toast.makeText(getApplicationContext(), "不可包含單引號", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "請輸入標題", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://taiwan_explorers.explore/main"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://explore.taiwan_explorers/http://taiwan_explorers.explore/main")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void removeAllData(final DBHelper helper, final Cursor cursor, final SimpleCursorAdapter cursorAdapter, String title){
        new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("清除將會遺失所有資料，確定要清除？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteAll();
                        cursor.requery();
                        cursorAdapter.notifyDataSetChanged();

                        if(helper.equals(myFlag.fhelper)){
                            coord.delete("table01",null,null);
                            map.mMap.clear();
                        }
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public int getScreenWidth(){
        return ScreenWidth;
    }

    public int getScreenHeight(){
        return ScreenHeight;
    }

    public void searchFlagFragment(double la,double lo,String ti,String co,int i,int i2){
        FragmentManager fm = getSupportFragmentManager();
        SearchFlagFragment s = new SearchFlagFragment();
        s.setLat(la);
        s.setLon(lo);
        s.setString(ti,co);
        if(i==1) {
            s.setWhichAct();
            s.setWhichAct2(i2);
        }
        fm.beginTransaction().replace(R.id.content_frame, s).commit();
        menu.clear();
    }

    public void selectFlagFragment(){
        FragmentManager fm = getSupportFragmentManager();
        myFlag.setSelect(1);
        fm.beginTransaction().replace(R.id.content_frame, myFlag).commit();
        menu.clear();
    }

    public void shareFragment(){
        FragmentManager fm = getSupportFragmentManager();
        menu.clear();
        getMenuInflater().inflate(R.menu.share, menu);
        fm.beginTransaction().replace(R.id.content_frame, new share_fragment()).commit();
    }

    public void editUploadFragment(int i){
        FragmentManager fm = getSupportFragmentManager();
        if(i == 1){
            eF.setWhichAct();
        }
        fm.beginTransaction().replace(R.id.content_frame, eF).commit();
        menu.clear();
    }

    public void selectDiaryFragment(){
        FragmentManager fm = getSupportFragmentManager();
        diary.setSelect();
        menu.clear();
        fm.beginTransaction().replace(R.id.content_frame, diary).commit();
    }
}