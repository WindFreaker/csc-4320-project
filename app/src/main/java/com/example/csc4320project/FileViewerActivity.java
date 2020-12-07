package com.example.csc4320project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileViewerActivity extends AppCompatActivity {

    private Uri persistentUri;
    private EditText et, sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);

        // get the URI from whatever opened the app and write it to memory so you can use it again later
        persistentUri = getIntent().getData();

        // this is the thingy that holds the text, put the text in it
        et = findViewById(R.id.multiline_edit_text);
        et.setText(readFromFile(persistentUri));

        // find the text field used for searching
        sb = findViewById(R.id.search_text);

    }

    private CharSequence readFromFile(Uri fileUri) {

        try {

            // yeah I'm gonna be honest this works but I'll never understand why
            InputStream stream = getContentResolver().openInputStream(fileUri);
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader buffered = new BufferedReader(reader);

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = buffered.readLine()) != null) {
                builder.append(line).append('\n');
            }

            // clean up trailing newline
            builder.deleteCharAt(builder.length() - 1);

            // close everything up all nicely
            buffered.close();
            reader.close();
            stream.close();

            return builder;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    private void writeToFile(Uri fileUri, CharSequence data) {

        try {

            // same lack in understand as the input stuffs and you know what that's okay
            // need to be in mode "wt" (t stands for truncate)
            OutputStream stream = getContentResolver().openOutputStream(fileUri, "wt");
            OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
            BufferedWriter buffered = new BufferedWriter(writer);

            // write the entire CharSequence to the file
            buffered.write(String.valueOf(data));

            // close everything up all nicely
            buffered.close();
            writer.close();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void buttonSaveFile(View v) {
        writeToFile(persistentUri, (CharSequence) et.getText());
    }

    public void buttonConvertUppercase(View v) {
        CharSequence oldText = et.getText();
        String newText = String.valueOf(oldText).toUpperCase();
        et.setText(newText);
    }

    public void buttonConvertLowercase(View v) {
        CharSequence oldText = et.getText();
        String newText = String.valueOf(oldText).toLowerCase();
        et.setText(newText);
    }

    public void buttonTestSelecting(View v) {

        // get all the information needed to perform a search
        String wholeText = String.valueOf(et.getText());
        String searchText = String.valueOf(sb.getText());
        int searchPos = et.getSelectionEnd();

        // if there is nothing in the search box, do nothing when the button is pressedCot
        if (searchText.equals("")) {
            return;
        }

        // perform said search
        int startIndex = searchText(wholeText, searchText, searchPos);

        // if the search returns a valid index, highlight that spot
        // otherwise highlight the search box
        if (startIndex != -1) {
            int stopIndex = startIndex + searchText.length();
            et.setSelection(startIndex, stopIndex);
            et.requestFocus();
        } else {
            sb.requestFocus();
        }

    }

    private int searchText(String str, String substr, int cursorPos) {

        // check is substr exists anywhere
        int testIndex = str.indexOf(substr);

        if (testIndex == -1) {  // if it does not, return such
            return -1;
        } else {  // otherwise, find the next instance of substr

            int actualIndex = str.indexOf(substr, cursorPos);
            if (actualIndex == -1) {
                return testIndex;
            } else {
                return actualIndex;
            }
        }
    }

}