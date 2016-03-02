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

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.activity.JLPTExam;

public class OptionInLandscapeFragment extends Fragment {

    private JLPTExam exam;
    private Button option1, option2, option3, option4;

    private OptionClickListener listener; // sending information

    public interface OptionClickListener {
        void onOptionClick(int chosenOptionId, String optionContent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_for_option_landscape, container, false);
    }

    @Override
    public void onViewCreated(View landscapeView, Bundle savedInstanceState) {
        exam = (JLPTExam) getActivity();

        TextView typeIndicator = (TextView) landscapeView.findViewById(R.id.option_title_landscape);
        TextView questionView = (TextView) landscapeView.findViewById(R.id.question_in_fragment_landscape);

        int currentQuestion = JLPTExam.currentNumberOfQuestions;
        questionView.setText(exam.getQuestionAt(currentQuestion));

        option1 = (Button) landscapeView.findViewById(R.id.option1_in_fragment_landscape);
        option2 = (Button) landscapeView.findViewById(R.id.option2_in_fragment_landscape);
        option3 = (Button) landscapeView.findViewById(R.id.option3_in_fragment_landscape);
        option4 = (Button) landscapeView.findViewById(R.id.option4_in_fragment_landscape);

        option1.setOnClickListener(new OnChooseListener());
        option2.setOnClickListener(new OnChooseListener());
        option3.setOnClickListener(new OnChooseListener());
        option4.setOnClickListener(new OnChooseListener());

        switch (exam.getQuestionTypeAt(currentQuestion)) {
            case JLPTExam.VOCAB_TYPE :
                typeIndicator.setText(getString(R.string.vocab_title));
                break;
            case JLPTExam.GRAMMAR_TYPE :
                typeIndicator.setText(getString(R.string.grammar_title));
                break;
            case JLPTExam.READING_TYPE :
                typeIndicator.setText(getString(R.string.reading_title));
                break;
        }

        option1.setText(String.format(getString(R.string.optionA), exam.getOption(currentQuestion, JLPTExam.OPTION_1)));
        option2.setText(String.format(getString(R.string.optionB), exam.getOption(currentQuestion, JLPTExam.OPTION_2)));
        option3.setText(String.format(getString(R.string.optionC), exam.getOption(currentQuestion, JLPTExam.OPTION_3)));
        option4.setText(String.format(getString(R.string.optionD), exam.getOption(currentQuestion, JLPTExam.OPTION_4)));

        int currentChosenOptionId = exam.getChosenOptionAt(currentQuestion);
        if (currentChosenOptionId != 0) {
            option1.setBackgroundResource(android.R.drawable.btn_default);
            option2.setBackgroundResource(android.R.drawable.btn_default);
            option3.setBackgroundResource(android.R.drawable.btn_default);
            option4.setBackgroundResource(android.R.drawable.btn_default);
            if (currentChosenOptionId == R.id.option1_in_fragment_landscape || currentChosenOptionId == R.id.option1_in_fragment_portrait)
                option1.setBackgroundColor(Color.YELLOW);
            if (currentChosenOptionId == R.id.option2_in_fragment_landscape || currentChosenOptionId == R.id.option2_in_fragment_portrait)
                option2.setBackgroundColor(Color.YELLOW);
            if (currentChosenOptionId == R.id.option3_in_fragment_landscape || currentChosenOptionId == R.id.option3_in_fragment_portrait)
                option3.setBackgroundColor(Color.YELLOW);
            if (currentChosenOptionId == R.id.option4_in_fragment_landscape || currentChosenOptionId == R.id.option4_in_fragment_portrait)
                option4.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OptionClickListener) (context);
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement OptionClickListener");
        }
    }

    class OnChooseListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int clickedId = view.getId();
            int currentQuestion = JLPTExam.currentNumberOfQuestions;
            int chosenOptionId = exam.getChosenOptionAt(JLPTExam.currentNumberOfQuestions);

            switch (clickedId) {
                case R.id.option1_in_fragment_landscape :
                    if (chosenOptionId == 0)
                        option1.setBackgroundColor(Color.YELLOW);
                    else {
                        option1.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    listener.onOptionClick(clickedId, exam.getOption(currentQuestion, JLPTExam.OPTION_1));
                    break;

                case R.id.option2_in_fragment_landscape :
                    if (chosenOptionId == 0)
                        option2.setBackgroundColor(Color.YELLOW);
                    else {
                        option2.setBackgroundColor(Color.YELLOW);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    listener.onOptionClick(clickedId, exam.getOption(currentQuestion, JLPTExam.OPTION_2));
                    break;

                case R.id.option3_in_fragment_landscape :

                    if (chosenOptionId == 0)
                        option3.setBackgroundColor(Color.YELLOW);
                    else {
                        option3.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                        option4.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    listener.onOptionClick(clickedId, exam.getOption(currentQuestion, JLPTExam.OPTION_3));
                    break;

                case R.id.option4_in_fragment_landscape :

                    if (chosenOptionId == 0)
                        option4.setBackgroundColor(Color.YELLOW);
                    else {
                        option4.setBackgroundColor(Color.YELLOW);
                        option2.setBackgroundResource(android.R.drawable.btn_default);
                        option3.setBackgroundResource(android.R.drawable.btn_default);
                        option1.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    listener.onOptionClick(clickedId, exam.getOption(currentQuestion, JLPTExam.OPTION_4));
                    break;
            }
        }
    }

}
