package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.UserAddOnAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Clause;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseTabFragment;

public class DetailedClause extends AppCompatActivity {

    public static final String KEY_GET_UPDATED_EXAMPLE =  "key to get updated example";
    public static final String KEY_GET_UPDATED_MEMORY_TRICK = "key to get updated memory trick";
    public static final String KEY_GET_UPDATED_LAST_EXAMPLE_ON =  "key to get updated last example on";

    public static final int REQUEST_CODE_EXAMPLE = 111;
    public static final int REQUEST_CODE_MEMORY_TRICK = 112;
    public static final int RESULT_CODE_OK = 970;

    private TextView lastExampleOnSection;
    private RecyclerView exampleSection, memoryTrickSection;
    private UserAddOnAdapter exampleAdapter, memoryTrickAdapter;
    private ArrayList<String> examples, memoryTricks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailed_clause);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    private void initialize() {
        Clause clauseSelected = getIntent().getParcelableExtra(ClauseTabFragment.KEY_GET_CURRENT_CLAUSE);
        ((TextView) findViewById(R.id.formula_section)).setText(clauseSelected.formula);
        ((TextView) findViewById(R.id.brief_summary_section)).setText(clauseSelected.briefSummary);
        ((TextView) findViewById(R.id.explanation_section)).setText(clauseSelected.explanation);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < clauseSelected.topic.size(); i++) {
            builder.append(clauseSelected.topic.get(i)).append("\n");
        }
        ((TextView) findViewById(R.id.topic_section)).setText(builder.toString());

        lastExampleOnSection = (TextView) findViewById(R.id.lastExampleOn_section);
        lastExampleOnSection.setText(clauseSelected.lastExampleOn);

        exampleSection = (RecyclerView) findViewById(R.id.example_section);
    //    exampleSection.setNestedScrollingEnabled(false);
        memoryTrickSection = (RecyclerView) findViewById(R.id.memory_trick_section);
  //      memoryTrickSection.setNestedScrollingEnabled(false);

        examples = new ArrayList<>(clauseSelected.example);
        exampleAdapter = new UserAddOnAdapter(DetailedClause.this, examples);
        memoryTricks = new ArrayList<>(clauseSelected.memoryTrick);
        memoryTrickAdapter = new UserAddOnAdapter(DetailedClause.this, memoryTricks);

        RecyclerView.LayoutManager exampleLayoutManager = new LinearLayoutManager(DetailedClause.this);
        RecyclerView.LayoutManager memoryTrickLayoutManager = new LinearLayoutManager(DetailedClause.this);

        exampleSection.setAdapter(exampleAdapter);
        exampleSection.setLayoutManager(exampleLayoutManager);
        memoryTrickSection.setAdapter(memoryTrickAdapter);
        memoryTrickSection.setLayoutManager(memoryTrickLayoutManager);

        findViewById(R.id.add_example_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedClause.this, NewExample.class);
                startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
            }
        });

        findViewById(R.id.add_memory_trick_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedClause.this, NewMemoryTrick.class);
                startActivityForResult(intent, REQUEST_CODE_MEMORY_TRICK);
            }
        });

        findViewById(R.id.agree_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedClause.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putStringArrayListExtra(KEY_GET_UPDATED_EXAMPLE, examples);
                intent.putStringArrayListExtra(KEY_GET_UPDATED_MEMORY_TRICK, memoryTricks);
                intent.putExtra(KEY_GET_UPDATED_LAST_EXAMPLE_ON, lastExampleOnSection.getText().toString());
                setResult(ClauseTabFragment.RESULT_CODE_OK, intent);
                finish();
            }
        });
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CODE_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_EXAMPLE :
                    String newExample = data.getStringExtra(NewExample.KEY_GET_NEW_EXAMPLE);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma EEEE, d MMMM yyyy");
                    ((TextView) findViewById(R.id.lastExampleOn_section)).setText(simpleDateFormat.format(new Date()));
                    examples.add(newExample);
                    exampleAdapter.notifyDataSetChanged();

                    break;

                case REQUEST_CODE_MEMORY_TRICK :
                    String newMemoryTrick = data.getStringExtra(NewMemoryTrick.KEY_GET_NEW_MEMORY_TRICK);
                    memoryTricks.add(newMemoryTrick);
                    memoryTrickAdapter.notifyDataSetChanged();
                    break;
            }

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
