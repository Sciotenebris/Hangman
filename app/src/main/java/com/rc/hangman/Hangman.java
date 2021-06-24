package com.rc.hangman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Hangman class contains methods for selecting the round categories and for picking the
 * hangword. The selected category stores a list of words into a dictionary arraylist and a
 * random word is picked from this list. This class also checks off the guessed letters against
 * the Hangword to determine if they are my_correct or not and is stored in an array of guessed
 * letters to ensure the same letter is not guessed twice.
 */
public class Hangman {

    private ArrayList<String> dictionary = new ArrayList<>();
    private ArrayList<Character> guessedLetters = new ArrayList<>();

    private int guessesLeft = 9;
    private InputStream inputStream;
    private Random random;
    private Context context;
    private String answer, passedCategory, category, hangWord, outputWord, word;
    private SoundPool soundPool;
    private int soundWood, soundRope, soundBody, soundWin, soundLose, soundCorrectGuess, uiSound, gameOverSound, soundWinAlt;

    public Hangman(Context context, String passedCategory) {
        this.context = context;
        this.passedCategory = passedCategory;
        random = new Random();
        selectedCategory(passedCategory);
        hangWord = randomWord();

        //SoundPool preparation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(8)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        }

        //Load sounds
        soundWood = soundPool.load(context, R.raw.my_thud, 1);
        soundRope = soundPool.load(context, R.raw.my_rope, 1);
        soundBody = soundPool.load(context, R.raw.my_body, 1);
        soundLose = soundPool.load(context, R.raw.lose, 1);
        soundWin = soundPool.load(context, R.raw.party_time, 1);
        soundWinAlt = soundPool.load(context, R.raw.cheer, 1);
        soundCorrectGuess = soundPool.load(context, R.raw.my_correct, 1);
        uiSound = soundPool.load(context, R.raw.my_clicker, 1);
        gameOverSound = soundPool.load(context,R.raw.my_game_over,1);
    }

    /**
     * Plays the game over sound.
      */
    public void playGameOverSound(){
        soundPool.play(gameOverSound, 1, 1, 0, 0, 1);
    }


    /**
     * Plays the UI sound effect when called.
     */
    public void playUIAudio() {
        soundPool.autoPause();
        soundPool.play(uiSound, 1, 1, 0, 0, 1);
    }

