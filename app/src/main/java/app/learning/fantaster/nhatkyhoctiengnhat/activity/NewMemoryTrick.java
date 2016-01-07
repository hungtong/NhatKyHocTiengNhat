package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class NewMemoryTrick extends AppCompatActivity {

    public static final String KEY_GET_NEW_MEMORY_TRICK = "key to get new memory trick";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_memory_trick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();
        ((EditText) findViewById(R.id.section_content)).setText(intent.getStringExtra(DetailedClause.KEY_GET_CURRENT_EXAMPLE));
        findViewById(R.id.make_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMemoryTrick.this, DetailedClause.class);
                String newSectionContent = ((EditText) findViewById(R.id.section_content)).getText().toString();
                intent.putExtra(KEY_GET_NEW_MEMORY_TRICK, newSectionContent);
                setResult(DetailedClause.RESULT_CODE_OK, intent);
                finish();
            }
        });
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
