package app.learning.fantaster.nhatkyhoctiengnhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;
import app.learning.fantaster.nhatkyhoctiengnhat.database.DAOQuestion;
import app.learning.fantaster.nhatkyhoctiengnhat.database.QuestionDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.QuestionTabFragment;


public class JLPTExam extends Activity {

    private TextView questionView;
    private Button option1, option2, option3, option4;
    private ImageView backward, forward;
    private TextView questionCounting;

    private ArrayList<Question> list;

    private int totalNumberOfQuestions;
    private int currentNumberOfQuestions = 0;
    private boolean[] correctOrNot;
    private String[] options1;
    private String[] options2;
    private String[] options3;
    private String[] options4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jlpt_exam);

        inititiateUI();
    }

    private void inititiateUI() {
        questionView = (TextView) findViewById(R.id.question);
        option1 = (Button) findViewById(R.id.option1);
        option2 = (Button) findViewById(R.id.option2);
        option3 = (Button) findViewById(R.id.option3);
        option4 = (Button) findViewById(R.id.option4);
        backward = (ImageView) findViewById(R.id.backward_arrow);
        forward = (ImageView) findViewById(R.id.forward_arrow);
        questionCounting = (TextView) findViewById(R.id.question_counting);

        prepareQuestion();
        Question currentQuestion = list.get(0);

        questionView.setText(currentQuestion.question);

        shuffleOptions(currentQuestion);

        option1.setOnClickListener(new OptionClickListener());
        option2.setOnClickListener(new OptionClickListener());
        option3.setOnClickListener(new OptionClickListener());
        option4.setOnClickListener(new OptionClickListener());

        backward.setOnClickListener(new ArrowClickListener());
        forward.setOnClickListener(new ArrowClickListener());

        questionCounting.setText(String.format(getString(R.string.out_of_total),
                                currentNumberOfQuestions + 1, totalNumberOfQuestions));

    }

    /**
     * Basically, initiate QuestionDatabaseHelper as well as Date Object Access for Question to
     * get all questions in the table, shuffle questions
     */
    private void prepareQuestion() {
        QuestionDatabaseHelper databaseHelper = new QuestionDatabaseHelper(JLPTExam.this);
        try {
            databaseHelper.createDatabase();
        }
        catch (IOException ex) {
            throw new Error("Failed to create database.");
        }
        try {
            databaseHelper.openDatabase();
        } catch (SQLException ex) {
            throw new Error("Failed to open database.");
        }

        DAOQuestion dao = new DAOQuestion(databaseHelper);
        list = dao.getAllQuestions();
        Collections.shuffle(list); // shuffle the list

        totalNumberOfQuestions = list.size();
        correctOrNot = new boolean[totalNumberOfQuestions];
        options1 = new String[totalNumberOfQuestions];
        options2 = new String[totalNumberOfQuestions];
        options3 = new String[totalNumberOfQuestions];
        options4 = new String[totalNumberOfQuestions];

    }

    /**
     * Shuffle Options so that option A is not always the correct answer
     * @param currentQuestion - question proceeded at this point
     */
    private void shuffleOptions(Question currentQuestion) {
        String[] options = new String[4];

        options[0] = currentQuestion.correctAnswer;
        options[1] = currentQuestion.distractor1;
        options[2] = currentQuestion.distractor2;
        options[3] = currentQuestion.distractor3;

        Random random = new Random();
        int positionToSwap;
        String temp;
        for (int i = 0; i < options.length; i++) {
            positionToSwap = random.nextInt(options.length);
            temp = options[i];
            options[i] = options[positionToSwap];
            options[positionToSwap] = temp;
        }

        option1.setText(options[0]);
        options1[currentNumberOfQuestions] = options[0];

        option2.setText(options[1]);
        options2[currentNumberOfQuestions] = options[1];

        option3.setText(options[2]);
        options3[currentNumberOfQuestions] = options[2];

        option4.setText(options[3]);
        options4[currentNumberOfQuestions] = options[3];
    }

    /**
     *  Specify Click Handling for option buttons
     */
    class OptionClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String correctAnswer = list.get(currentNumberOfQuestions).correctAnswer;
            int chosenColor = getColor(R.color.button_chosen);
            switch (view.getId()) {
                case R.id.option1 :
                    option1.setBackgroundColor(chosenColor);
                    option2.setBackgroundResource(android.R.drawable.btn_default);
                    option3.setBackgroundResource(android.R.drawable.btn_default);
                    option4.setBackgroundResource(android.R.drawable.btn_default);
                    if (option1.getText().toString().equalsIgnoreCase(correctAnswer))
                        correctOrNot[currentNumberOfQuestions] = true;
                    break;
                case R.id.option2 :
                    option2.setBackgroundColor(chosenColor);
                    option1.setBackgroundResource(android.R.drawable.btn_default);
                    option3.setBackgroundResource(android.R.drawable.btn_default);
                    option4.setBackgroundResource(android.R.drawable.btn_default);
                    if (option2.getText().toString().equalsIgnoreCase(correctAnswer))
                        correctOrNot[currentNumberOfQuestions] = true;
                    break;
                case R.id.option3 :
                    option3.setBackgroundColor(chosenColor);
                    option2.setBackgroundResource(android.R.drawable.btn_default);
                    option1.setBackgroundResource(android.R.drawable.btn_default);
                    option4.setBackgroundResource(android.R.drawable.btn_default);
                    if (option3.getText().toString().equalsIgnoreCase(correctAnswer))
                        correctOrNot[currentNumberOfQuestions] = true;
                    break;
                case R.id.option4 :
                    option4.setBackgroundColor(chosenColor);
                    option2.setBackgroundResource(android.R.drawable.btn_default);
                    option3.setBackgroundResource(android.R.drawable.btn_default);
                    option1.setBackgroundResource(android.R.drawable.btn_default);
                    if (option4.getText().toString().equalsIgnoreCase(correctAnswer))
                        correctOrNot[currentNumberOfQuestions] = true;
                    break;
            }
        }

    }

    class ArrowClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backward_arrow :
                    moveToLastQuestion();
                    break;
                case R.id.forward_arrow :
                    moveToNextQuestion();
                    break;
            }
        }
    }

    /**
     * Click forward button to move to next question without checking the answer.
     * Assumed that in this case, user, for some reasons, does not want to answer by clicking an option
     * button.
     */
    private void moveToNextQuestion() {
        if (currentNumberOfQuestions < totalNumberOfQuestions) {
            currentNumberOfQuestions++;
            Question nextQuestion = list.get(currentNumberOfQuestions); // this is typically for questionView and questionCounting
            questionView.setText(nextQuestion.question);

            shuffleOptions(nextQuestion);

            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, totalNumberOfQuestions));
        }
        else calculatePointsAndReturnResult();  // If this is the last question
    }

    /**
     * Click backward button to move back to last question, retrieve last state without caring whether
     * the answer is right or wrong
     */
    private void moveToLastQuestion() {
        if (currentNumberOfQuestions > 1) {
            currentNumberOfQuestions--; // back up 1
            Question lastQuestion = list.get(currentNumberOfQuestions); // now we have the last question
            questionView.setText(lastQuestion.question);
            option1.setText(options1[currentNumberOfQuestions]);
            option2.setText(options2[currentNumberOfQuestions]);
            option3.setText(options3[currentNumberOfQuestions]);
            option4.setText(options4[currentNumberOfQuestions]);
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, totalNumberOfQuestions));
        }
        else Toast.makeText(getBaseContext(), "This is the first question!!!", Toast.LENGTH_SHORT).show();

    }


    private void calculatePointsAndReturnResult() {
        int points = 0;
        for (int i = 0; i < totalNumberOfQuestions; i++)
            if (correctOrNot[i])
                points++;
        alertUser(points);
    }

    private void alertUser(final int points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(JLPTExam.this);
        builder.setTitle(getString(R.string.alert_exam_ending));
        builder.setMessage(getString(R.string.do_you_want_to_check_again));
        builder.setPositiveButton(getString(R.string.finish_anyway), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent dataBack = new Intent();
                dataBack.putExtra(QuestionTabFragment.KEY_GET_POINTS, points);
                setResult(QuestionTabFragment.RESULT_OK, dataBack);

                JLPTExam.this.finish();
            }
        });
        builder.setNegativeButton(getString(R.string.i_want_to_check_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

}
