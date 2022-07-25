package com.example.hartenjagen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ThirdActivity extends AppCompatActivity {

    public static final String NAMES = "com.example.example.NAME";
    private TextView[] playerNames = new TextView[5];
    private EditText[] tempScores = new EditText[5];
    private Integer[]  tempScoresInt = new Integer[5];
    private TextView[] totalScores = new TextView[5];
    private Integer[] totalScoresInt = new Integer[5];
    private Button[] plusButtons = new Button[5];
    private Button[] minusButtons = new Button[5];

    private Button backButton;
    private Button calculateButton;
    private Button undoButton;
    private TextView warningText;
    private TextView below0Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        final String[] names = intent.getStringArrayExtra(SecondActivity.NAMES);
        String[] scores = intent.getStringArrayExtra(SecondActivity.SCORES);

        playerNames[0] = findViewById(R.id.namePlayer1);
        playerNames[1] = findViewById(R.id.namePlayer2);
        playerNames[2] = findViewById(R.id.namePlayer3);
        playerNames[3] = findViewById(R.id.namePlayer4);
        playerNames[4] = findViewById(R.id.namePlayer5);

        tempScores[0] = findViewById(R.id.scoreToAddPlayer1);
        tempScores[1] = findViewById(R.id.scoreToAddPlayer2);
        tempScores[2] = findViewById(R.id.scoreToAddPlayer3);
        tempScores[3] = findViewById(R.id.scoreToAddPlayer4);
        tempScores[4] = findViewById(R.id.scoreToAddPlayer5);

        totalScores[0] = findViewById(R.id.totalScorePlayer1);
        totalScores[1] = findViewById(R.id.totalScorePlayer2);
        totalScores[2] = findViewById(R.id.totalScorePlayer3);
        totalScores[3] = findViewById(R.id.totalScorePlayer4);
        totalScores[4] = findViewById(R.id.totalScorePlayer5);

        plusButtons[0] = findViewById(R.id.plusButton6);
        plusButtons[1] = findViewById(R.id.plusButton7);
        plusButtons[2] = findViewById(R.id.plusButton8);
        plusButtons[3] = findViewById(R.id.plusButton9);
        plusButtons[4] = findViewById(R.id.plusButton10);

        minusButtons[0] = findViewById(R.id.minusButton6);
        minusButtons[1] = findViewById(R.id.minusButton7);
        minusButtons[2] = findViewById(R.id.minusButton8);
        minusButtons[3] = findViewById(R.id.minusButton9);
        minusButtons[4] = findViewById(R.id.minusButton10);

        for (int i = 0; i < 5; i++) {
            playerNames[i].setVisibility(View.INVISIBLE);
            tempScores[i].setVisibility(View.INVISIBLE);
            totalScores[i].setVisibility(View.INVISIBLE);
            plusButtons[i].setVisibility(View.INVISIBLE);
            minusButtons[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < names.length; i++) {
            playerNames[i].setVisibility(View.VISIBLE);
            tempScores[i].setVisibility(View.VISIBLE);
            totalScores[i].setVisibility(View.VISIBLE);
            plusButtons[i].setVisibility(View.VISIBLE);
            minusButtons[i].setVisibility(View.VISIBLE);
            playerNames[i].setText(names[i] + ":");
            tempScores[i].setText("0");
            totalScores[i].setText(scores[i]);
        }

        warningText = findViewById(R.id.warningText);
        below0Text = findViewById(R.id.below0Text);

        backButton = findViewById(R.id.button_previous);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(NAMES, names);
                startActivity(intent);
            }
        });

        undoButton = findViewById(R.id.button_undo2);
        undoButton.setVisibility(View.INVISIBLE);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Calculates previous scores based on the tempscores of last round
                    for (int i = 0; i < names.length; i++) {
                        convertIntToTextView(totalScoresInt[i] + tempScoresInt[i], totalScores[i]);
                        convertIntToEditText(tempScoresInt[i], tempScores[i]);
                    }
                    //Toggles buttons
                    undoButton.setVisibility(View.INVISIBLE);
                    below0Text.setText("");
                    calculateButton.setVisibility(View.VISIBLE);

                } catch (NumberFormatException e) {}
            }
        });

        calculateButton = findViewById(R.id.calculateScore);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //Checks if temp scores add to 20, if not give a warning, if yes continue
                    int count = 0;
                    for (int i = 0; i < names.length; i++) {
                        count = count + convertEditTextToInt(tempScores[i]);
                    }
                    if (count != 20) {
                        warningText.setText(getResources().getString(R.string.score_not_20));
                        return;
                    }
                    warningText.setText("");

                    //Converts temp scores to integers
                    for (int i = 0; i < names.length; i++) {
                        tempScoresInt[i] = convertEditTextToInt(tempScores[i]);
                    }

                    //Calculate new scores and updates them
                    for (int i = 0; i < names.length; i++) {
                        totalScoresInt[i] = convertTextViewToInt(totalScores[i]) - tempScoresInt[i];
                        convertIntToTextView(totalScoresInt[i], totalScores[i]);
                        tempScores[i].setText("0");
                    }
                    undoButton.setVisibility(View.VISIBLE);

                    String below0String = "";
                    for (int i = 0; i < names.length; i++) {
                        if (totalScoresInt[i] < 1) {
                            if (below0String.length() > 0) {
                                below0String = below0String.concat("and "+ names[i] + " ");
                            } else {
                                below0String = below0String.concat(names[i] + " ");
                            }

                        }
                    }
                    if (below0String.length() > 0) {
                        below0String = below0String.concat(getResources().getString(R.string.score_below_0));
                        below0Text.setText(below0String);
                        calculateButton.setVisibility(View.INVISIBLE);
                    }
                } catch (NumberFormatException e ) {
                }

            }
        });

        //Functionality for plus and minus buttons
        for (int i = 0; i < names.length; i++) {
            int finalI = i;
            plusButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        convertIntToEditText(convertEditTextToInt(tempScores[finalI]) + 1, tempScores[finalI]);
                    } catch (NumberFormatException e) {}
                }
            });

            minusButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        convertIntToEditText(convertEditTextToInt(tempScores[finalI]) - 1, tempScores[finalI]);
                    } catch (NumberFormatException e) {}
                }
            });
        }
    }

    //Converts textview to integer
    public Integer convertTextViewToInt(TextView s) {
        return Integer.parseInt(s.getText().toString());
    }

    //Converts edittext to integer
    public Integer convertEditTextToInt(EditText s) {
        return Integer.parseInt(s.getText().toString());
    }

    //Converts integer to textview
    public void convertIntToTextView(Integer n, TextView s) {
        s.setText(String.valueOf(n));
    }

    //Converts integer to edittext
    public void convertIntToEditText(Integer n, EditText s) {
        s.setText(String.valueOf(n));
    }
}