package com.rc.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MenuActivity class holds all aspects of the main menu.
 */
public class MenuActivity extends AppCompatActivity {

    private static final String FILE_NAME = "stats.txt";
    private ArrayList<String> stats = new ArrayList<>();
    private LinearLayout menuScreen, statScreen, categorySelectScreen, quitScreen;
    private boolean statPageActive, categoryPageActive;
    private TextView roundsWonStat, roundsLostStat, roundsQuitStat, winStreakStat, gameOverStat,
            roundsPlayedStat, skippedStat, highestRoundStat, levelStat;
    private Intent intent;
    private SoundPool soundPool;
    private int uiSound;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //AdMob init
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Init adView
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Find by ID
        Button backButton = findViewById(R.id.backButton);
        Button categoryBackButton = findViewById(R.id.categoryBackButton);
        TextView yesQuitButton = findViewById(R.id.quitYesText);
        TextView noQuitButton = findViewById(R.id.quitNoText);
        TextView playButton = findViewById(R.id.playButton);
        TextView statButton = findViewById(R.id.statButton);
        TextView moviesCategoryButton = findViewById(R.id.movieCategory);
        TextView musicCategoryButton = findViewById(R.id.musicCategory);
        TextView sportCategoryButton = findViewById(R.id.sportCategory);
        TextView scienceCategoryButton = findViewById(R.id.scienceCategory);
        TextView dictionaryCategoryButton = findViewById(R.id.dictionaryCategory);
        TextView randomCategoryButton = findViewById(R.id.randomCategory);
        TextView videoGamesCategoryButton = findViewById(R.id.videoGamesCategory);
        TextView animalsCategoryButton = findViewById(R.id.animalCategory);
        TextView festiveCategory = findViewById(R.id.specialCategory);
        TextView householdCategory = findViewById(R.id.houseHoldCategory);
        menuScreen = findViewById(R.id.menuScreen);
        statScreen = findViewById(R.id.statScreen);
        quitScreen = findViewById(R.id.quitScreen);
        roundsPlayedStat = findViewById(R.id.roundsPlayedStat);
        roundsWonStat = findViewById(R.id.roundsWonStat);
        roundsLostStat = findViewById(R.id.roundsLostStat);
        winStreakStat = findViewById(R.id.winstreakStat);
        roundsQuitStat = findViewById(R.id.roundsQuitStat);
        highestRoundStat = findViewById(R.id.highestRoundStat);
        gameOverStat = findViewById(R.id.gameOverStat);
        skippedStat = findViewById(R.id.skippedStat);
        levelStat = findViewById(R.id.levelStat);
        categorySelectScreen = findViewById(R.id.categorySelectScreen);

        // Sets visibility of Layouts
        menuScreen.setVisibility(View.VISIBLE);
        statScreen.setVisibility(View.INVISIBLE);
        categorySelectScreen.setVisibility(View.INVISIBLE);
        quitScreen.setVisibility(View.INVISIBLE);
        intent = new Intent(getApplicationContext(), MainActivity.class);

        loadData();
        updatePlayerLevelStat();

        //SoundPool preparation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
        uiSound = soundPool.load(this, R.raw.my_clicker, 1);