    /**
     * Selects a text file to read based on the passedCategory String.
     */
    public void selectedCategory(String passedCategory) {
        dictionary.clear();

        if (passedCategory.equals("random")) {
            randomCategory();
        } else {

            switch (passedCategory) {
                case "movie":
                    inputStream = context.getResources().openRawResource(R.raw.list_movies);
                    category = "Movies";
                    break;
                case "music":
                    inputStream = context.getResources().openRawResource(R.raw.list_music);
                    category = "Music";
                    break;
                case "sport":
                    inputStream = context.getResources().openRawResource(R.raw.list_sport);
                    category = "Sport";
                    break;
                case "science":
                    inputStream = context.getResources().openRawResource(R.raw.list_science);
                    category = "Science";
                    break;
                case "video":
                    inputStream = context.getResources().openRawResource(R.raw.list_videogames);
                    category = "Gaming";
                    break;
                case "dictionary":
                    inputStream = context.getResources().openRawResource(R.raw.list_dictionary);
                    category = "Dictionary";
                    break;
                case "animal":
                    inputStream = context.getResources().openRawResource(R.raw.list_animals);
                    category = "Animals";
                    break;
                case "festive":
                    inputStream = context.getResources().openRawResource(R.raw.list_festive);
                    category = "Festive";
                    break;
                case "household":
                    inputStream = context.getResources().openRawResource(R.raw.list_household);
                    category = "Household";
                    break;
                case "place":
                    inputStream = context.getResources().openRawResource(R.raw.list_places);
                    category = "Places";
                    break;
                case "book":
                    inputStream = context.getResources().openRawResource(R.raw.list_books);
                    category = "Books";
                    break;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                while (br.ready()) {
                    dictionary.add(br.readLine());
                }
            } catch (IOException ex) {
                Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Selects a random category for the for the random round from multiple text files.
     */
    public void randomCategory() {

        int categoryNumber = random.nextInt(11);

        if (categoryNumber == 0) {
            category = "Dictionary";
            inputStream = context.getResources().openRawResource(R.raw.list_dictionary);
        } else if (categoryNumber == 1) {
            category = "Gaming";
            inputStream = context.getResources().openRawResource(R.raw.list_videogames);
        } else if (categoryNumber == 2) {
            category = "Science";
            inputStream = context.getResources().openRawResource(R.raw.list_science);
        } else if (categoryNumber == 3) {
            category = "Movies";
            inputStream = context.getResources().openRawResource(R.raw.list_movies);
        } else if (categoryNumber == 4) {
            category = "Sport";
            inputStream = context.getResources().openRawResource(R.raw.list_sport);
        } else if (categoryNumber == 5) {
            category = "Animals";
            inputStream = context.getResources().openRawResource(R.raw.list_animals);
        } else if (categoryNumber == 6) {
            category = "Music";
            inputStream = context.getResources().openRawResource(R.raw.list_music);
        } else if (categoryNumber == 7) {
            category = "Festive";
            inputStream = context.getResources().openRawResource(R.raw.list_festive);
        }else if (categoryNumber == 8) {
            category = "Household";
            inputStream = context.getResources().openRawResource(R.raw.list_household);
        }else if (categoryNumber == 9) {
            category = "Places";
            inputStream = context.getResources().openRawResource(R.raw.list_places);
        }else if (categoryNumber == 10) {
            category = "Books";
            inputStream = context.getResources().openRawResource(R.raw.list_books);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while (br.ready()) {
                dictionary.add(br.readLine());
            }
        } catch (IOException ex) {
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks if the guessed letter has already been checked, if it has not it is added to the
     * guessed letter Arraylist and then passed on to the checkHangword method. The displayedWord
     * method is then called.
     */
    public void guessLetter(char guess) {
        if (!guessedLetters.contains(guess)) {
            guessedLetters.add(guess);
            checkHangword(guess);
        }
        displayedWord();
        if (!outputWord.contains("_")) {
            dictionary.remove(word);
            int randomNumber = random.nextInt();
                if(randomNumber % 2 == 0){
                    soundPool.play(soundWin, 1, 1, 0, 0, 1);
                }else {
                    soundPool.play(soundWinAlt, 1, 1, 0, 0, 1);
                }
        }
    }

    /**
     * Checks if the hangword contains the guessed Letter. If it does not contain the letter, the
     * player loses a life.
     */
    public void checkHangword(char guess) {
        if (!hangWord.contains(Character.toString(guess))) {
            guessesLeft = guessesLeft - 1;
            if (guessesLeft < 9 && guessesLeft > 5) {
                soundPool.play(soundWood, 1, 1, 0, 0, 1);
            } else if (guessesLeft == 5) {
                soundPool.play(soundRope, 1, 1, 0, 0, 1);
            } else if (guessesLeft < 5 && guessesLeft > 0) {
                soundPool.play(soundBody, 1, 1, 0, 0, 1);
            } else if (guessesLeft == 0) {
                soundPool.play(soundBody, 1, 1, 0, 0, 1);
                soundPool.play(soundLose, 1, 1, 0, 0, 1);
            }
        } else {
            soundPool.play(soundCorrectGuess, 1, 1, 0, 0, 1);
        }
    }

    /**
     * Displays the hangword to the player. It displays "_" for letters yet to be guessed and
     * reveals the letter if the player has guessed them. If there are two words, it is split
     * at the " " and put on to the next line.
     */
    public String displayedWord() {
        String displayWord = "";
        for (int i = 0; i < hangWord.length(); i++) {
            if (guessedLetters.contains(hangWord.charAt(i))) {
                displayWord = displayWord + hangWord.charAt(i);
            } else if (hangWord.charAt(i) == ' ') {
                displayWord = displayWord + "\n";
            } else {
                displayWord = displayWord + " _ ";
            }
            outputWord = displayWord;
        }
        return outputWord;
    }

    /**
     * Picks a random word from the already randomly selected category.
     */
    public String randomWord() {
        Random r = new Random();
        int wordIndex = Math.abs(r.nextInt()) % dictionary.size();
        word = dictionary.get(wordIndex);
        answer = "The answer was " + word;
        return word;
    }

    /**
     * Resets game and picks a new word.
     */
    public void newGame() {
        if (dictionary.size() == 0) {
            selectedCategory(passedCategory);
            Collections.shuffle(dictionary);
        }
        if (passedCategory.equals("random")) {
            selectedCategory(passedCategory);
        }
        hangWord = randomWord();
        setGuessesLeft(9);
        guessedLetters.clear();
    }

    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public int getGuessesLeft() {
        return guessesLeft;
    }

    public void setGuessesLeft(int guessesLeft) {
        this.guessesLeft = guessesLeft;
    }

    public String getOutputWord() {
        return outputWord;
    }

    public String getCategory() {
        return category;
    }

    public String getAnswer() {
        return answer;
    }
}