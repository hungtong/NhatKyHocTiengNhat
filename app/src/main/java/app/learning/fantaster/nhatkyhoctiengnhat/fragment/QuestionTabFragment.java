package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.learning.fantaster.nhatkyhoctiengnhat.activity.JLPTExam;
import app.learning.fantaster.nhatkyhoctiengnhat.R;

public class QuestionTabFragment extends Fragment {

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
                startActivity(intent);
            }
        });
    }
}