        // Button (or clickable textView) functionality
        festiveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "festive");
                categoryClick();
            }
        });
        householdCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "household");
                categoryClick();
            }
        });
        animalsCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "animal");
                categoryClick();
            }
        });
        moviesCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "movie");
                categoryClick();
            }
        });
        musicCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "music");
                categoryClick();
            }
        });
        sportCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "sport");
                categoryClick();
            }
        });
        scienceCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "science");
                categoryClick();
            }
        });
        videoGamesCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "video");
                categoryClick();
            }
        });
        randomCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "random");
                categoryClick();
            }
        });
        dictionaryCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("category", "dictionary");
                categoryClick();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                categoryPageActive = true;
                categorySelectScreen.setVisibility(View.VISIBLE);
                menuScreen.setVisibility(View.INVISIBLE);
            }
        });
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                statPageActive = true;
                roundsPlayedStat.setText(stats.get(0));
                roundsWonStat.setText(stats.get(1));
                roundsLostStat.setText(stats.get(2));
                roundsQuitStat.setText(stats.get(3));
                winStreakStat.setText(stats.get(4));
                skippedStat.setText(stats.get(5));
                highestRoundStat.setText(stats.get(6));
                gameOverStat.setText(stats.get(7));
                menuScreen.setVisibility(View.INVISIBLE);
                statScreen.setVisibility(View.VISIBLE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                statPageActive = false;
                menuScreen.setVisibility(View.VISIBLE);
                statScreen.setVisibility(View.INVISIBLE);
            }
        });
        categoryBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                categoryPageActive = false;
                menuScreen.setVisibility(View.VISIBLE);
                categorySelectScreen.setVisibility(View.INVISIBLE);
            }
        });
        yesQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                soundPool.release();
                finish();
            }
        });
        noQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playUIAudio();
                quitScreen.setVisibility(View.INVISIBLE);
                menuScreen.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    /**
     * Sets intent, plays UI sound, releases soundpool, and closes this activity.
     */
    public void categoryClick() {
        playUIAudio();
        startActivity(intent);
        soundPool.release();
        finish();
    }

    /**
     * Calculates the player level using loaded player stat data.
     */
    public void updatePlayerLevelStat() {

        int positiveMultiplier = 3;
        int roundsWonScore = Integer.parseInt(stats.get(1));
        int winstreakScore = Integer.parseInt(stats.get(4));
        int highestRoundScore = Integer.parseInt(stats.get(6));
        int roundsLostScore = Integer.parseInt(stats.get(2));
        int roundsQuitScore = Integer.parseInt(stats.get(3));
        int skipsScore = Integer.parseInt(stats.get(5));
        int gameOverScore = Integer.parseInt(stats.get(7));

        int score = roundsWonScore + (highestRoundScore + winstreakScore * positiveMultiplier)
                - roundsLostScore - roundsQuitScore - skipsScore - gameOverScore;

        String skillLevel;
        if (score < 0 && score > -50) {
            skillLevel = "Noob";
        } else if (score <= -50) {
            skillLevel = "Uber Noober";
        } else if (score > 0 && score <= 50) {
            skillLevel = "Novice";
        } else if (score > 50 && score <= 100) {
            skillLevel = "Competent";
        } else if (score > 100 && score <= 500) {
            skillLevel = "Adept";
        } else if (score > 500 && score <= 1000) {
            skillLevel = "Professional";
        } else if (score > 1000 && score <= 5000) {
            skillLevel = "Master";
        } else if (score > 5000 && score <= 10000) {
            skillLevel = "Legendary";
        } else if (score > 10000) {
            skillLevel = "God";
        } else {
            skillLevel = "Beginner";
        }
        levelStat.setText(skillLevel);
    }

    /**
     * Plays the UI audio.
     */
    public void playUIAudio() {
        soundPool.autoPause();
        soundPool.play(uiSound, 1, 1, 0, 0, 1);
    }

    /**
     * Sets the back button to return to the main menu, or quit if already in the main menu.
     */
    @Override
    public void onBackPressed() {
        if (statPageActive) {
            playUIAudio();
            statPageActive = false;
            menuScreen.setVisibility(View.VISIBLE);
            statScreen.setVisibility(View.INVISIBLE);
        } else if (categoryPageActive) {
            playUIAudio();
            categoryPageActive = false;
            menuScreen.setVisibility(View.VISIBLE);
            categorySelectScreen.setVisibility(View.INVISIBLE);
        } else {
            playUIAudio();
            menuScreen.setVisibility(View.INVISIBLE);
            quitScreen.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks to see if there is a saved stat data file, if there isn't it adds default stats of
     * 0 to the stat Arraylist. This will need to be changed at some point to allow me to add
     * stats in updates - currently any added stats will cause the game to crash on loading stats.
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
}