package explore.taiwan_explorers.Photo_Album;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2016/12/22.
 */

public class gallery extends Activity {
    Gallery g = null;
    List<String> it = null;// 遍歷符合條件的清單
    public String actionUrl = null;
    ImageView i;
    ImageAdapter IA;
    Bitmap bmp;
    Button but;

    public int ScreenWidth;
    public int ScreenHeight;

    private final String SD_PATH = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES + "/TaiwanExplorer";

    public void onCreate(Bundle savedInstanceState) {
        IA = new ImageAdapter(this, getSD());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        i=(ImageView)findViewById(R.id.image1);

        but = (Button) findViewById(R.id.buttonBUT);
        but.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenWidth = dm.widthPixels;
        ScreenHeight = dm.heightPixels;

        g = (Gallery) findViewById(R.id.gallery);
//添加一個ImageAdapter並設置給Gallery物件
        g.setAdapter(IA);
//設置一個itemclickListener並Toast被按一下圖片的位置
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(gallery.this,
                        "序列：" + (position + 1) + "\n路徑：" + it.get(position),
                        Toast.LENGTH_LONG).show();

                bmp = BitmapFactory.decodeFile(it.get(position).toString());
                if(bmp.getWidth()>ScreenWidth) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = (int) (ScreenWidth / 1.5);
                    options.outHeight = (int) (ScreenHeight / 1.5);
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeFile(it.get(position).toString(), options);//= IA.getBitmap(); //
                }
                adjustPIC(bmp, bmp.getWidth(), bmp.getHeight());
                i.setImageBitmap(bmp);

                /*Toast.makeText(gallery.this, it.get(position),
                        Toast.LENGTH_LONG).show();*/
            }
        });

        g.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                final int position = pos;
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(gallery.this);

                builder.setTitle("刪除圖片");
                builder.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(it.get(position).toString());
                        file.delete();
                        onResume();
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });

                builder.show();
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        this.onCreate(null);
    }

    private void adjustPIC(Bitmap bp, int Width, int Height) {
        Matrix matrix = new Matrix();
        if (Width > Height) {
            matrix.setRotate(90);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bp.getWidth(), bmp.getHeight(), matrix, true);
        }
    }

    //遍歷SD卡中某一路徑下指定類型的圖片
    private List<String> getSD() {
        it = new ArrayList<String>();
        /*File f = new File(SD_PATH + "//" + "picture");
        File[] files = f.listFiles();*/

        FilenameFilter mediafilefilter = new FilenameFilter(){
            private String[] filter = {".jpg",".png"};
            @Override
            public boolean accept(File dir, String filename) {
                for(int i= 0;i< filter.length ; i++){
                    if(filename.indexOf(filter[i]) != -1)return true;
                }
                return false;
            }};

        File mediaDiPath = new File(SD_PATH);
        if (!mediaDiPath.exists())mediaDiPath.mkdir();
        File[] mediaInDir = mediaDiPath.listFiles(mediafilefilter);
        /*int x=0;
        do {
            String tmp = x + ".png";
            f = new File(SD_PATH ,tmp);
            x++;
            it.add(f.getPath());
        }while(isPicExist(f.toString()));*/

        for (int i = 0; i < mediaInDir.length; i++) {
            File file = mediaInDir[i];
            if (getImageFile(file.getPath()))
                it.add(file.getPath());
        }
        return it;
    }

    public boolean isPicExist(String thispath) {
        boolean exist = false;
        File file = new File(thispath);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    //指定遍歷檔案類型
    private boolean getImageFile(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            re = true;
        } else {
            re = false;
        }
        return re;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
