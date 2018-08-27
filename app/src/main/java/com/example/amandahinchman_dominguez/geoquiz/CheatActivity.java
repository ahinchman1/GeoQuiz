package com.example.amandahinchman_dominguez.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.amandahinchman_dominguez.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.amandahinchman_dominguez.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    private static final String WAS_ANSWER_SHOWN = "isAnswerShown";
    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private TextView mAndroidAPILevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Log.d(TAG, "onCreate(Bundle) called");

        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(WAS_ANSWER_SHOWN);
        } else {
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        }

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mAndroidAPILevel = findViewById(R.id.api_level);
        mAndroidAPILevel.setText("API Level " + Build.VERSION.SDK_INT);

        Button showAnswerButton = findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
        finish();
    }

    // encapsulate the implementation details of what CheatActivity expects
    // as extras on its Intent here
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    // decode the extra into something QuiActivity can use
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(WAS_ANSWER_SHOWN, mAnswerIsTrue);
    }
}
