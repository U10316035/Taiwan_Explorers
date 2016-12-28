package explore.taiwan_explorers.Travel_Diary;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import explore.taiwan_explorers.R;

//Activity for a old note being opened

public class ENote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set the editText with text from notesArray that is text from a old note
        EditText editText = (EditText)findViewById(R.id.text_editor2);

        if(MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber) == "(empty)")
            editText.setText("");
        else
            editText.setText(MainDiaryFragment.notesArray.get(MainDiaryFragment.itemNumber));
        editText.setSelection(editText.getText().length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            if(editText.getText().toString() == "")
                editText.setText("empty");
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
                         EditText editText = (EditText)findViewById(R.id.text_editor2);
                         if(editText.getText().toString() == "")
                             editText.setText("empty");
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
                }).setNegativeButton("否,繼續編輯", null).show();
    }
}
