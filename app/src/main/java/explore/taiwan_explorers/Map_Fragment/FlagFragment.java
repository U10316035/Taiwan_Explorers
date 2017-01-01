package explore.taiwan_explorers.Map_Fragment;

/**
 * Created by no_name on 2016/12/11.
 */
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.DBHelper.DBHelper;
import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

public class FlagFragment extends Fragment {

    //private EditText inputText;
    public ListView listInput;
    public DBHelper fhelper;
    public Cursor cursor;
    public SimpleCursorAdapter cursorAdapter;
    private List<String> option;
    private List<String> option_1;
    private boolean isScrollFoot = true;
    private SQLiteDatabase coord;
    int select;
    Button back;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flag,container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = (Button)view.findViewById(R.id.buttonFlagBack);
        initFlagDB();
        initView();
    }

    private void initFlagDB(){
        fhelper = new DBHelper(getActivity().getApplicationContext(),"flag_database");
        cursor = fhelper.select();
        listInput = (ListView)getActivity().findViewById(R.id.listInputText);
        listInput.setDividerHeight(1);
        cursorAdapter = new SimpleCursorAdapter(getActivity().getApplication(),
                R.layout.flag_adapter, cursor,
                new String[]{"item_text"},
                new int[]{R.id.text},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

       /* cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                SpannableStringBuilder style = new SpannableStringBuilder("1234567891");

                style.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.RED), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView) view).setText(style);
                return false;
            }
        });*/
    }

    private void initView(){
        option = new ArrayList<>();
        if(select == 0) {
            option.add(getActivity().getApplicationContext().getString(R.string.modify));
            option.add(getString(R.string.delete));
            option.add("前往地圖");
        }else {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v) {
                    ((MainActivity)getActivity()).editUploadFragment(0);
                    back.setVisibility(View.INVISIBLE);
                }
            });
            option.add("選擇此旗標");
            option.add("前往地圖");
        }

        option_1 = new ArrayList<>();
        option_1.add("title");
        option_1.add("context");
        // inputText = (EditText)getActivity().findViewById(R.id.inputText);
        listInput = (ListView)getActivity().findViewById(R.id.listInputText);
        listInput.setAdapter(cursorAdapter);




        listInput .setOnScrollListener( new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    //最底
                    isScrollFoot = true;
                } else {
                    isScrollFoot = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isScrollFoot ) { // 滾動靜止且滾動到最底部
                    //停在最底部
                    Toast. makeText(getActivity(), "繼續探險吧", Toast. LENGTH_SHORT).show();
                } else {

                    // 不是停在最底部
                }
            }
        });

        listInput.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                //Toast.makeText(getActivity(),position+"", Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),cursor.getPosition()+"", Toast.LENGTH_LONG).show();
                final int pos = position;
                //cursor.moveToPosition(1);

                /*cursor*/
                final Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);
                Cur.moveToPosition(pos);
                /*cursor*/

                if(select == 0) {
                    new AlertDialog.Builder(getActivity())
                            .setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0://modify
                                            final View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_layout, null);
                                            final EditText editText = (EditText) item.findViewById(R.id.edittext);
                                            editText.setText(cursor.getString(1));
                                        /* alertdialog */
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                                            LinearLayout lila1 = new LinearLayout(getContext());
                                            lila1.setOrientation(LinearLayout.VERTICAL);
                                            builder.setTitle("Edit flag");
                                            final EditText title = new EditText(getContext());
                                            title.setText(Cur.getString(1));
                                            final EditText input = new EditText(getContext());
                                            input.setText(Cur.getString(2));
                                            TextView text1 = new TextView(getContext());
                                            text1.setText("　　Title");
                                            TextView text2 = new TextView(getContext());
                                            text2.setText("　　context");
                                            lila1.addView(text1);
                                            lila1.addView(title);
                                            lila1.addView(text2);
                                            lila1.addView(input);
                                            builder.setView(lila1);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                /*title.getText().toString();
                                                m_Text = input.getText().toString();*/
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    cursor.moveToPosition(pos);
                                                    fhelper.update(cursor.getInt(0), "標題 : " + " " + title.getText().toString() + "\n" + "描述 : " + " " + input.getText().toString());
                                                    cursor.requery();
                                                    cursorAdapter.notifyDataSetChanged();

                                                    //Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);

                                                    //Toast.makeText(this,i+"", Toast.LENGTH_LONG).show();

                                                    //Cur.moveToPosition(pos);
                                                    //String[] AfterSplit = editText.getText().toString().split(" ");
                                                    ContentValues values = new ContentValues();
                                                /*values.put("tit",AfterSplit[0]);
                                                values.put("txt",AfterSplit[1]);*/
                                                    values.put("tit", title.getText().toString());
                                                    values.put("txt", input.getText().toString());
                                                    coord.update("table01", values, "_id=" + Cur.getInt(0), null);
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                            builder.show();
                                        /* end alertdialog  */


                                        /*new AlertDialog.Builder(getActivity())
                                                .setTitle("修改內容")
                                                .setView(item)
                                                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        fhelper.update(cursor.getInt(0), editText.getText().toString());
                                                        cursor.requery();
                                                        cursorAdapter.notifyDataSetChanged();

                                                        Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);

                                                        //Toast.makeText(this,i+"", Toast.LENGTH_LONG).show();

                                                        Cur.moveToPosition(pos);
                                                        String[] AfterSplit = editText.getText().toString().split(" ");
                                                        ContentValues values = new ContentValues();
                                                        values.put("tit",AfterSplit[0]);
                                                        values.put("txt",AfterSplit[1]);
                                                        coord.update("table01",values,"_id="+Cur.getInt(0),null);




                                                    }
                                                })
                                                .show();*/
                                            break;
                                        case 1://delete
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("刪除列")
                                                    .setMessage("你確定要刪除？")
                                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            fhelper.delete(cursor.getInt(0));
                                                            cursor.requery();
                                                            cursorAdapter.notifyDataSetChanged();

                                                            //Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);

                                                            //Toast.makeText(this,i+"", Toast.LENGTH_LONG).show();

                                                            //Cur.moveToPosition(pos);
                                                            //Toast.makeText(getActivity(),i+"", Toast.LENGTH_LONG).show();
                                                            // String p = Integer.toString(pos);
                                                            // Toast.makeText(getActivity(),p, Toast.LENGTH_LONG).show();
                                                            coord.delete("table01", "_id=" + Cur.getInt(0), null);


                                                        }

                                                        //  Toast.makeText(getActivity(),cursor.getColumnCount(), Toast.LENGTH_LONG).show();


                                                    })
                                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();
                                            break;
                                        case 2://set map
                                            String tit = Cur.getString(1);
                                            String con = Cur.getString(2);
                                            double lat = Cur.getDouble(3);
                                            double lon = Cur.getDouble(4);
                                            searchFlag(lat, lon, tit, con,0);
                                            break;
                                    }

                                }
                            }).show();
                }/*else{
                    new AlertDialog.Builder(getActivity())
                            .setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                                String tit = Cur.getString(1);
                                String con = Cur.getString(2);
                                double lat = Cur.getDouble(3);
                                double lon = Cur.getDouble(4);
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            ContentValues values = new ContentValues();
                                            values.put("tit", tit);
                                            values.put("txt", con);
                                            values.put("lat", lat);
                                            values.put("lon", lon);
                                            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
                                            Cu.moveToPosition(0);
                                            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                                            ((MainActivity)getActivity()).editUploadFragment(1);
                                            break;
                                        case 1:
                                            searchFlag(lat, lon, tit, con,1);
                                            break;
                                    }
                                }
                            }).show();
                }*///long click selet 1 option
                return false;
            }

        });

        listInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                final int pos = position;
                final Cursor Cur = coord.rawQuery("SELECT * FROM table01",null);
                Cur.moveToPosition(pos);
                if(select == 1) {
                    new AlertDialog.Builder(getActivity())
                            .setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String tit = Cur.getString(1);
                                    String con = Cur.getString(2);
                                    double lat = Cur.getDouble(3);
                                    double lon = Cur.getDouble(4);
                                    switch (which) {
                                        case 0:
                                            ContentValues values = new ContentValues();
                                            values.put("tit", tit);
                                            values.put("txt", con);
                                            values.put("lat", lat);
                                            values.put("lon", lon);
                                            Cursor Cu = coord.rawQuery("SELECT * FROM tableEditUpload", null);
                                            Cu.moveToPosition(0);
                                            coord.update("tableEditUpload", values, "_id=" + Cu.getInt(0), null);
                                            ((MainActivity) getActivity()).editUploadFragment(1);
                                            break;
                                        case 1:
                                            searchFlag(lat, lon, tit, con, 1);
                                            break;
                                    }
                                }
                            }).show();
                }
            }
        });
    }




    private void searchFlag(double la,double lo,String ti,String co,int i){
        ((MainActivity)getActivity()).searchFlagFragment(la,lo,ti,co,i,0);
    }

    public void setFlagSQL(SQLiteDatabase coord){
        this.coord = coord;
    }

    public void setSelect(int pass){
        this.select = pass;
    }

}
