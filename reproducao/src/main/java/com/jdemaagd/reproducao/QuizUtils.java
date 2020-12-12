package com.jdemaagd.reproducao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class QuizUtils {

    private static final String CURRENT_SCORE_KEY = "current_score";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String GAME_FINISHED = "game_finished";
    private static final int NUM_ANSWERS = 4;

    static void endGame(Context context){
        Intent endGame = new Intent(context, MainActivity.class);
        endGame.putExtra(GAME_FINISHED, true);
        context.startActivity(endGame);
    }

    /**
     * Generate ArrayList of Integers that contain IDs to NUM_ANSWERS samples
     * Samples constitute possible answers to question
     *
     * @param remainingSampleIDs ArrayList of Integers which contain IDs of all samples not used yet
     * @return ArrayList of possible answers
     */
    static ArrayList<Integer> generateQuestion(ArrayList<Integer> remainingSampleIDs) {
        Collections.shuffle(remainingSampleIDs);

        ArrayList<Integer> answers = new ArrayList<>();

        for (int i = 0; i < NUM_ANSWERS; i++) {
            if (i < remainingSampleIDs.size()) {
                answers.add(remainingSampleIDs.get(i));
            }
        }

        return answers;
    }

    /**
     * Pick one of possible answers to be correct one at random
     *
     * @param answers possible answers to the question
     * @return correct answer
     */
    static int getCorrectAnswerID(ArrayList<Integer> answers){
        Random r = new Random();
        int answerIndex = r.nextInt(answers.size());

        return answers.get(answerIndex);
    }

    /**
     * Get user current score
     *
     * @param context application context
     * @return user current score
     */
    static int getCurrentScore(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        return mPreferences.getInt(CURRENT_SCORE_KEY, 0);
    }

    /**
     * Get user high score
     *
     * @param context application context
     * @return user high score
     */
    static int getHighScore(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        return mPreferences.getInt(HIGH_SCORE_KEY, 0);
    }

    /**
     * Set user current score
     *
     * @param context The application context
     * @param currentScore The user's current score
     */
    static void setCurrentScore(Context context, int currentScore){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_SCORE_KEY, currentScore);
        editor.apply();
    }

    /**
     * Set user high score
     *
     * @param context The application context
     * @param highScore The user's high score
     */
    static void setHighScore(Context context, int highScore){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }

    /**
     * Check that user selected answer is correct one
     *
     * @param correctAnswer correct answer
     * @param userAnswer user answer
     * @return true if user is correct, false otherwise
     */
    static boolean userCorrect(int correctAnswer, int userAnswer){
        return userAnswer == correctAnswer;
    }
}