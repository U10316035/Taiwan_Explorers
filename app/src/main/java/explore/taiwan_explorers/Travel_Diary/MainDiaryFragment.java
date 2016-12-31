package explore.taiwan_explorers.Travel_Diary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

import static android.content.Context.MODE_PRIVATE;


public class MainDiaryFragment extends Fragment {

    static ArrayList<String> notesArray = new ArrayList<String>();
    static ArrayList<String> displayArray = new ArrayList<String>();
    static ListAdapter theAdapter;
    static ListView theListView;
    static int itemNumber;
    boolean isLeave = true;
    int select = 0;
    private SQLiteDatabase coord = null;
    Button back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary_main,container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("noteSaves");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            notesArray = (ArrayList<String>)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        back = (Button)view.findViewById(R.id.buttonDiaryBack);
        if(select == 1) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    ((MainActivity) getActivity()).editUploadFragment(0);
                    back.setVisibility(View.INVISIBLE);
                }
            });
        }
        coord = getActivity().openOrCreateDatabase("coord1.db",MODE_PRIVATE,null);

        //copying notes array into displayarray but only the first line
        for(String x : notesArray)
        {
            String[] beforeNewline = x.split("\n", 20);
            if(beforeNewline[0].equals(""))
                beforeNewline[0] = "(No title)";


            displayArray.add(beforeNewline[0]);
        }

        theAdapter= new ArrayAdapter<String>(getActivity(),R.layout.list_green_text,
                R.id.list_content,displayArray);

        theListView = (ListView)view.findViewById(R.id.listView);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int position, long id) {
                itemNumber = position;
                if(select==0) {
                    Intent intent = new Intent(getActivity(), ENote.class);
                    startActivity(intent);
                    isLeave = false;
                }else{
                    List<String> option;
                    option = new ArrayList<>();
                    option.add("選擇");
                    option.add("查看內文");

                    new AlertDialog.Builder(getActivity()).setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    new AlertDialog.Builder(getActivity()).setTitle("選擇這篇日記")
                                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String selectedDiary = notesArray.get(itemNumber);
                                                    ContentValues values = new ContentValues();
                                                    values.put("diary", selectedDiary);
                                                    Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
                                                    Cu.moveToPosition(0);
                                                    coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                                                    ((MainActivity) getActivity()).editUploadFragment(1);
                                                }
                                            })
                                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .show();
                                    break;
                                case 1:
                                    ENote e = new ENote(1);
                                    Intent intent = new Intent(getActivity(), e.getClass());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("whichAct",1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    isLeave = false;
                                    break;
                            }

                        }
                    }).show();
                }

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        isLeave=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isLeave)
            displayArray.removeAll(displayArray);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_new) {

            Intent int1 = new Intent();
            int1.setClass(getActivity(), NewNote.class);
            startActivity(int1);
            isLeave=false;
            /*Intent intent = new Intent(getActivity(), NewNote.class);
            startActivity(intent);*/

        }
        return super.onOptionsItemSelected(item);
    }

    public void setIsLeave(){
        isLeave=false;
    }

    public void setSelect(){
        select = 1;
    }

    public void setNotSelect(){
        select = 0;
    }
}
