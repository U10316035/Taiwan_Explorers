package explore.taiwan_explorers.share;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by no_name on 2016/12/29.
 */

public class editUploadFragment  extends Fragment {
    private SQLiteDatabase coord = null;

    Button buttonSelectPic;
    Button buttonSelectFlag;
    Button buttonSelectDiary;
    Button buttonSend;
    Button buttonClear;
    TextView textFlagInfor;
    TextView textDiary;
    EditText title;
    EditText uploader;
    int ScreenWidth;
    int ScreenHeight;
    int picWidth;
    int picHeight;
    ImageView iv;
    ImageView ivE;
    String picString = "";
    Bitmap bmp;
    int whichAct = 0;
    String tit;
    String con;
    double lat;
    double lon;
    boolean resumeOrNot = false;//is program pass through onResume()
    Cursor Cu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_upload_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buttonSelectPic = (Button)view.findViewById(R.id.buttonSelectPic);
        buttonSelectFlag = (Button)view.findViewById(R.id.buttonSelectFlag);
        buttonSelectDiary = (Button)view.findViewById(R.id.buttonSelectDiary);
        buttonSend = (Button)view.findViewById(R.id.buttonSend);
        buttonClear = (Button)view.findViewById(R.id.buttonClear);
        textFlagInfor = (TextView)view.findViewById(R.id.textViewFlagInfor);
        textDiary = (TextView)view.findViewById(R.id.textViewDiary);
        title = (EditText) view.findViewById(R.id.editTextTit);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus) {//onfocus
                } else {// else
                    //title.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
                }
            }
        });
        uploader = (EditText) view.findViewById(R.id.editTextName);
        uploader.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus) {//onfocus
                } else {// else
                    //uploader.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(uploader.getWindowToken(), 0);
                }
            }
        });
        iv = (ImageView)view.findViewById(R.id.imageUpload);
        ivE = (ImageView)view.findViewById(R.id.imageViewEnlarge);
        init();

        /* send information and after action */
        buttonSend.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("送出?");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(title.getText().toString().equals("") || uploader.getText().toString().equals("") || textFlagInfor.getText().toString().equals("待選擇...")
                                || textDiary.getText().toString().equals("待選擇...") || picString.equals("")) {
                            new android.support.v7.app.AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("離開")
                                    .setMessage("不可有欄位為空")
                                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }else {
                            sendToFirebase();
                            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
                            Cu.moveToPosition(0);
                            ContentValues values = new ContentValues();
                            values.put("title", "5k4g4xj04a83");
                            title.setText("");
                            values.put("tit", "5k4g4xj04a83");
                            values.put("txt", "0");
                            values.put("lat", 0);
                            values.put("lon", 0);
                            values.put("diary", "5k4g4xj04a83");
                            values.put("pic", "0");
                            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                            ((MainActivity) getActivity()).shareFragment();
                        }
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        buttonClear.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("全部清除");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
                        Cu.moveToPosition(0);
                        ContentValues values = new ContentValues();
                        values.put("title", "5k4g4xj04a83");
                        title.setText("");
                        values.put("uploadername", "5k4g4xj04a83");
                        uploader.setText("");
                        values.put("tit", "5k4g4xj04a83");
                        values.put("txt", "0");
                        values.put("lat", 0);
                        values.put("lon", 0);
                        values.put("diary", "5k4g4xj04a83");
                        values.put("pic", "0");
                        coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(editUploadFragment.this).attach(editUploadFragment.this).commit();
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        buttonSelectPic.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.DIRECTORY_PICTURES );
                intent.setDataAndType(uri, "image/png");
                //startActivity(Intent.createChooser(intent, "Open folder"));
                startActivityForResult(intent,0);

            }
        });

        buttonSelectDiary.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                ((MainActivity)getActivity()).selectDiaryFragment();
            }
        });

        buttonSelectFlag.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                ((MainActivity)getActivity()).selectFlagFragment();
            }
        });

        ScreenWidth = ((MainActivity)getActivity()).getScreenWidth();
        ScreenHeight = ((MainActivity)getActivity()).getScreenHeight();
        picWidth = iv.getLayoutParams().width;
        picHeight = iv.getLayoutParams().height;
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeOrNot = true;
        Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
        Cu.moveToPosition(0);
        /* 上傳者名稱資訊 */
        String temp = Cu.getString(1);
        if(!temp.equals("5k4g4xj04a83"))
            title.setText(Cu.getString(1));

        /* 上傳者名稱資訊 */
        temp = Cu.getString(2);
        if(!temp.equals("5k4g4xj04a83"))
            uploader.setText(Cu.getString(2));

        /* 上傳圖片資訊 */
        temp = Cu.getString(8);
        if(!temp.equals("0") && isPicExist(temp)){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(temp,options);
            iv.setImageBitmap(bmp);
            iv.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    ivE.setVisibility(View.VISIBLE);
                    ivE.setImageBitmap(bmp);
                }
            });
            ivE.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    ivE.setImageBitmap(null);
                    ivE.setVisibility(View.INVISIBLE);
                }
            });
            picString = temp;
        }

        /* 上傳旗標資訊 */
        temp = Cu.getString(3);
        if(!temp.equals("5k4g4xj04a83")) {
            setFlagInfor();
        }

        /* 上傳日記資訊 */
        temp = Cu.getString(7);
        if(!temp.equals("5k4g4xj04a83")) {
            setDiaryInfor();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!uploader.getText().equals("")) {
            ContentValues values = new ContentValues();
            values.put("uploadername", uploader.getText().toString());
            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
            Cu.moveToPosition(0);
            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
        }

        if(!title.getText().equals("")) {
            ContentValues values = new ContentValues();
            values.put("title", title.getText().toString());
            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
            Cu.moveToPosition(0);
            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result)
    {
        if (resultCode == RESULT_OK)
        {
            Uri data = result.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            /*options.outWidth = ScreenWidth;
            options.outHeight = ScreenHeight;*/
            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            picString = getPath(data);
            ContentValues values = new ContentValues();
            values.put("pic", picString);
            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
            Cu.moveToPosition(0);
            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
            bmp = BitmapFactory.decodeFile(picString,options);
            iv.setImageBitmap(bmp);

            iv.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    ivE.setVisibility(View.VISIBLE);
                    ivE.setImageBitmap(bmp);
                }
            });
            ivE.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    ivE.setImageBitmap(null);
                    ivE.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void init(){
        coord = getActivity().openOrCreateDatabase("coord1.db",MODE_PRIVATE,null);
        String create = "CREATE TABLE IF NOT EXISTS tableEditUpload(_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,uploadername TEXT,tit TEXT,txt TEXT,lat DOUBLE,lon DOUBLE,diary TEXT,pic TEXT)";
        coord.execSQL(create);
        Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);

       // int i = Cu.getCount();
        if(Cu.getCount() == 0) {
            String insert = "INSERT INTO tableEditUpload (title,uploadername,tit,txt,lat,lon,diary,pic) values ('" + "5k4g4xj04a83" + "','" + "5k4g4xj04a83" + "','" + "5k4g4xj04a83" + "','" + 0 + "'," + 0 + "," + 0 + ",'" + "5k4g4xj04a83" + "','" + 0 + "'" + ")";
            coord.execSQL(insert);
        }
        if(whichAct==1){
            /*假設已有資料傳遞所執行的動作*/
            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
            Cu.moveToPosition(0);
            String temp = Cu.getString(3);
            if(!temp.equals("5k4g4xj04a83"))
                setFlagInfor();
            temp = Cu.getString(7);
            if(!temp.equals("5k4g4xj04a83"))
                setDiaryInfor();
            whichAct = 0;
        }
    }

    public void setWhichAct(){
        whichAct = 1;
    }

    public boolean isPicExist(String thispath) {
        boolean exist = false;
        File file = new File(thispath);
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    public void setFlagInfor(){
        Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
        Cu.moveToPosition(0);
        String temp = Cu.getString(3);
        textFlagInfor.setText(temp);
        textFlagInfor.invalidate();
        tit = Cu.getString(3);
        con = Cu.getString(4);
        lat = Cu.getDouble(5);
        lon = Cu.getDouble(6);
        textFlagInfor.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("旗標資訊")
                        .setMessage("標題 : " + tit + "\n" + "描述 : " + con + "\n" + "經度 : " +  lat + "\n" + "緯度 : " + lon)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    public void setDiaryInfor(){
        Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
        Cu.moveToPosition(0);
        final String temp = Cu.getString(7);
        String[] beforeNewline = temp.split("\n", 20);
        if(beforeNewline[0].equals("") && whichAct == 1) {
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle("日記第一行不可為空")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("diary", "5k4g4xj04a83");
                            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                            textDiary.setText("待選擇...");
                        }
                    })
                    .show();
        }else {
            textDiary.setText(beforeNewline[0]);
        }
        textDiary.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("日記資訊")
                        .setMessage(temp)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    public void sendToFirebase(){
        // Setup Firebase library
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setAndroidContext((MainActivity)getActivity());

        // Creating a Firebase database Reference
        // TODO Need to change “your_reference_path” to your own path
        DatabaseReference myFirebaseRef = database.getReference("post");

        Calendar mCal = Calendar.getInstance();
        String dateformat = "yyyyMMddkkmmss";
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        String today = df.format(mCal.getTime());

        Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
        Cu.moveToPosition(0);


        myFirebaseRef = database.getReference("post");
        myFirebaseRef = myFirebaseRef.push();//.child(Integer.toString(i));
        shareDatas datas = new shareDatas(title.getText().toString() , uploader.getText().toString() , Cu.getString(3) , Cu.getString(4) , Cu.getDouble(5), Cu.getDouble(6), Cu.getString(7), Cu.getString(8) ,today);
        myFirebaseRef.setValue(datas);

    }
}
