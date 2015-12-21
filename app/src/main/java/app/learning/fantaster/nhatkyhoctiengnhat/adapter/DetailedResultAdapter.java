package app.learning.fantaster.nhatkyhoctiengnhat.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Answer;

public class DetailedResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CORRECT_ANSWER = 53917;
    public static final int INCORRECT_ANSWER = 38260;
    public static final int UNATTEMPTED_ANSWER = 29333;

    private Activity context;
    private ArrayList<Answer> list;

    public DetailedResultAdapter(Activity context, ArrayList<Answer> list) {
        this.context = context;
        this.list = list;
    }

    static class CorrectAnswerHolder extends RecyclerView.ViewHolder {
        public final TextView questionNumber, question, userAnswer, correctAnswer;
        public final ImageView symbol;

        public CorrectAnswerHolder(View view) {
            super(view);
            questionNumber = (TextView) view.findViewById(R.id.question_number_correct_entry);
            question = (TextView) view.findViewById(R.id.brief_question_correct_entry);
            userAnswer = (TextView) view.findViewById(R.id.user_answer_correct_entry);
            correctAnswer = (TextView) view.findViewById(R.id.correct_answer_correct_entry);
            symbol = (ImageView) view.findViewById(R.id.right_symbol);
        }

    }

    static class IncorrectAnswerHolder extends RecyclerView.ViewHolder {
        public final TextView questionNumber, question, userAnswer, correctAnswer;
        public final ImageView symbol;

        public IncorrectAnswerHolder(View view) {
            super(view);
            questionNumber = (TextView) view.findViewById(R.id.question_number_incorrect_entry);
            question = (TextView) view.findViewById(R.id.brief_question_incorrect_entry);
            userAnswer = (TextView) view.findViewById(R.id.user_answer_incorrect_entry);
            correctAnswer = (TextView) view.findViewById(R.id.correct_answer_incorrect_entry);
            symbol = (ImageView) view.findViewById(R.id.wrong_symbol);
        }

    }

    static class UnattemptedAnswerHolder extends RecyclerView.ViewHolder {
        public final TextView questionNumber, question, correctAnswer;

        public UnattemptedAnswerHolder(View view) {
            super(view);
            questionNumber = (TextView) view.findViewById(R.id.question_number_unattempted_entry);
            question = (TextView) view.findViewById(R.id.brief_question_unattempted_entry);
            correctAnswer = (TextView) view.findViewById(R.id.correct_answer_unattempted_entry);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Answer answer = list.get(position);
        if (answer.attemptedOrNot == 0)
            return UNATTEMPTED_ANSWER;
        else {
            if (answer.correctOrNot == 0)
                return INCORRECT_ANSWER;
            return CORRECT_ANSWER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        View view;
        switch (viewType) {
            case UNATTEMPTED_ANSWER :
                view = context.getLayoutInflater().inflate(R.layout.unattempted_entry, container, false);
                return new UnattemptedAnswerHolder(view);
            case INCORRECT_ANSWER :
                view = context.getLayoutInflater().inflate(R.layout.incorrect_answer_entry, container, false);
                return new IncorrectAnswerHolder(view);
            default :
                view = context.getLayoutInflater().inflate(R.layout.correct_answer_entry, container, false);
                return new CorrectAnswerHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case UNATTEMPTED_ANSWER :

                UnattemptedAnswerHolder unattemptedAnswerHolder = (UnattemptedAnswerHolder) viewHolder;
                unattemptedAnswerHolder.questionNumber.setText(position);
                unattemptedAnswerHolder.question.setText(list.get(position).question);
                unattemptedAnswerHolder.correctAnswer.setText(list.get(position).correctAnswer);
                break;

            case INCORRECT_ANSWER :

                IncorrectAnswerHolder incorrectAnswerHolder = (IncorrectAnswerHolder) viewHolder;
                incorrectAnswerHolder.questionNumber.setText(position);
                incorrectAnswerHolder.question.setText(list.get(position).question);
                incorrectAnswerHolder.userAnswer.setText(list.get(position).answer);
                incorrectAnswerHolder.correctAnswer.setText(list.get(position).correctAnswer);
                incorrectAnswerHolder.symbol.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                break;

            case CORRECT_ANSWER :

                CorrectAnswerHolder correctAnswerHolder = (CorrectAnswerHolder) viewHolder;
                correctAnswerHolder.questionNumber.setText(position);
                correctAnswerHolder.question.setText(list.get(position).question);
                correctAnswerHolder.userAnswer.setText(list.get(position).answer);
                correctAnswerHolder.correctAnswer.setText(list.get(position).correctAnswer);
                correctAnswerHolder.symbol.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
