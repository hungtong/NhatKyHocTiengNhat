package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.learning.fantaster.nhatkyhoctiengnhat.JLPTExam;
import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class QuestionTabFragment extends Fragment {

    public static final int REQUESTED_CODE = 301;
    public static final int RESULT_OK = 201;
    public static final String KEY_GET_POINTS = "key to get points";

    private TextView textView;


    public static QuestionTabFragment newInstance() {
        return new QuestionTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_question_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        Button button = (Button) view.findViewById(R.id.start_exam);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JLPTExam.class);
                startActivityForResult(intent, REQUESTED_CODE);
            }
        });
        textView = (TextView) view.findViewById(R.id.display_exam_result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTED_CODE && resultCode == RESULT_OK && data != null) {
            textView.setText(String.format(getString(R.string.result), data.getIntExtra(KEY_GET_POINTS, 0)));
        }
    }


}
