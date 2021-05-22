package com.rc.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * The MainActivity class holds stat information, has methods for saving and loading stat data,
 * uses the Hangman class to determine if the round is won or lost and has methods for to
 * process the outcome of those. This class also handles the use of ads.
 */
public class MainActivity extends AppCompatActivity {

    private Hangman hangman;
    private static final String FILE_NAME = "stats.txt";
    private LinearLayout playScreen, roundLostScreen, roundWonScreen, giveUpScreen, skipScreen,
            gameOverScreen, categoryCompleteScreen;
    private TextView hangWordText, categoryText, roundWonWordReveal, celebrationText,
            ripText, roundLostWordReveal, skipButton, roundNumberText;
    private String celebrationPhraseString, roundDisplay, passedCategory;
    private ImageView playImage, roundWonImage, playerLivesImage;
    private ArrayList<String> stats = new ArrayList<>();
    private int winStreak, numberOfRoundsWon, numberOfRoundsLost, highStreak, numberOfRoundsQuit,
            numberOfRoundsPlayed, numberOfSkips, highestRoundReached, currentRound, playerLives, gameOverStat;
    private boolean roundPlaying, activityRunning, skippingScreenActive, canSkip;
    private TextView[] letterButton;
    private char[] letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        activityRunning = true;
        skippingScreenActive = false;

        //Grabs passed category data from menu activity.
        passedCategory = getIntent().getStringExtra("category");
        hangman = new Hangman(this, passedCategory);

        // LinearLayouts
        playScreen = findViewById(R.id.playScreen);
        roundLostScreen = findViewById(R.id.roundLostScreen);
        roundWonScreen = findViewById(R.id.roundWonScreen);
        giveUpScreen = findViewById(R.id.giveUpScreen);
        skipScreen = findViewById(R.id.skipScreen);
        gameOverScreen = findViewById(R.id.gameOverScreen);
        categoryCompleteScreen = findViewById(R.id.categoryCompleteScreen);

        // Images
        ImageView endGameImage = findViewById(R.id.endGameImage);
        playImage = findViewById(R.id.playImage);
        roundWonImage = findViewById(R.id.roundWonImage);

        // Displayed text
        TextView endGameText = findViewById(R.id.gameOverText);
        hangWordText = findViewById(R.id.hangwordText);
        categoryText = findViewById(R.id.categoryText);
        roundLostWordReveal = findViewById(R.id.roundLostWordReveal);
        roundWonWordReveal = findViewById(R.id.roundWonWordReveal);
        celebrationText = findViewById(R.id.celebrationText);
        ripText = findViewById(R.id.ripText);
        roundNumberText = findViewById(R.id.roundNumberText);

        // Buttons or clickable textViews
        TextView giveUpYesButton = findViewById(R.id.giveUpYesButton);
        TextView giveUpNoButton = findViewById(R.id.giveUpNoButton);
        TextView roundWonYesButton = findViewById(R.id.roundWonYesButton);
        TextView roundWonNoButton = findViewById(R.id.roundWonNoButton);
        TextView gameOverYesButton = findViewById(R.id.roundLostYesButton);
        TextView gameOverNoButton = findViewById(R.id.roundLostNoButton);
        TextView yesSkipButton = findViewById(R.id.yesSkipText);
        TextView noSkipButton = findViewById(R.id.noSkipText);
        TextView quitButton = findViewById(R.id.quitButton);
        TextView congratsText = findViewById(R.id.congratsText);
        TextView congratsInfoText = findViewById(R.id.congratsInfoText);
        skipButton = findViewById(R.id.skipButton);
        playerLivesImage = findViewById(R.id.livesLeftImage);
        letterButton = new TextView[26];
        letterButton[0] = findViewById(R.id.letterButtonA);
        letterButton[1] = findViewById(R.id.letterButtonB);
        letterButton[2] = findViewById(R.id.letterButtonC);
        letterButton[3] = findViewById(R.id.letterButtonD);
        letterButton[4] = findViewById(R.id.letterButtonE);
        letterButton[5] = findViewById(R.id.letterButtonF);
        letterButton[6] = findViewById(R.id.letterButtonG);
        letterButton[7] = findViewById(R.id.letterButtonH);
        letterButton[8] = findViewById(R.id.letterButtonI);
        letterButton[9] = findViewById(R.id.letterButtonJ);
        letterButton[10] = findViewById(R.id.letterButtonK);
        letterButton[11] = findViewById(R.id.letterButtonL);
        letterButton[12] = findViewById(R.id.letterButtonM);
        letterButton[13] = findViewById(R.id.letterButtonN);
        letterButton[14] = findViewById(R.id.letterButtonO);
        letterButton[15] = findViewById(R.id.letterButtonP);
        letterButton[16] = findViewById(R.id.letterButtonQ);
        letterButton[17] = findViewById(R.id.letterButtonR);
        letterButton[18] = findViewById(R.id.letterButtonS);
        letterButton[19] = findViewById(R.id.letterButtonT);
        letterButton[20] = findViewById(R.id.letterButtonU);
        letterButton[21] = findViewById(R.id.letterButtonV);
        letterButton[22] = findViewById(R.id.letterButtonW);
        letterButton[23] = findViewById(R.id.letterButtonX);
        letterButton[24] = findViewById(R.id.letterButtonY);
        letterButton[25] = findViewById(R.id.letterButtonZ);

