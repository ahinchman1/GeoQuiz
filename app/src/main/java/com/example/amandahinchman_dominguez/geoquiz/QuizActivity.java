package com.example.amandahinchman_dominguez.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String CHEAT_VALUE = "isCheater";
    private static final String CHEAT_TOKEN_COUNT = "CheatTokenCount";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int numQuestionsRight = 0;
    private double percentage = ((double) numQuestionsRight) / ((double) mQuestionBank.length) * 100;

    private int mCurrentIndex = 0;
    private int mCheatTokenCount = 0;
    private boolean mIsCheater;     // holds the value that CheatActivity is passing back
    TextView mQuestionTextView;
    private Button mFalseButton;
    private Button mTrueButton;
    private Button mCheatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        mFalseButton = findViewById(R.id.false_button);
        mTrueButton = findViewById(R.id.true_button);
        mCheatButton = findViewById(R.id.cheat_button);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEAT_VALUE);
            mCheatTokenCount = savedInstanceState.getInt(CHEAT_TOKEN_COUNT, 0);
        }

        enableAnswers(!mIsCheater);
        mQuestionTextView = findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextRestId();
        mQuestionTextView.setText(question);

        if (mCheatTokenCount >= 3) {
            mCheatButton.setEnabled(false);
        }

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                enableAnswers(false);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                enableAnswers(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        ImageButton mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (mCurrentIndex < mQuestionBank.length) {
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    mIsCheater = false;
                    int question = mQuestionBank[mCurrentIndex].getTextRestId();
                    mQuestionTextView.setText(question);
                    enableAnswers(true);
                    updateQuestion();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEAT_VALUE, mIsCheater);
        savedInstanceState.putInt(CHEAT_TOKEN_COUNT, mCheatTokenCount);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void enableAnswers(boolean enable) {
            mTrueButton.setEnabled(enable);
            mFalseButton.setEnabled(enable);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextRestId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
            mCheatTokenCount++;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                numQuestionsRight++;
                percentage = ((double) numQuestionsRight) / ((double) mQuestionBank.length) * 100;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        String SCORE = ((int) percentage) + "%";
        Toast toast = Toast.makeText(this, getResources().getString(messageResId) + "\nScore: " + SCORE, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // retrieve the value passed from CheatActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            checkAnswer(true);
        }
    }
}
