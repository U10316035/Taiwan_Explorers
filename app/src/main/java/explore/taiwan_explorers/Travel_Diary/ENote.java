package explore.taiwan_explorers.Travel_Diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import explore.taiwan_explorers.R;

//Activity for a old note being opened

public class ENote extends AppCompatActivity {
    Button back;
    int whichAct = 0;
    int whichAct2 = 0;
    EditText editText;
    String text;

    public ENote(){

    }

    public ENote(int i){
       whichAct = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_note);
        Intent intent = this.getIntent();
        Bundle bundle =this.getIntent().getExtras();
        if(bundle != null) {
            whichAct = bundle.getInt("whichAct");
            whichAct2 = bundle.getInt("whichAct2");
            text = bundle.getString("diaryText");
        }
        back = (Button)findViewById(R.id.buttonDiaryBack);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        editText = (EditText) findViewById(R.id.text_editor2);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus) {//onfocus
                } else {// else
                    editText.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        });
        if(whichAct == 0) {
            //Set the editText with text from notesArray that is text from a old note
            if (MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber).equals("(No title)"))
                editText.setText("");
            else
                editText.setText(MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber));
            editText.setSelection(editText.getText().length());
        }else {
            if(whichAct2==0) {
                if (MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber).equals("(No title)"))
                    editText.setText("");
                else
                    editText.setText(MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber));
            }else{
                editText.setText(text);
                whichAct2 = 0;
            }

            editText.setSelection(editText.getText().length());
            editText.setEnabled(false);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    back.setVisibility(View.INVISIBLE);
                    onBackPressed();
                    whichAct = 0;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(whichAct==0)
            getMenuInflater().inflate(R.menu.menu_e_note, menu2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_discardNote2) {

            MainDiaryFragment.notesArray.remove(MainDiaryFragment.itemNumber);
            MainDiaryFragment.displayArray.remove(MainDiaryFragment.itemNumber);

            MainDiaryFragment.theListView.setAdapter(MainDiaryFragment.theAdapter);

            try {
                FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(MainDiaryFragment.notesArray);
                objectOutputStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finish();
        }
        if (id == R.id.action_saveNote2) {


            //Get text from user input in the EditText and add it to the listview using adapter
            EditText editText = (EditText)findViewById(R.id.text_editor2);
            if(editText.getText().toString().equals(""))
                editText.setText("(No title)");
            String theText = (String)editText.getText().toString();
            //set updated note to notes Array
            MainDiaryFragment.notesArray.set(MainDiaryFragment.itemNumber, theText);

            //set updated note up to newline to display array
            String[] beforeNewline = theText.split("\n", 20);
            MainDiaryFragment.displayArray.set(MainDiaryFragment.itemNumber,beforeNewline[0]);

            //update the listview on main activity
            MainDiaryFragment.theListView.setAdapter(MainDiaryFragment.theAdapter);

            //write notesArray to memory
            try {
                FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(MainDiaryFragment.notesArray);
                objectOutputStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(whichAct==0) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("離開")
                    .setMessage("未儲存離開?")
                    .setPositiveButton("是,離開", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNeutralButton("幫我儲存後離開!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText = (EditText) findViewById(R.id.text_editor2);
                    if (editText.getText().toString().equals(""))
                        editText.setText("(No title)");
                    String theText = (String) editText.getText().toString();
                    //set updated note to notes Array
                    MainDiaryFragment.notesArray.set(MainDiaryFragment.itemNumber, theText);

                    //set updated note up to newline to display array
                    String[] beforeNewline = theText.split("\n", 20);
                    MainDiaryFragment.displayArray.set(MainDiaryFragment.itemNumber, beforeNewline[0]);

                    //update the listview on main activity
                    MainDiaryFragment.theListView.setAdapter(MainDiaryFragment.theAdapter);

                    //write notesArray to memory
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                        objectOutputStream.writeObject(MainDiaryFragment.notesArray);
                        objectOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            }).setNegativeButton("否,繼續編輯", null).show();
        }else
            super.onBackPressed();
    }

    public void setWhichAct(){
        whichAct = 1;
    }

}
