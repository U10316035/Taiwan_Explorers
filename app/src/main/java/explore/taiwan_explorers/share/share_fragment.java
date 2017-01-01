package explore.taiwan_explorers.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;
import explore.taiwan_explorers.Travel_Diary.ENote;

/**
 * Created by no_name on 2016/12/14.
 */
public class share_fragment  extends Fragment {

    private ListView listView1;
    AlertDialog.Builder messageLoading;
    AlertDialog dialog;
    Button button;
    ImageView enlarge;
    Bitmap bitmap;//enlarge bitmap
    shareDatas dataAccept;
    ArrayList<shareDatas> dataGroup;
    //ArrayList<File> ALF;
    FirebaseDatabase database;
    DatabaseReference myFirebaseRef;
    shareListAdapter adapter;
    ValueEventListener VE;
    Query queryRef;

    int ScreenWidth;
    int ScreenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showMessage();

        ScreenWidth = ((MainActivity)getActivity()).getScreenWidth();
        ScreenHeight = ((MainActivity)getActivity()).getScreenHeight();

        dataGroup = new ArrayList<>();
        //ALF = new ArrayList<>();//alf
        database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference("post");
        queryRef = myFirebaseRef.orderByChild("time");
        VE = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                messageLoading.setTitle("有人上傳新文章囉").
                        setPositiveButton("完成", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dataGroup.removeAll(dataGroup);
                //ALF.removeAll(ALF);//alf
                //int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataAccept = dataSnapshot.getValue(shareDatas.class);
                    dataGroup.add(dataAccept);
                    /*StorageReference mStorageRef;                                                         //alf
                    mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(dataAccept.picture);    //alf
                    try {
                        ALF.add(File.createTempFile("images" + String.valueOf(i), "jpg"));
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
                Collections.reverse(dataGroup);
                //Collections.reverse(ALF);
                if(dataGroup.size()!=0) {
                    adapter = new shareListAdapter(getContext(),
                            R.layout.share_fragment_listitem, dataGroup, share_fragment.this);
                    /*adapter = new shareListAdapter(getContext(),
                            R.layout.share_fragment_listitem, dataGroup, share_fragment.this,ALF);*/

                    listView1 = (ListView) getView().findViewById(R.id.listViewShare);
                    listView1.setAdapter(adapter);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                            new AlertDialog.Builder(getContext())
                                    .setTitle("查看內文")
                                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ENote e = new ENote(1);
                                            Intent intent = new Intent(getActivity(), e.getClass());
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("whichAct",1);
                                            bundle.putInt("whichAct2",1);
                                            bundle.putString("diaryText",dataGroup.get(position).diary);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();

                        }
                    });
                }
                button.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        };

        queryRef.addValueEventListener(VE);

        /*myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataGroup.removeAll(dataGroup);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataAccept = dataSnapshot.getValue(shareDatas.class);
                    dataGroup.add(dataAccept);
                }
                shareListAdapter adapter = new shareListAdapter(getContext(),
                        R.layout.share_fragment_listitem, dataGroup,share_fragment.this);

                listView1 = (ListView)getView().findViewById(R.id.listViewShare);
                listView1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });*/
        enlarge = (ImageView)view.findViewById(R.id.imageViewShareEnlarge);
        /*ArrayList<shareDatas> dataGroup = new ArrayList<>();
        dataGroup.add(dataAccept);*/
        /*shareDatas data[] = new shareDatas[]
                {
                        new shareDatas("title","uploader", "flagtitle", "flagcontext", 0, 0, "diary", "pic"),
                        new shareDatas("title1","uploader1", "flagtitle1", "flagcontext1", 0, 0, "diary1", "pic"),
                        new shareDatas("title","uploader2", "flagtitle2", "flagcontext", 0, 0, "diary2", "pic"),
                        new shareDatas("title","uploader3", "flagtitle3", "flagcontext", 0, 0, "diary2", "pic"),
                };*/
    }

    @Override
    public void onStop(){
        super.onStop();
        queryRef.removeEventListener(VE);
    }


    public void enlargePIC(String s){//Bitmap b){
        //enlarge.setImageBitmap(b);
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(s);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .into(enlarge);
        enlarge.setVisibility(View.VISIBLE);
        enlarge.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                enlarge.setVisibility(View.INVISIBLE);
            }
        });
        /*BitmapDrawable drawable = ((BitmapDrawable) enlarge.getDrawable());
        bitmap = drawable.getBitmap();
        adjustPIC(enlarge,bitmap.getWidth(),bitmap.getHeight());*/
    }

    private void showMessage(){
        messageLoading = new AlertDialog.Builder(getActivity());
        messageLoading.setTitle("讀取中").
                setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
         //
        dialog = messageLoading.create();
        dialog.show();
        dialog.setCancelable(false);
        button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);
    }

    /*private void adjustPIC(ImageView img, int Width, int Height) {
        Matrix matrix;
        float d;
        matrix = new Matrix();
        if(Width > Height) {
            d = (float)Width/(float)ScreenHeight;
            if(d>1){
                matrix.setScale(1/(d+0.5f), 1/(d+0.5f));
                matrix.setRotate(90);
            }
        }else {
            d = (float) Height / (float) ScreenHeight;
            if(d>1) {
                matrix.setScale(1 / (d), 1 / (d));
            }
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        enlarge.setImageBitmap(bitmap);
        enlarge.setVisibility(View.VISIBLE);
        enlarge.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                enlarge.setVisibility(View.INVISIBLE);
            }
        });
    }*///adjust pic if width > height
}
