package explore.taiwan_explorers.Photo_Album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

import static android.R.attr.path;
import static android.app.Activity.RESULT_OK;

/**
 * Created by no_name on 2016/12/11.
 */
public class chooseAct_fragment  extends Fragment {

    Button shot;
    Button album;
    ImageView iv;
    Button savePIC;
    Button throwPIC;

    int ScreenWidth;
    int ScreenHeight;
    int PicWidth;
    int PicHeight;
    String path;
    //String prepath;

    File preSaveFile;

    private Matrix matrix;
    Bitmap bmp;

    int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_act,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        shot.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                //使用Intent調用其他服務幫忙拍照
                //Toast.makeText(getActivity(),preSaveFile.toString(), Toast.LENGTH_LONG).show();
                index = 0;
                initFilePath();
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent_camera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(preSaveFile));
                startActivityForResult(intent_camera, 0);
            }

        });


        album.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                index = 1;
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //Uri uri = Uri.parse(path);
                //intent.setDataAndType(uri, "*/*");
                //startActivity(Intent.createChooser(intent, "Open folder"));
                /*Intent i=new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                startActivity(i);*/

                /*final int PHOTO = 99 ;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PHOTO);*/


                Intent int1 = new Intent();
                int1.setClass(getActivity(), gallery.class);
                startActivity(int1);
                //getActivity().finish();
            }

        });

        ScreenWidth = ((MainActivity)getActivity()).getScreenWidth();
        ScreenHeight = ((MainActivity)getActivity()).getScreenHeight();
    }

    /*@Override
    public void onResume(){
        super.onResume();
        init();
    }*/

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照後顯示圖片
        if(index==0) {
            if (resultCode == RESULT_OK) {
            /*//取出拍照後回傳資料
            Bundle extras = data.getExtras();
            //將資料轉換為圖像格式
            bmp = (Bitmap) extras.get("data");
            //載入ImageView

            PicWidth = bmp.getWidth();
            PicHeight = bmp.getHeight();

            adjustPIC(iv,PicWidth,PicHeight);
            iv.setImageBitmap(bmp);
            iv.setVisibility(View.VISIBLE);*/
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bmp = BitmapFactory.decodeFile(preSaveFile.toString(), options);
            /*try {
                adjustDegree(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
                PicWidth = bmp.getWidth();
                PicHeight = bmp.getHeight();
                adjustPIC(iv, PicWidth, PicHeight);
                iv.setImageBitmap(bmp);
                iv.setVisibility(View.VISIBLE);

                savePIC.setVisibility(View.VISIBLE);
                throwPIC.setVisibility(View.VISIBLE);

                shot.setVisibility(View.INVISIBLE);
                album.setVisibility(View.INVISIBLE);

                savePIC.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        //save("/Pictures/Taiwan_explorer");
                        //save(path);

                        shot.setVisibility(View.VISIBLE);
                        album.setVisibility(View.VISIBLE);
                        savePIC.setVisibility(View.INVISIBLE);
                        throwPIC.setVisibility(View.INVISIBLE);
                        iv.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "照片已儲存", Toast.LENGTH_LONG).show();
                    }
                });

                throwPIC.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        delete(preSaveFile.toString());
                        shot.setVisibility(View.VISIBLE);
                        album.setVisibility(View.VISIBLE);
                        savePIC.setVisibility(View.INVISIBLE);
                        throwPIC.setVisibility(View.INVISIBLE);
                        iv.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "照片已捨棄", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Toast.makeText(this.getActivity(), "失敗", Toast.LENGTH_LONG).show();
        }

        //覆蓋原來的Activity
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adjustPIC(ImageView img, int Width, int Height) {
        float d;
        matrix = new Matrix();
        if(Width > Height) {
            d = (float)Width/(float)ScreenHeight;
            if(d>1){
                matrix.setScale(1/(d+0.5f), 1/(d+0.5f));
                //bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                matrix.setRotate(90);
            }
//            matrix.setRotate(90);
            //bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }else {
            d = (float) Height / (float) ScreenHeight;
            if(d>1) {
                matrix.setScale(1 / (d + 0.5f), 1 / (d + 0.5f));
            }
        }
       // Toast.makeText(getActivity(),String.valueOf(d),Toast.LENGTH_LONG).show();
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        /*ViewGroup.LayoutParams params = img.getLayoutParams();
        params.width = Width;
        params.height = Height;
        img.setLayoutParams(params);*/

    }

    private void init(){
        shot = (Button) getView().findViewById(R.id.buttonShot);
        album = (Button) getView().findViewById(R.id.buttonAlbum);
        iv = (ImageView)getActivity().findViewById(R.id.imagePhoto);
        savePIC = (Button)getActivity().findViewById(R.id.buttonSave);
        throwPIC = (Button)getActivity().findViewById(R.id.buttonThrow);

        shot.setVisibility(View.VISIBLE);
        album.setVisibility(View.VISIBLE);
        savePIC.setVisibility(View.INVISIBLE);
        throwPIC.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.INVISIBLE);

        path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_PICTURES + "/TaiwanExplorer";
        isDirExist(path);
        //isDirExist(Environment.getExternalStorageDirectory().getAbsolutePath()+"/tessdata/");
        //isDirExist("/Pictures/Taiwan_explorer");
    }

    private void initFilePath(){
        int i=0;
        do {
            String tmp = i + ".png";
            preSaveFile = new File(path ,tmp);
            i++;
        }while(isPicExist(preSaveFile.toString()));
        //prepath = preSaveFile.toString();
    }

    public void isDirExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean t;
            t = file.mkdirs();
            file.mkdir();
            if(!t)
               Toast.makeText(getActivity(),"創建目錄失敗,圖片將無法儲存",Toast.LENGTH_LONG).show();
            //Toast.makeText(getActivity(),path, Toast.LENGTH_LONG).show();
            /*Toast.makeText(getActivity(),"mkdir", Toast.LENGTH_LONG).show();*/
        }
    }

    public boolean isPicExist(String thispath) {
        boolean exist = false;
        File file = new File(thispath);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    /*private void adjustDegree(Bitmap b)
            throws IOException {
                ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotateImage(b, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotateImage(b, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotateImage(b, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:

            default:
                break;
        }


    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }*/

    private void save(String path){
        try {
            // 開啟檔案
            File file;
            int i=0;
            do {
                String tmp = i + ".png";
                file = new File(path ,tmp);
                i++;
            }while(isPicExist(file.toString()));
            //File file = new File("/sdcard/Image.png");
            // 開啟檔案串流
            FileOutputStream out = new FileOutputStream(file);
            // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
            bmp.compress (Bitmap.CompressFormat.PNG , 100 , out);
            // 刷新並關閉檔案串流
            out.flush ();
            out.close ();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
    }

    private void delete(String thispath) {
        File file = new File(thispath);
        file.delete();
    }
}