        // Load or init variables
        letter = new char[26];
        letter[0] = 'A';
        letter[1] = 'B';
        letter[2] = 'C';
        letter[3] = 'D';
        letter[4] = 'E';
        letter[5] = 'F';
        letter[6] = 'G';
        letter[7] = 'H';
        letter[8] = 'I';
        letter[9] = 'J';
        letter[10] = 'K';
        letter[11] = 'L';
        letter[12] = 'M';
        letter[13] = 'N';
        letter[14] = 'O';
        letter[15] = 'P';
        letter[16] = 'Q';
        letter[17] = 'R';
        letter[18] = 'S';
        letter[19] = 'T';
        letter[20] = 'U';
        letter[21] = 'V';
        letter[22] = 'W';
        letter[23] = 'X';
        letter[24] = 'Y';
        letter[25] = 'Z';
        playerLives = 5;
        roundPlaying = true;
        currentRound = 1;
        canSkip = true;
        roundDisplay = "Round " + currentRound;
        loadData();
        numberOfRoundsPlayed = Integer.parseInt(stats.get(0));
        numberOfRoundsPlayed++;
        numberOfRoundsWon = Integer.parseInt(stats.get(1));
        numberOfRoundsLost = Integer.parseInt(stats.get(2));
        numberOfRoundsQuit = Integer.parseInt(stats.get(3));
        highStreak = Integer.parseInt(stats.get(4));
        numberOfSkips = Integer.parseInt((stats.get(5)));
        highestRoundReached = Integer.parseInt(stats.get(6));
        gameOverStat = Integer.parseInt(stats.get(7));
        winStreak = 0;

        // Set layout visibility
        playScreen.setVisibility(View.VISIBLE);
        roundLostScreen.setVisibility(View.INVISIBLE);
        roundWonScreen.setVisibility(View.INVISIBLE);
        giveUpScreen.setVisibility(View.INVISIBLE);
        skipScreen.setVisibility(View.INVISIBLE);
        gameOverScreen.setVisibility(View.INVISIBLE);
        categoryCompleteScreen.setVisibility(View.INVISIBLE);

        // Set displayed text and images
        playerLivesImage.setImageResource(R.drawable.five_lives);
        playImage.setImageResource(R.drawable.nine);
        hangWordText.setText(hangman.displayedWord());
        categoryText.setText(hangman.getCategory());
        roundNumberText.setText(roundDisplay);

