package app.learning.fantaster.nhatkyhoctiengnhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.adapter.DetailedResultAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Answer;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;

public class ExamResult extends AppCompatActivity{

    private ArrayList<Question> listQuestion;
    private ArrayList<Answer> listAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exam_result);

        initialize();
    }

    private void initialize() {
        final Intent intent = getIntent();

        listQuestion = intent.getParcelableArrayListExtra(JLPTExam.KEY_GET_QUESTION_LIST);
        listAnswer = intent.getParcelableArrayListExtra(JLPTExam.KEY_GET_ANSWERS_LIST);

        final String totalTime = String.format(getString(R.string.total_time), intent.getStringExtra(JLPTExam.KEY_GET_TIME_USED));
        ((TextView) findViewById(R.id.total_time)).setText(totalTime);

        int numberOfAttempts = intent.getIntExtra(JLPTExam.KEY_GET_ATTEMPTS, 0);
        final String attempts = String.format(getString(R.string.attempts), numberOfAttempts);
        ((TextView) findViewById(R.id.attempts)).setText(attempts);

        int numberOfRightAnswer =  intent.getIntExtra(JLPTExam.KEY_GET_POINTS, 0);
        final String rightAnswers = String.format(getString(R.string.right_answers), numberOfRightAnswer);
        ((TextView) findViewById(R.id.right_answers)).setText(rightAnswers);

        final String wrongAnswers = String.format(getString(R.string.wrong_answers), numberOfAttempts - numberOfRightAnswer);
        ((TextView) findViewById(R.id.wrong_answers)).setText(wrongAnswers);

        ((Button) findViewById(R.id.view_details)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exam_in_details);

                DetailedResultAdapter adapter = new DetailedResultAdapter(ExamResult.this, listAnswer);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        ((Button) findViewById(R.id.share_result)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // mime type
                StringBuffer contentToSend = new StringBuffer();
                contentToSend.append(totalTime).append("\n");
                contentToSend.append(attempts).append("\n");
                contentToSend.append(rightAnswers).append(" / ").append(JLPTExam.TOTAL_QUESTIONS).append("\n");
                contentToSend.append(wrongAnswers).append(" / ").append(JLPTExam.TOTAL_QUESTIONS).append("\n");
                intent.putExtra(Intent.EXTRA_TEXT, contentToSend.toString());
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
            }

        });

    }

}
