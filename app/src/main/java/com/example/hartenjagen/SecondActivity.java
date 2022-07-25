package com.example.hartenjagen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {

    public static final String NAMES = "com.example.example.NAME";
    public static final String SCORES = "com.example.example.SCORE";

    private TextView[] playerNames = new TextView[4];
    private EditText[] tempScores = new EditText[4];
    private Integer[]  tempScoresInt = new Integer[4];
    private TextView[] totalScores = new TextView[4];
    private Integer[] totalScoresInt = new Integer[4];
    private Button[] plusButtons = new Button[4];
    private Button[] minusButtons = new Button[4];

    private Button previousButton;
    private Button calculateButton;
    private Button undoButton;
    private TextView warningText;
    private TextView above40Text;
    private Button goBackButton;
    private Button dontGoBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        final String[] names = intent.getStringArrayExtra(MainActivity.NAMES);

        goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setVisibility(View.INVISIBLE);
        dontGoBackButton = findViewById(R.id.dontGoBackButton);
        dontGoBackButton.setVisibility(View.INVISIBLE);

        playerNames[0] = findViewById(R.id.namePlayer1);
        playerNames[1] = findViewById(R.id.namePlayer2);
        playerNames[2] = findViewById(R.id.namePlayer3);
        playerNames[3] = findViewById(R.id.namePlayer4);

        tempScores[0] = findViewById(R.id.scoreToAddPlayer1);
        tempScores[1] = findViewById(R.id.scoreToAddPlayer2);
        tempScores[2] = findViewById(R.id.scoreToAddPlayer3);
        tempScores[3] = findViewById(R.id.scoreToAddPlayer4);

        totalScores[0] = findViewById(R.id.totalScorePlayer1);
        totalScores[1] = findViewById(R.id.totalScorePlayer2);
        totalScores[2] = findViewById(R.id.totalScorePlayer3);
        totalScores[3] = findViewById(R.id.totalScorePlayer4);

        plusButtons[0] = findViewById(R.id.plusButton1);
        plusButtons[1] = findViewById(R.id.plusButton2);
        plusButtons[2] = findViewById(R.id.plusButton3);
        plusButtons[3] = findViewById(R.id.plusButton4);

        minusButtons[0] = findViewById(R.id.minusButton1);
        minusButtons[1] = findViewById(R.id.minusButton2);
        minusButtons[2] = findViewById(R.id.minusButton3);
        minusButtons[3] = findViewById(R.id.minusButton4);

        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i].setText(names[i] + ":");
            tempScores[i].setText("0");
            totalScores[i].setText("0");
        }

        warningText = findViewById(R.id.warningText);
        above40Text = findViewById(R.id.above40Text);

        previousButton = findViewById(R.id.button_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        undoButton = findViewById(R.id.button_undo);
        undoButton.setVisibility(View.INVISIBLE);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Calculates previous scores based on the tempscores of last round
                    for (int i = 0; i < tempScoresInt.length; i++) {
                        if (tempScoresInt[i] == 20) {
                            for (int j = 0; j < tempScoresInt.length; j++) {
                                if (j != i) {
                                    totalScoresInt[j] = totalScoresInt[j] - 20;
                                    convertIntToTextView(totalScoresInt[j], totalScores[j]);
                                }
                                convertIntToEditText(tempScoresInt[j], tempScores[j]);
                            }
                        } else {
                            convertIntToTextView(totalScoresInt[i] - tempScoresInt[i], totalScores[i]);
                            convertIntToEditText(tempScoresInt[i], tempScores[i]);
                        }
                    }
                    //Toggles buttons
                    undoButton.setVisibility(View.INVISIBLE);
                    above40Text.setText("");
                    calculateButton.setVisibility(View.VISIBLE);
                    goBackButton.setVisibility(View.INVISIBLE);
                    dontGoBackButton.setVisibility(View.INVISIBLE);

                } catch (NumberFormatException e) {}
            }
        });

        //Functionality for calculating score
        calculateButton = findViewById(R.id.calculateScore);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //Checks if temp scores add to 20, if not give a warning, if yes continue
                    int count = 0;
                    for (int i = 0; i < tempScoresInt.length; i++) {
                        count = count + convertEditTextToInt(tempScores[i]);
                    }
                    if (count != 20) {
                        warningText.setText(getResources().getString(R.string.score_not_20));
                        return;
                    }
                    warningText.setText("");

                    //Converts temp scores to integers
                    for (int i = 0; i < tempScores.length; i++) {
                        tempScoresInt[i] = convertEditTextToInt(tempScores[i]);
                    }

                    //Calculate new scores and updates them
                    for (int i = 0; i < totalScoresInt.length; i++) {
                        if (tempScoresInt[i] == 20) {
                            totalScoresInt[i] = convertTextViewToInt(totalScores[i]);
                            for (int j = 0; j < tempScoresInt.length; j++) {
                                if (j != i) {
                                    totalScoresInt[j] = convertTextViewToInt(totalScores[j]) + 20;
                                }
                                convertIntToTextView(totalScoresInt[j], totalScores[j]);
                                tempScores[j].setText("0");
                            }
                        } else {
                            totalScoresInt[i] = convertTextViewToInt(totalScores[i]) + tempScoresInt[i];
                            convertIntToTextView(totalScoresInt[i], totalScores[i]);
                            tempScores[i].setText("0");
                        }
                    }
                    undoButton.setVisibility(view.VISIBLE);

                    String above40String = "";
                    for (int i = 0; i < 4; i++) {
                        if (totalScoresInt[i] > 39) {
                            if (above40String.length() > 0) {
                                above40String = above40String.concat("and "+ names[i] + " ");
                            } else {
                                above40String = above40String.concat(names[i] + " ");
                            }

                        }
                    }
                    if (above40String.length() > 0) {
                        above40String = above40String.concat(getResources().getString(R.string.score_above_40));
                        above40Text.setText(above40String);
                        calculateButton.setVisibility(view.INVISIBLE);
                        goBackButton.setVisibility(view.VISIBLE);
                        goBackButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                                String[] scores = new String[4];
                                for (int i = 0; i < scores.length; i++) {
                                    scores[i] = totalScores[i].getText().toString();
                                }
                                intent.putExtra(SCORES, scores);
                                intent.putExtra(NAMES, names);
                                startActivity(intent);
                            }
                        });

                        dontGoBackButton.setVisibility(view.VISIBLE);
                        dontGoBackButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(NAMES, names);
                                startActivity(intent);
                            }
                        });


                    }
                } catch (NumberFormatException e) {}
            }
        });

        //Functionality for plus and minus buttons
        for (int i = 0; i < plusButtons.length; i++) {
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