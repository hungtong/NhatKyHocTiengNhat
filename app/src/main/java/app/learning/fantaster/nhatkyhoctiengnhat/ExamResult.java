package app.learning.fantaster.nhatkyhoctiengnhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;

public class ExamResult extends AppCompatActivity{

    private ArrayList<Question> list;
    private String[] options1, options2, options3, options4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exam_result);

        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();

        String totalTime = String.format(getString(R.string.total_time), intent.getStringExtra(JLPTExam.KEY_GET_TIME_USED));
        ((TextView) findViewById(R.id.total_time)).setText(totalTime);

        int numberOfAttempts = intent.getIntExtra(JLPTExam.KEY_GET_ATTEMPTS, 0);
        String attempts = String.format(getString(R.string.attempts), numberOfAttempts);
        ((TextView) findViewById(R.id.attempts)).setText(attempts);

        int numberOfRightAnswer =  intent.getIntExtra(JLPTExam.KEY_GET_POINTS, 0);
        String rightAnswers = String.format(getString(R.string.right_answers), numberOfRightAnswer);
        ((TextView) findViewById(R.id.right_answers)).setText(rightAnswers);

        String wrongAnswers = String.format(getString(R.string.wrong_answers), numberOfAttempts - numberOfRightAnswer);
        ((TextView) findViewById(R.id.wrong_answers)).setText(wrongAnswers);

    }

}
