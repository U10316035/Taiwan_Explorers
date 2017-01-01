package explore.taiwan_explorers.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    ImageView enlarge;
    shareDatas dataAccept;
    ArrayList<shareDatas> dataGroup;
    FirebaseDatabase database;
    DatabaseReference myFirebaseRef;
    shareListAdapter adapter;
    ValueEventListener VE;
    Query queryRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataGroup = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference("post");
        queryRef = myFirebaseRef.orderByChild("time");
        VE = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataAccept = dataSnapshot.getValue(shareDatas.class);
                    dataGroup.add(dataAccept);
                }
                Collections.reverse(dataGroup);
                if(dataGroup.size()!=0) {
                    adapter = new shareListAdapter(getContext(),
                            R.layout.share_fragment_listitem, dataGroup, share_fragment.this);

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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        };

        queryRef.addListenerForSingleValueEvent(VE);

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


    public void enlargePIC(){
        enlarge.setVisibility(View.VISIBLE);
        enlarge.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                enlarge.setVisibility(View.INVISIBLE);
            }
        });
    }
}
