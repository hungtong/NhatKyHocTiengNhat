package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseTabFragment;

public class DetailedClause extends AppCompatActivity {

    public static final String KEY_GET_CURRENT_EXAMPLE = "key to get current example";
    public static final String KEY_GET_UPDATED_EXAMPLE =  "key to get updated example";
    public static final String KEY_GET_UPDATED_LAST_EXAMPLE_ON =  "key to get updated last example on";

    public static final int REQUEST_CODE_EXAMPLE = 111;
    public static final int RESULT_CODE_OK = 970;

    private TextView exampleSection, lastExampleOnSection;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailed_clause_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    private void initialize() {
        exampleSection = (TextView) findViewById(R.id.example_section);
        lastExampleOnSection = (TextView) findViewById(R.id.lastExampleOn_section);

        findViewById(R.id.add_example_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedClause.this, ClauseSection.class);
                intent.putExtra(KEY_GET_CURRENT_EXAMPLE, exampleSection.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
            }
        });

        findViewById(R.id.agree_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedClause.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(KEY_GET_UPDATED_EXAMPLE, exampleSection.getText().toString());
                intent.putExtra(KEY_GET_UPDATED_LAST_EXAMPLE_ON, lastExampleOnSection.getText().toString());
                setResult(ClauseTabFragment.RESULT_CODE_OK, intent);
                finish();
            }
        });
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_OK && data != null) {
            String newExample = data.getStringExtra(ClauseSection.KEY_GET_NEW_EXAMPLE);
            exampleSection.setText(newExample);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma EEEE, d MMMM yyyy");
            ((TextView) findViewById(R.id.lastExampleOn_section)).setText(simpleDateFormat.format(new Date()));
        }
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
