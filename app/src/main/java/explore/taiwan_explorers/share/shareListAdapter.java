package explore.taiwan_explorers.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2017/1/1.
 */

public class shareListAdapter extends ArrayAdapter<shareDatas> {

    Context context;
    int layoutResourceId;
    //shareDatas data[] = null;
    share_fragment fragment;
    ArrayList<shareDatas> dataGroup;
    shareListHolder holder;
    Bitmap bitmap;
    Bitmap b;
    File localFile;
    //ArrayList<File> ALF;
    //int i = 0;

    public shareListAdapter(Context context, int layoutResourceId, ArrayList<shareDatas> dataGroup, share_fragment fragment) {
        super(context, layoutResourceId, dataGroup);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dataGroup = dataGroup;
        this.fragment = fragment;
        //this.ALF = ALF;
    }

    @Override
    public View getView(int position,final View convertView, ViewGroup parent) {
        View row = convertView;
        holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new shareListHolder();
            holder.sharePic = (ImageView)row.findViewById(R.id.imageViewSharePicure);
            holder.shareTitle = (TextView)row.findViewById(R.id.textViewShareTitle);
            holder.shareContext = (TextView)row.findViewById(R.id.textViewShareContext);
            holder.shareUploader = (TextView)row.findViewById(R.id.textViewUploader);
            holder.shareFlag = (ImageView)row.findViewById(R.id.imageViewShareFlag);
            holder.shareTime = (TextView)row.findViewById(R.id.textViewTime);
            row.setTag(holder);
        }
        else
        {
            holder = (shareListHolder)row.getTag();
        }

        final shareDatas shareDatas = dataGroup.get(position);
        //final File f = ALF.get(position);
        //fileToBitmap(shareDatas);
        StorageReference mStorageRef;
        /*try {
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(shareDatas.picture);
            Glide.with(fragment)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(holder.sharePic);
        } catch(IllegalArgumentException e) {
            new AlertDialog.Builder(getContext())
                    .setTitle("失敗")
                    .setMessage("圖片讀取失敗")
                    .setPositiveButton("刷新頁面", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }*/

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(shareDatas.picture);
        Glide.with(fragment)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .into(holder.sharePic);



        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bb;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inJustDecodeBounds = false;
            bb = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            holder.sharePic.setImageBitmap(bb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        /*StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(shareDatas.picture);
        localFile = null;
        try {
            localFile = File.createTempFile("images" + String.valueOf(i), "jpg");
            i++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //afterLoadPIC(shareDatas);
                Bitmap bb;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    options.inJustDecodeBounds = false;
                    bb = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
                    holder.sharePic.setImageBitmap(bb);
                    //afterLoadPIC(shareDatas);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new AlertDialog.Builder(getContext())
                        .setTitle("失敗")
                        .setMessage("讀取失敗")
                        .setPositiveButton("刷新頁面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });*/

        holder.sharePic.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                //fragment.enlargePIC(bitmap);
                String s = shareDatas.picture.toString();
                fragment.enlargePIC(s);
            }
        });

        holder.shareFlag.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                List<String> option;
                option = new ArrayList<>();
                option.add("資訊");
                option.add("跳轉地圖");

                new AlertDialog.Builder(fragment.getActivity()).setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new AlertDialog.Builder(getContext())
                                        .setTitle("旗標")
                                        .setMessage("標題 : " + shareDatas.flagTitle + "\n" + "描述 : " + shareDatas.flagContext + "\n" + "經度 : " +  shareDatas.latitude + "\n" + "緯度 : " + shareDatas.longtitude)
                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                                break;
                            case 1:
                                ((MainActivity)fragment.getActivity()).searchFlagFragment(shareDatas.latitude,shareDatas.longtitude,shareDatas.flagTitle,shareDatas.flagContext,1,1);
                                break;
                        }

                    }
                }).show();
            }
        });

        holder.shareTitle.setText("標題 : " + shareDatas.title);
        String[] beforeNewline = shareDatas.diary.split("\n", 20);
        String s1 = beforeNewline[0].substring(0,4);
        holder.shareContext.setText("內文 : " + s1 +"...");//.setImageResource(shareDatas.title);
        holder.shareUploader.setText(shareDatas.uploader);
        holder.shareTime.setText(timeString(shareDatas.time));


        return row;
    }

    static class shareListHolder
    {
        ImageView sharePic;
        TextView shareTitle;
        TextView shareContext;
        TextView shareUploader;
        ImageView shareFlag;
        TextView shareTime;
    }

    private String timeString(String s){
        String s1 = s.substring(0,4);
        String s2 = s.substring(4,6);
        String s3 = s.substring(6,8);
        String s4 = s.substring(8,10);
        String s5 = s.substring(10,12);
        String s6 = s.substring(12,14);
        String re = s1 + "/" + s2 + "/" + s3 + " " + s4 + ":" + s5 + ":" + s6;
        return re;

    }

    /*private void afterLoadPIC(final shareDatas shareDatas){
        Bitmap bb;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
            options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inJustDecodeBounds = false;
            bb = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
            holder.sharePic.setImageBitmap(bb);
            //afterLoadPIC(shareDatas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        holder.sharePic.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                fragment.enlargePIC(bitmap);
            }
        });

        holder.shareFlag.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                List<String> option;
                option = new ArrayList<>();
                option.add("資訊");
                option.add("跳轉地圖");

                new AlertDialog.Builder(fragment.getActivity()).setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new AlertDialog.Builder(getContext())
                                        .setTitle("旗標")
                                        .setMessage("標題 : " + shareDatas.flagTitle + "\n" + "描述 : " + shareDatas.flagContext + "\n" + "經度 : " +  shareDatas.latitude + "\n" + "緯度 : " + shareDatas.longtitude)
                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                                break;
                            case 1:
                                ((MainActivity)fragment.getActivity()).searchFlagFragment(shareDatas.latitude,shareDatas.longtitude,shareDatas.flagTitle,shareDatas.flagContext,1,1);
                                break;
                        }

                    }
                }).show();
            }
        });

        holder.shareTitle.setText(shareDatas.title);
        String[] beforeNewline = shareDatas.diary.split("\n", 20);
        holder.shareContext.setText(beforeNewline[0]);//.setImageResource(shareDatas.title);
        holder.shareUploader.setText(shareDatas.uploader);
        holder.shareTime.setText(timeString(shareDatas.time));
    }*/

    /*public synchronized void fileToBitmap(shareDatas s){
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(s.picture);
        localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //afterLoadPIC(shareDatas);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    options.inJustDecodeBounds = false;
                    b = BitmapFactory.decodeStream(new FileInputStream(localFile), null, options);
                    //afterLoadPIC(shareDatas);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Local temp file has been created
                holder.sharePic.setImageBitmap(b);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                new AlertDialog.Builder(getContext())
                        .setTitle("失敗")
                        .setMessage("讀取失敗")
                        .setPositiveButton("刷新頁面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }*/
}
