package app.learning.fantaster.nhatkyhoctiengnhat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.adapter.DetailedResultAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Answer;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.SeeTranslationExplanation;

public class ExamResult extends AppCompatActivity{

    public static final String KEY_GET_TRANSLATION = "key to get translation";
    public static final String KEY_GET_EXPLANATION = "key to get explanation";

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

        findViewById(R.id.view_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exam_in_details);

                DetailedResultAdapter adapter = new DetailedResultAdapter(ExamResult.this, listAnswer, new SeeTranslationExplanation() {
                    @Override
                    public void onSeeTranslationExplanation(int position) {
                        Intent intent = new Intent(ExamResult.this, TranslationExplanation.class);
                        intent.putExtra(KEY_GET_TRANSLATION, listQuestion.get(position).translation);
                        intent.putExtra(KEY_GET_EXPLANATION, listQuestion.get(position).explanation);
                        startActivity(intent);
                    }
                });

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.share_result).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                takeScreenShotAndSave();
                shareResultImage();
                deleteScreenShot();
            }

        });

        findViewById(R.id.back_to_question_tab_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainScreen();
            }
        });

    }

    private void takeScreenShotAndSave() {
        View rootView = findViewById(R.id.exam_result);
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = rootView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput("screenshot.jpeg", MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); //100 is maximum quality
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            Log.d("File Not Found!", "Unable to open or create screenshot.jpeg");
        } catch (IOException ex) {
            Log.d("Wrong Input Output", ex.toString());
        }

    }

    private void shareResultImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        File imageFile = getFileStreamPath("screenshot.jpeg");
        Uri uri = Uri.fromFile(imageFile);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private synchronized void deleteScreenShot() {
        File imageFile = getFileStreamPath("screenshot.jpeg");
        imageFile.delete();
    }

    private void backToMainScreen() {
        Intent intent = new Intent(ExamResult.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMainScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
