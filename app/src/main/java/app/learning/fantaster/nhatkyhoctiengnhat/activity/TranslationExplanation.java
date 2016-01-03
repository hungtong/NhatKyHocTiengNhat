package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class TranslationExplanation extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_translation_explanation);

        unpackData();
    }

    private void unpackData() {
        ((TextView) findViewById(R.id.question_translation)).setText(getIntent().getStringExtra(ExamResult.KEY_GET_TRANSLATION));
        ((TextView) findViewById(R.id.question_explanation)).setText(getIntent().getStringExtra(ExamResult.KEY_GET_EXPLANATION));
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
