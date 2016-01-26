package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class AddOnModification extends AppCompatActivity {

    public static final String KEY_TO_GET_MODIFIED_CONTENT = "key to get modified content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_user_add_on);

        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();
        ((TextView) findViewById(R.id.section_modify_title)).setText(intent.getStringExtra(DetailedClause.KEY_GET_WHAT_TO_MODIFY));
        ((EditText) findViewById(R.id.section_modify_content)).setText(intent.getStringExtra(DetailedClause.KEY_GET_CONTENT_TO_MODIFY));
        findViewById(R.id.make_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(AddOnModification.this, DetailedClause.class);
                String modifiedContent = ( ((EditText) findViewById(R.id.section_modify_content)).getText().toString());
                intentBack.putExtra(KEY_TO_GET_MODIFIED_CONTENT, modifiedContent);
                setResult(DetailedClause.RESULT_CODE_OK, intentBack);
                finish();
            }
        });
    }

}
