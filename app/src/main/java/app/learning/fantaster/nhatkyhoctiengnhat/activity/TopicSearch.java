package app.learning.fantaster.nhatkyhoctiengnhat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.adapter.TopicSearchAdapter;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ClauseTabFragment;


public class TopicSearch extends AppCompatActivity {

    public static final String KEY_GET_DESIRED_TOPIC = "key to get desired topic";

    private RecyclerView topicsAvailable;
    private  AutoCompleteTextView topicSearchArea;
    private TopicSearchAdapter adapter;
    private ArrayList<String> listTopic;
    private ArrayList<String> backupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_topic);

        initializeTopicsAvailable();
        initializeSearchArea();
        initializeSearchKeywordAndReset();
    }

    private void initializeTopicsAvailable() {
        topicsAvailable = (RecyclerView) findViewById(R.id.topics_list);

        listTopic = new ArrayList<>(getIntent().getStringArrayListExtra(ClauseTabFragment.KEY_GET_LIST_TOPIC));
        backupList = new ArrayList<>(listTopic);

        adapter = new TopicSearchAdapter(TopicSearch.this, listTopic, new TopicSearchAdapter.TopicSelectedListener() {
            @Override
            public void onTopicSelectedListener(String contentAtThisPosition) {
                Intent intent = new Intent(TopicSearch.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(KEY_GET_DESIRED_TOPIC, contentAtThisPosition);
                setResult(ClauseTabFragment.RESULT_CODE_OK, intent);
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TopicSearch.this);
        topicsAvailable.setAdapter(adapter);
        topicsAvailable.setLayoutManager(layoutManager);
    }

    private void initializeSearchArea() {
        topicSearchArea = (AutoCompleteTextView) findViewById(R.id.search_topic_area);

        topicSearchArea.setAdapter(new ArrayAdapter<String>(TopicSearch.this, android.R.layout.simple_list_item_1, listTopic));
        topicSearchArea.setThreshold(1);
        topicSearchArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopicSearch.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(KEY_GET_DESIRED_TOPIC, (String) parent.getItemAtPosition(position));
                setResult(ClauseTabFragment.RESULT_CODE_OK, intent);
                finish();
            }
        });
    }

    private void initializeSearchKeywordAndReset() {
        findViewById(R.id.search_topic_by_keyword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = topicSearchArea.getText().toString().toLowerCase();
                ArrayList<String> temporaryList = new ArrayList<String>();
                for (int i = 0; i < listTopic.size(); i++)
                    if (listTopic.get(i).toLowerCase().contains(keyword))
                        temporaryList.add(listTopic.get(i));
                if (temporaryList.size() > 0) {
                    listTopic.clear();
                    listTopic.addAll(temporaryList);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        findViewById(R.id.reset_topic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTopic.clear();
                listTopic.addAll(backupList);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
