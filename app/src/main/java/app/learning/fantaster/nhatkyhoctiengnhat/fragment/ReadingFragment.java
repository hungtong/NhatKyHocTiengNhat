package app.learning.fantaster.nhatkyhoctiengnhat.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.learning.fantaster.nhatkyhoctiengnhat.Exam;
import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.util.OptionClickListener;

public class ReadingFragment extends Fragment {

    private Exam exam;
    private TextView questionView;
    private Button option1, option2, option3, option4;

    private OptionClickListener listener; // sending information

    public TextView getQuestionView() { return questionView; }

    public Button getOption1() { return option1; }
    public Button getOption2() { return option2; }
    public Button getOption3() { return option3; }
    public Button getOption4() { return option4; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        exam = (Exam) getContext();
        questionView = (TextView) view.findViewById(R.id.reading_question);

        option1 = (Button) view.findViewById(R.id.option1_in_reading_question);
        option1.setOnClickListener(new OnChooseListener());

        option2 = (Button) view.findViewById(R.id.option2_in_reading_question);
        option2.setOnClickListener(new OnChooseListener());

        option3 = (Button) view.findViewById(R.id.option3_in_reading_question);
        option3.setOnClickListener(new OnChooseListener());

        option4 = (Button) view.findViewById(R.id.option4_in_reading_question);
        option4.setOnClickListener(new OnChooseListener());

        int firstQuestion = Exam.currentNumberOfQuestions;
        questionView.setText(exam.getQuestionAt(firstQuestion));
        option1.setText(exam.getOption(firstQuestion, Exam.OPTION_1));
        option2.setText(exam.getOption(firstQuestion, Exam.OPTION_2));
        option3.setText(exam.getOption(firstQuestion, Exam.OPTION_3));
        option4.setText(exam.getOption(firstQuestion, Exam.OPTION_4));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OptionClickListener) (context);
        } catch (ClassCastException ex) {
            throw new ClassCastException((context).toString() + " must implement OptionClickListener");
        }
    }

    class OnChooseListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int clickedId = view.getId();
            int chosenOptionId = exam.getChosenOptionAt(Exam.currentNumberOfQuestions);
            switch (clickedId) {
                case R.id.option1_in_reading_question :
                    if (chosenOptionId == 0)
                        option1.setBackgroundColor(Color.YELLOW);
                    else {
                        option1.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    break;
                case R.id.option2_in_reading_question :
                    if (chosenOptionId == 0)
                        option2.setBackgroundColor(Color.YELLOW);
                    else {
                        option2.setBackgroundColor(Color.YELLOW);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    break;
                case R.id.option3_in_reading_question :
                    if (chosenOptionId == 0)
                        option3.setBackgroundColor(Color.YELLOW);
                    else {
                        option3.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    break;
                case R.id.option4_in_reading_question :
                    if (chosenOptionId == 0)
                        option4.setBackgroundColor(Color.YELLOW);
                    else {
                        option4.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    break;
            }
            listener.onOptionClick(clickedId, ((Button) view).getText().toString());
        }

    }

}
