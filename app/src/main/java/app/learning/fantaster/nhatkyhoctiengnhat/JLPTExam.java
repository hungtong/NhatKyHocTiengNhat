package app.learning.fantaster.nhatkyhoctiengnhat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.DAOQuestion;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.QuestionDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.LongOptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.QuestionTabFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ShortOptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.util.OptionClickListener;

public class JLPTExam extends AppCompatActivity implements OptionClickListener {

    private TextView questionCounting;

    public static final int TOTAL_QUESTIONS = 25;
    public static final int NUMBER_OF_VOCAB_QUESTIONS = 15;
    public static final int NUMBER_OF_GRAMMAR_QUESTIONS = 5;
    public static final int NUMBER_OF_READING_QUESTIONS = 5;
    public static final int NUMBER_OF_OPTIONS = 4;
    public static final int OPTION_1 = 0;
    public static final int OPTION_2 = 1;
    public static final int OPTION_3 = 2;
    public static final int OPTION_4 = 3;
    public static final int VOCAB_TYPE = 1;
    public static final int GRAMMAR_TYPE = 2;
    public static final int READING_TYPE = 3;
    public static final String FRAGMENT_TAG_FOR_SHORT_OPTION = "fragment tag for short option";
    public static final String FRAGMENT_TAG_FOR_LONG_OPTION = "fragment tag for long option";


    public static int currentNumberOfQuestions = 0;

    private ArrayList<Question> list;
    private String[] options1, options2, options3, options4;
    private int[] chosenOptionId;
    private boolean[] correctOrNot;
    private String[] fragmentTag;

    public String getQuestionAt(int whichQuestions) {
        return list.get(whichQuestions).question;
    }

    public int getQuestionTypeAt(int whichQuestions) {
        return list.get(whichQuestions).questionType;
    }

    public String getOption(int whichQuestions, int whichOptions) {
        switch (whichOptions)  {
            case OPTION_1 :
                return options1[whichQuestions];
            case OPTION_2 :
                return options2[whichQuestions];
            case OPTION_3 :
                return options3[whichQuestions];
            default :
                return options4[whichQuestions];
        }
    }

    public int getChosenOptionAt(int whichQuestions) {
        return chosenOptionId[whichQuestions];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exam);

        questionCounting = (TextView) findViewById(R.id.question_counting);
        ImageView backward = (ImageView) findViewById(R.id.backward_arrow);
        ImageView forward = (ImageView) findViewById(R.id.forward_arrow);

        questionCounting.setText(String.format(getString(R.string.out_of_total),
                currentNumberOfQuestions + 1, TOTAL_QUESTIONS));
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextQuestion();
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLastQuestion();
            }
        });

        prepareQuestion();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentTag[currentNumberOfQuestions].equals(FRAGMENT_TAG_FOR_SHORT_OPTION))
            fragmentTransaction.replace(R.id.question_container, new ShortOptionFragment(), fragmentTag[currentNumberOfQuestions]);
        else fragmentTransaction.replace(R.id.question_container, new LongOptionFragment(), fragmentTag[currentNumberOfQuestions]);
        fragmentTransaction.commit();
    }

    @Override
    public void onOptionClick(int chosenOptionId, String optionContent) {
        this.chosenOptionId[currentNumberOfQuestions] = chosenOptionId;
        if (optionContent.equalsIgnoreCase(list.get(currentNumberOfQuestions).correctAnswer))
            correctOrNot[currentNumberOfQuestions] = true;
    }

    private void prepareQuestion() {
        QuestionDatabaseHelper databaseHelper = new QuestionDatabaseHelper(JLPTExam.this);
        try {
            databaseHelper.createDatabase();
        } catch (IOException ex) {
            Log.d("Failed", "Failed to create database");
        }
        try {
            databaseHelper.openDatabase();
        } catch (SQLException ex) {
            Log.d("Failed", "Failed to open database");
        }

        DAOQuestion dao = new DAOQuestion(databaseHelper);
        list = dao.getRandomTypeQuestions(NUMBER_OF_VOCAB_QUESTIONS, VOCAB_TYPE);
        list.addAll(dao.getRandomTypeQuestions(NUMBER_OF_GRAMMAR_QUESTIONS, GRAMMAR_TYPE));
        list.addAll(dao.getRandomTypeQuestions(NUMBER_OF_READING_QUESTIONS, READING_TYPE));

        options1 = new String[TOTAL_QUESTIONS];
        options2 = new String[TOTAL_QUESTIONS];
        options3 = new String[TOTAL_QUESTIONS];
        options4 = new String[TOTAL_QUESTIONS];

        chosenOptionId = new int[TOTAL_QUESTIONS];
        correctOrNot = new boolean[TOTAL_QUESTIONS];
        fragmentTag = new String[TOTAL_QUESTIONS];

        Question newQuestion;
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            newQuestion = list.get(i);
            shuffleOptions(newQuestion, i);
            fragmentTag[i] = FRAGMENT_TAG_FOR_SHORT_OPTION;
            if (options1[i].length() > 50 || options2[i].length() > 50 ||
                    options3[i].length() > 50 || options4[i].length() > 50) {
                fragmentTag[i] = FRAGMENT_TAG_FOR_LONG_OPTION;
            }
        }

    }

    private void shuffleOptions(Question question, int whichQuestion) {

        String[] options = new String[NUMBER_OF_OPTIONS];
        options[0] = question.correctAnswer;
        options[1] = question.distractor1;
        options[2] = question.distractor2;
        options[3] = question.distractor3;

        Random random = new Random();
        int positionToSwap;
        String temp;
        for (int i = 0; i < NUMBER_OF_OPTIONS; i++) {
            positionToSwap = random.nextInt(NUMBER_OF_OPTIONS);
            temp = options[i];
            options[i] = options[positionToSwap];
            options[positionToSwap] = temp;
        }

        options1[whichQuestion] = options[0];
        options2[whichQuestion] = options[1];
        options3[whichQuestion] = options[2];
        options4[whichQuestion] = options[3];
    }

    private void moveToNextQuestion() {
        if (currentNumberOfQuestions < TOTAL_QUESTIONS - 1) {
            currentNumberOfQuestions++;
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragmentTag[currentNumberOfQuestions].equals(FRAGMENT_TAG_FOR_SHORT_OPTION))
                fragmentTransaction.replace(R.id.question_container, new ShortOptionFragment(), fragmentTag[currentNumberOfQuestions]);
            else fragmentTransaction.replace(R.id.question_container, new LongOptionFragment(), fragmentTag[currentNumberOfQuestions]);
            fragmentTransaction.commit();
        }
        else calculatePointsAndReturnResult();
    }

    private void moveToLastQuestion() {
        if (currentNumberOfQuestions > 0) {
            currentNumberOfQuestions--;
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragmentTag[currentNumberOfQuestions].equals(FRAGMENT_TAG_FOR_SHORT_OPTION))
                fragmentTransaction.replace(R.id.question_container, new ShortOptionFragment(), fragmentTag[currentNumberOfQuestions]);
            else fragmentTransaction.replace(R.id.question_container, new LongOptionFragment(), fragmentTag[currentNumberOfQuestions]);
            fragmentTransaction.commit();
        }
        else Toast.makeText(getBaseContext(), getString(R.string.head_of_text), Toast.LENGTH_SHORT).show(); // Out of bound
    }

    private void calculatePointsAndReturnResult() {
        int points = 0;
        for (int i = 0; i < TOTAL_QUESTIONS; i++)
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