        // Button functions
        for(int i = 0; i < 26; i++){
            final int finalI = i;
            letterButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hangman.guessLetter(letter[finalI]);
                    letterButton[finalI].setEnabled(false);
                    letterButton[finalI].setTextColor(Color.DKGRAY);
                    update();
                }
            });
        }
        congratsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                categoryCompleteScreen.setVisibility(View.INVISIBLE);
                roundWonScreen.setVisibility(View.VISIBLE);
            }
        });
        congratsInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                categoryCompleteScreen.setVisibility(View.INVISIBLE);
                roundWonScreen.setVisibility(View.VISIBLE);
            }
        });
        gameOverNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(startIntent);
                activityRunning = false;
                hangman.getSoundPool().release();
                finish();
            }
        });
        gameOverYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                playScreen.setVisibility(View.VISIBLE);
                roundLostScreen.setVisibility(View.INVISIBLE);
                updateLivesTallyImage();
                numberOfRoundsPlayed++;
                hangman.newGame();
                roundPlaying = true;
                categoryText.setText(hangman.getCategory());
                letterButtonReset();
                playImage.setImageResource(R.drawable.nine);
                hangWordText.setText(hangman.displayedWord());
                if (!canSkip) {
                    canSkip = true;
                    skipButton.setEnabled(true);
                    skipButton.setTextColor(Color.WHITE);
                }
            }
        });
        roundWonNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(startIntent);
                activityRunning = false;
                hangman.getSoundPool().release();
                finish();
            }
        });
        roundWonYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                playScreen.setVisibility(View.VISIBLE);
                roundLostScreen.setVisibility(View.INVISIBLE);
                roundWonScreen.setVisibility(View.INVISIBLE);
                hangman.newGame();
                currentRound++;
                numberOfRoundsPlayed++;
                roundPlaying = true;
                categoryText.setText(hangman.getCategory());
                roundDisplay = "Round " + currentRound;
                roundNumberText.setText(roundDisplay);
                letterButtonReset();
                playImage.setImageResource(R.drawable.nine);
                hangWordText.setText(hangman.displayedWord());
                if (!canSkip) {
                    canSkip = true;
                    skipButton.setEnabled(true);
                    skipButton.setTextColor(Color.WHITE);
                }
                if (currentRound > highestRoundReached) {
                    highestRoundReached = currentRound;
                }
                saveData();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                skippingScreenActive = true;
                playScreen.setVisibility(View.INVISIBLE);
                skipScreen.setVisibility(View.VISIBLE);
            }
        });
        yesSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                playScreen.setVisibility(View.VISIBLE);
                skipScreen.setVisibility(View.INVISIBLE);
                canSkip = false;
                skipButton.setEnabled(false);
                skipButton.setTextColor(Color.DKGRAY);
                numberOfSkips++;
                numberOfRoundsPlayed++;
                hangman.newGame();
                roundPlaying = true;
                categoryText.setText(hangman.getCategory());
                letterButtonReset();
                playImage.setImageResource(R.drawable.nine);
                hangWordText.setText(hangman.displayedWord());
                skippingScreenActive = false;
            }
        });
        noSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                playScreen.setVisibility(View.VISIBLE);
                skipScreen.setVisibility(View.INVISIBLE);
                skippingScreenActive = false;
            }
        });
        giveUpNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                giveUpScreen.setVisibility(View.INVISIBLE);
                playScreen.setVisibility(View.VISIBLE);
            }
        });
        giveUpYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                roundPlaying = false;
                numberOfRoundsQuit++;
                saveData();
                Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(startIntent);
                activityRunning = false;
                hangman.getSoundPool().release();
                finish();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roundPlaying && !skippingScreenActive) {
                    hangman.playUIAudio();
                    giveUpScreen.setVisibility(View.VISIBLE);
                    playScreen.setVisibility(View.INVISIBLE);
                }
            }
        });
        endGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(startIntent);
                activityRunning = false;
                hangman.getSoundPool().release();
                finish();
            }
        });
        endGameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangman.playUIAudio();
                Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(startIntent);
                activityRunning = false;
                hangman.getSoundPool().release();
                finish();
            }
        });
    }

    /**
     * Updates the image of how many lives the player has left.
     */
    public void updateLivesTallyImage() {

        if (playerLives == 5) {
            playerLivesImage.setImageResource(R.drawable.five_lives);
        } else if (playerLives == 4) {
            playerLivesImage.setImageResource(R.drawable.four_lives);
        } else if (playerLives == 3) {
            playerLivesImage.setImageResource(R.drawable.three_lives);
        } else if (playerLives == 2) {
            playerLivesImage.setImageResource(R.drawable.two_lives);
        } else if (playerLives == 1) {
            playerLivesImage.setImageResource(R.drawable.one_lives);
        }
    }

    /**
     * Bypasses the default back button press and gives player a prompt to confirm if wanting to
     * exit round or not.
     */
    @Override
    public void onBackPressed() {

        if (roundPlaying && !skippingScreenActive) {
            hangman.playUIAudio();
            giveUpScreen.setVisibility(View.VISIBLE);
        } else if (roundPlaying) {
            hangman.playUIAudio();
            skippingScreenActive = false;
            skipScreen.setVisibility(View.INVISIBLE);
            playScreen.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates the game according to the amount of guesses a player has, or if the player has
     * completed the word.
     */
    public void update() {
        if (hangman.getGuessesLeft() == 9) {
            playImage.setImageResource(R.drawable.nine);
        }
        if (hangman.getGuessesLeft() == 8) {
            playImage.setImageResource(R.drawable.eight);
        }
        if (hangman.getGuessesLeft() == 7) {
            playImage.setImageResource(R.drawable.seven);
        }
        if (hangman.getGuessesLeft() == 6) {
            playImage.setImageResource(R.drawable.six);
        }
        if (hangman.getGuessesLeft() == 5) {
            playImage.setImageResource(R.drawable.five);
        }
        if (hangman.getGuessesLeft() == 4) {
            playImage.setImageResource(R.drawable.four);
        }
        if (hangman.getGuessesLeft() == 3) {
            playImage.setImageResource(R.drawable.three);
        }
        if (hangman.getGuessesLeft() == 2) {
            playImage.setImageResource(R.drawable.two);
        }
        if (hangman.getGuessesLeft() == 1) {
            playImage.setImageResource(R.drawable.one);
        }
        if (hangman.getGuessesLeft() == 0) {
            playImage.setImageResource(R.drawable.zero);
            numberOfRoundsLost++;
            winStreak = 0;
            playerLives--;

            if (playerLives == 0) {
                gameOverStat++;
                saveData();
                gameOver();
            } else {
                saveData();
                roundLost();
            }
        }
        hangWordText.setText(hangman.displayedWord());
        if (!hangman.getOutputWord().contains(Character.toString('_'))) {
            numberOfRoundsWon++;
            winStreak++;
            if (winStreak > highStreak) {
                highStreak = winStreak;
            }
            saveData();
            roundWon();
        }
    }

    /**
     * Ensures the stat data is saved when game is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    /**
     * Converts in game data into a String that is then saved into a text file.
     */
    public void saveData() {

        String savedData = numberOfRoundsPlayed + "\n" +  //0
                numberOfRoundsWon + "\n" +     //1
                numberOfRoundsLost + "\n" +    //2
                numberOfRoundsQuit + "\n" +    //3
                highStreak + "\n" +            //4
                numberOfSkips + "\n" +         //5
                highestRoundReached + "\n" +   //6
                gameOverStat + "\n";          //7

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(savedData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * If all of the words in category have been found, a congratulations screen is displayed.
     */
    public void categoryComplete() {
        categoryCompleteScreen.setVisibility(View.VISIBLE);
    }

    /**
     * Loads the text file containing game stats. It then puts this info into a stat Arraylist.
     */
    public void loadData() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (bufferedReader.ready()) {
                stats.add(bufferedReader.readLine());
            }
        } catch (FileNotFoundException e) {
            stats.add("0");
            stats.add("0");
            stats.add("0");
            stats.add("0");
            stats.add("0");
            stats.add("0");
            stats.add("0");
            stats.add("0");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes ad if active and ensures stat data is saved.
     */
    @Override
    protected void onDestroy() {
        saveData();
        activityRunning = false;
        super.onDestroy();
    }

    /**
     * Ensures all letter buttons have been reset for the new round.
     */
    public void letterButtonReset() {
        for(int i = 0; i < 26; i++){
            letterButton[i].setEnabled(true);
            letterButton[i].setTextColor(Color.WHITE);
        }
    }

    /**
     * Displays a celebration phrase on successful completion of the round. If the player
     * decides to continue, the next round loads. If not, an ad is played and the player is
     * returned to the main menu. Checks for if the list_festive category is played, if yes then
     * a different image is displayed.
     */
    public void roundWon() {

        if (hangman.getDictionary().size() == 0) {
            categoryComplete();
        }

        roundPlaying = false;
        playScreen.setVisibility(View.INVISIBLE);
        roundLostScreen.setVisibility(View.INVISIBLE);
        roundWonScreen.setVisibility(View.VISIBLE);
        Random random = new Random();
        int randomInt = random.nextInt(5);
        if (randomInt == 0) {
            celebrationPhraseString = "Well done!";
        } else if (randomInt == 1) {
            celebrationPhraseString = "Awesome!";
        } else if (randomInt == 2) {
            celebrationPhraseString = "Good job!";
        } else if (randomInt == 3) {
            celebrationPhraseString = "Great work!";
        } else if (randomInt == 4) {
            celebrationPhraseString = "You survived!";
        }
        celebrationText.setText(celebrationPhraseString);
        if (passedCategory.equals("festive")) {
            roundWonImage.setImageResource(R.drawable.winning_xmas);
        } else {
            roundWonImage.setImageResource(R.drawable.winning);
        }
        roundWonWordReveal.setText(hangman.getAnswer());
    }

    /**
     * Handles the game over screen. Displays how many lives the player has left and reveals my_correct
     * answer. Allows player the option to play another round or quit, quiting results in an ad being
     * played.
     */
    public void roundLost() {
        roundPlaying = false;
        playScreen.setVisibility(View.INVISIBLE);
        roundLostScreen.setVisibility(View.VISIBLE);
        String roundLoseText;
        if (playerLives == 1) {
            roundLoseText = playerLives + " life left!";
        } else {
            roundLoseText = playerLives + " lives left!";
        }
        ripText.setText(roundLoseText);
        roundLostWordReveal.setText(hangman.getAnswer());
    }

    /**
     * Sets the gameOver screen visible and sets roundPlaying to false.
     */
    public void gameOver() {
        roundPlaying = false;
        hangman.playGameOverSound();
        playScreen.setVisibility(View.INVISIBLE);
        gameOverScreen.setVisibility(View.VISIBLE);
    }
}