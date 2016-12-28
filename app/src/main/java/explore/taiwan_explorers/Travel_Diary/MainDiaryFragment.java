package explore.taiwan_explorers.Travel_Diary;

import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.R;


public class MainDiaryFragment extends Fragment {

    static ArrayList<String> notesArray = new ArrayList<String>();
    static ArrayList<String> displayArray = new ArrayList<String>();
    static ListAdapter theAdapter;
    static ListView theListView;
    static int itemNumber;

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

        //copying notes array into displayarray but only the first line
        for(String x : notesArray)
        {
            String[] beforeNewline = x.split("\n", 20);
            if(beforeNewline[0]=="")
                beforeNewline[0] = "(empty)";


            displayArray.add(beforeNewline[0]);
        }

        theAdapter= new ArrayAdapter<String>(getActivity(),R.layout.list_green_text,
                R.id.list_content,displayArray);

        theListView = (ListView)view.findViewById(R.id.listView);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                itemNumber = position;
                Intent intent = new Intent(getActivity(), ENote.class);
                startActivity(intent);
            }
        });
    }
    /*@Override
    //Get everything ready
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);

        try {
            FileInputStream fileInputStream = openFileInput("noteSaves");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            notesArray = (ArrayList<String>)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //copying notes array into displayarray but only the first line
        for(String x : notesArray)
        {
            String[] beforeNewline = x.split("\n", 20);


            displayArray.add(beforeNewline[0]);
        }

        theAdapter= new ArrayAdapter<String>(this,R.layout.list_green_text,
                R.id.list_content,displayArray);

        theListView = (ListView)findViewById(R.id.listView);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                itemNumber = position;
                Intent intent = new Intent(MainDiaryActivity.this, ENote.class);
                startActivity(intent);
            }
        });


    }*/

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_diary_main, menu);
        return true;/**/


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
            /*Intent intent = new Intent(getActivity(), NewNote.class);
            startActivity(intent);*/

        }
        return super.onOptionsItemSelected(item);
    }
}
