package app.learning.fantaster.nhatkyhoctiengnhat;

import android.graphics.Color;
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
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ReadingFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.VocabGrammarFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.util.OptionClickListener;

public class Exam extends AppCompatActivity implements OptionClickListener {

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
    public static final String VOCAB_GRAMMAR_FRAGMENT_TAG = "vocab grammar tag";
    public static final String READING_FRAGMENT_TAG = "reading tag";


    public static int currentNumberOfQuestions = 0;

    private ArrayList<Question> list;
    private String[] questions;
    private String[] options1, options2, options3, options4;
    private int[] chosenOptionId;
    private boolean[] correctOrNot;

    public String getQuestionAt(int whichQuestions) {
        return questions[whichQuestions];
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

        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.question_container, new VocabGrammarFragment(), VOCAB_GRAMMAR_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onOptionClick(int chosenOptionId, String optionContent) {
        this.chosenOptionId[currentNumberOfQuestions] = chosenOptionId;
        if (optionContent.equalsIgnoreCase(list.get(currentNumberOfQuestions).correctAnswer))
            correctOrNot[currentNumberOfQuestions] = true;
    }

    private void prepareQuestion() {
        QuestionDatabaseHelper databaseHelper = new QuestionDatabaseHelper(Exam.this);
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


        questions = new String[TOTAL_QUESTIONS];
        options1 = new String[TOTAL_QUESTIONS];
        options2 = new String[TOTAL_QUESTIONS];
        options3 = new String[TOTAL_QUESTIONS];
        options4 = new String[TOTAL_QUESTIONS];

        Question newQuesstion;
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            newQuesstion = list.get(i);
            questions[i] = newQuesstion.question;
            shuffleOptions(newQuesstion, i);
        }

        chosenOptionId = new int[TOTAL_QUESTIONS];
        correctOrNot = new boolean[TOTAL_QUESTIONS];
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
        if (currentNumberOfQuestions < NUMBER_OF_VOCAB_QUESTIONS + NUMBER_OF_GRAMMAR_QUESTIONS - 1) {
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundResource(android.R.drawable.btn_default);
            currentNumberOfQuestions++; // super important
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            VocabGrammarFragment vocabGrammarFragment = (VocabGrammarFragment)
                    getSupportFragmentManager().findFragmentByTag(VOCAB_GRAMMAR_FRAGMENT_TAG);

            Question newQuestion = list.get(currentNumberOfQuestions);
            switch (newQuestion.questionType) {
                case VOCAB_TYPE :
                    vocabGrammarFragment.getTypeIndicator().setText(getString(R.string.vocab_title));
                    break;
                case GRAMMAR_TYPE :
                    vocabGrammarFragment.getTypeIndicator().setText(getString(R.string.grammar_title));
                    break;
            }
            vocabGrammarFragment.getQuestionView().setText(newQuestion.question);
            vocabGrammarFragment.getOption1().setText(options1[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption2().setText(options2[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption3().setText(options3[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption4().setText(options4[currentNumberOfQuestions]);
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundColor(Color.YELLOW);

        }
        else if (currentNumberOfQuestions == NUMBER_OF_VOCAB_QUESTIONS + NUMBER_OF_GRAMMAR_QUESTIONS - 1) {
            currentNumberOfQuestions++;
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.question_container, new ReadingFragment(), READING_FRAGMENT_TAG);
            fragmentTransaction.commit();

        }
    /*    else {
            // alert user that this is the last question, turn in or check again
        } */
    }

    private void moveToLastQuestion() {
        if (currentNumberOfQuestions > 0 && currentNumberOfQuestions < NUMBER_OF_VOCAB_QUESTIONS + NUMBER_OF_GRAMMAR_QUESTIONS - 1) {
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundResource(android.R.drawable.btn_default);
            currentNumberOfQuestions--; // super important
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            VocabGrammarFragment vocabGrammarFragment = (VocabGrammarFragment)
                    getSupportFragmentManager().findFragmentByTag(VOCAB_GRAMMAR_FRAGMENT_TAG);

            Question newQuestion = list.get(currentNumberOfQuestions);
            switch (newQuestion.questionType) {
                case VOCAB_TYPE :
                    vocabGrammarFragment.getTypeIndicator().setText(getString(R.string.vocab_title));
                    break;
                case GRAMMAR_TYPE :
                    vocabGrammarFragment.getTypeIndicator().setText(getString(R.string.grammar_title));
                    break;
            }
            vocabGrammarFragment.getQuestionView().setText(newQuestion.question);

            vocabGrammarFragment.getOption1().setText(options1[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption2().setText(options2[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption3().setText(options3[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption4().setText(options4[currentNumberOfQuestions]);
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundColor(Color.YELLOW);

        }
        else if (currentNumberOfQuestions == NUMBER_OF_VOCAB_QUESTIONS + NUMBER_OF_GRAMMAR_QUESTIONS - 1) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            VocabGrammarFragment vocabGrammarFragment = (VocabGrammarFragment)
                    getSupportFragmentManager().findFragmentByTag(VOCAB_GRAMMAR_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.question_container, vocabGrammarFragment, VOCAB_GRAMMAR_FRAGMENT_TAG);
            fragmentTransaction.commit();

            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundResource(android.R.drawable.btn_default);
            currentNumberOfQuestions--; // super important
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            Question newQuestion = list.get(currentNumberOfQuestions);
            vocabGrammarFragment.getTypeIndicator().setText(getString(R.string.grammar_title));

            vocabGrammarFragment.getQuestionView().setText(newQuestion.question);
            vocabGrammarFragment.getOption1().setText(options1[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption2().setText(options2[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption3().setText(options3[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption4().setText(options4[currentNumberOfQuestions]);
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundColor(Color.YELLOW);

        }
        else if (currentNumberOfQuestions < TOTAL_QUESTIONS - 1) {
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundResource(android.R.drawable.btn_default);
            currentNumberOfQuestions--; // super important
            questionCounting.setText(String.format(getString(R.string.out_of_total),
                    currentNumberOfQuestions + 1, TOTAL_QUESTIONS));

            VocabGrammarFragment vocabGrammarFragment = (VocabGrammarFragment)
                    getSupportFragmentManager().findFragmentByTag(VOCAB_GRAMMAR_FRAGMENT_TAG);

            Question newQuestion = list.get(currentNumberOfQuestions);

            vocabGrammarFragment.getQuestionView().setText(newQuestion.question);
            vocabGrammarFragment.getOption1().setText(options1[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption2().setText(options2[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption3().setText(options3[currentNumberOfQuestions]);
            vocabGrammarFragment.getOption4().setText(options4[currentNumberOfQuestions]);
            if (chosenOptionId[currentNumberOfQuestions] != 0)
                (findViewById(chosenOptionId[currentNumberOfQuestions])).setBackgroundColor(Color.YELLOW);

        }
        else Toast.makeText(getBaseContext(), getString(R.string.head_of_text), Toast.LENGTH_SHORT).show(); // Out of bound
    }


}