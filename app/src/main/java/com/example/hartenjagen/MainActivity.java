package com.example.hartenjagen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String NAMES = "com.example.example.NAME";
    private EditText[] playerNames = new EditText[5];
    private Button playButton;
    private Button clearNamesButton;
    private int PLAYERLENGTH = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerNames[0] = findViewById(R.id.editTextPlayer1);
        playerNames[1] = findViewById(R.id.editTextPlayer2);
        playerNames[2] = findViewById(R.id.editTextPlayer3);
        playerNames[3] = findViewById(R.id.editTextPlayer4);
        playerNames[4] = findViewById(R.id.editTextPlayer5);

        playerNames[0].setFilters(new InputFilter[] {new InputFilter.LengthFilter(PLAYERLENGTH)});
        playerNames[1].setFilters(new InputFilter[] {new InputFilter.LengthFilter(PLAYERLENGTH)});
        playerNames[2].setFilters(new InputFilter[] {new InputFilter.LengthFilter(PLAYERLENGTH)});
        playerNames[3].setFilters(new InputFilter[] {new InputFilter.LengthFilter(PLAYERLENGTH)});
        playerNames[4].setFilters(new InputFilter[] {new InputFilter.LengthFilter(PLAYERLENGTH)});

        Intent a = getIntent();
        String[] names = a.getStringArrayExtra(ThirdActivity.NAMES);
        names = a.getStringArrayExtra(SecondActivity.NAMES);
        if (names != null) {
            for (int i = 0; i < names.length; i++) {
                playerNames[i].setText(names[i]);
            }
        }

        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                ArrayList<String> names = new ArrayList<>();
                for (int i = 0; i < playerNames.length; i++) {
                    if (!playerNames[i].getText().toString().equals("")) {
                        names.add(playerNames[i].getText().toString());
                    }
                }
                intent.putExtra(NAMES, names);
                startActivity(intent);
            }
        });

        clearNamesButton = findViewById(R.id.clearNamesButton);
        clearNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < playerNames.length; i++) {
                    playerNames[i].setText("");
                }
            }
        });

    }

}