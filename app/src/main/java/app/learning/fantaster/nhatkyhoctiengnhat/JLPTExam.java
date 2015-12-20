package app.learning.fantaster.nhatkyhoctiengnhat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.DAOQuestion;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.QuestionDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.LongOptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.ShortOptionFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.util.listener.OptionClickListener;

/**
 * - This is our exam
 * - The Activity will first get all questions in 3 categories : 5 Grammar, 15 Vocab and 5 Reading. Initially
 * A is always the correct answer so it is crucial to shuffleOptions
 * - Depending on how long the options of a question is, 2 will have two ways to represent questions,
 * which are using Short Fragment or Long Fragment.
 * - Every time clicking in an option, it will be highlighted and its id will be stored so that it is easy to
 * retrieve when moving backward or forward when all the buttons have to become default state.
 * - To move backward or forward we only need to adjust current number of questions and display data which were
 * initialized at the beginning.
 */
public class JLPTExam extends AppCompatActivity implements OptionClickListener {

    private TextView questionCounting;
    private TextView countdownTimer;

    public static String KEY_GET_TIME_USED = "keyy to get time used";
    public static String KEY_GET_ATTEMPTS = "key to get attempts";
    public static String KEY_GET_QUESTION_LIST = "key to get question list";
    public static String KEY_GET_POINTS = "key to get points";
    public static String KEY_GET_OPTIONS_1 = "key to get options 1 array";
    public static String KEY_GET_OPTIONS_2 = "key to get options 2 array";
    public static String KEY_GET_OPTIONS_3 = "key to get options 3 array";
    public static String KEY_GET_OPTIONS_4 = "key to get options 4 array";
    public static int TOTAL_QUESTIONS = 25;
    public static int NUMBER_OF_VOCAB_QUESTIONS = 15;
    public static int NUMBER_OF_GRAMMAR_QUESTIONS = 5;
    public static int NUMBER_OF_READING_QUESTIONS = 5;
    public static int NUMBER_OF_OPTIONS = 4;
    public static final int OPTION_1 = 0;
    public static final int OPTION_2 = 1;
    public static final int OPTION_3 = 2;
    public static final int OPTION_4 = 3;
    public static final int VOCAB_TYPE = 1;
    public static final int GRAMMAR_TYPE = 2;
    public static final int READING_TYPE = 3;
    public static String FRAGMENT_TAG_FOR_SHORT_OPTION = "fragment tag for short option";
    public static String FRAGMENT_TAG_FOR_LONG_OPTION = "fragment tag for long option";
    public static long TOTAL_TIME_TO_FINISH_EXAM = 1200000;
    public static long INTERVAL = 1000;

    public static int currentNumberOfQuestions = 0;

    private ArrayList<Question> list;
    private String[] options1, options2, options3, options4;
    private int[] chosenOptionId;
    private boolean[] correctOrNot;
    private String[] fragmentTag;
    private JLPTExamCountDownTimer timer;

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
        setContentView(R.layout.layout_jlpt_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Since, currentNumberOfQuestions is static so it will get sticked with the class itself even if
     * the activity is destroyed. To make sure, the exam always starts off at 1, make it down to 0.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        currentNumberOfQuestions = 0;
    }

    /**
     * Prepare all materials needed to build this activity
     */
    private void initialize() {
        questionCounting = (TextView) findViewById(R.id.question_counting);
        countdownTimer = (TextView) findViewById(R.id.time_remaining);
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

        timer = new JLPTExamCountDownTimer(TOTAL_TIME_TO_FINISH_EXAM, INTERVAL);
        timer.start();
    }

    /**
     * This is the method sending fragment information to its associated activity
     * @param chosenOptionId what option did user choose?
     * @param optionContent what option's id did user choose?
     */
    @Override
    public void onOptionClick(int chosenOptionId, String optionContent) {
        this.chosenOptionId[currentNumberOfQuestions] = chosenOptionId;
        if (optionContent.equalsIgnoreCase(list.get(currentNumberOfQuestions).correctAnswer))
            correctOrNot[currentNumberOfQuestions] = true;
    }

    /**
     * We will get all questions needed here and shuffle their options
     */
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
        else alertUser();
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

    private int getPoints() {
        int points = 0;
        for (int i = 0; i < TOTAL_QUESTIONS; i++)
            if (correctOrNot[i])
                points++;
        return points;
    }

    private int getNumberOfAttemtps() {
        int attemtps = 0;
        for (int i = 0; i < TOTAL_QUESTIONS; i++)
            if (chosenOptionId[i] != 0)
                attemtps++;
        return attemtps;
    }

    private String getTotalTime() throws ParseException {
        String timeRemaining = countdownTimer.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Date date = simpleDateFormat.parse(timeRemaining);
        return simpleDateFormat.format(TOTAL_TIME_TO_FINISH_EXAM - date.getTime());
    }

    private void alertUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JLPTExam.this);
        builder.setTitle(getString(R.string.alert_exam_ending));
        builder.setMessage(getString(R.string.do_you_want_to_check_again));
        builder.setPositiveButton(getString(R.string.finish_anyway), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(JLPTExam.this, ExamResult.class);
                intent.putExtra(KEY_GET_POINTS, getPoints());
                intent.putExtra(KEY_GET_ATTEMPTS, getNumberOfAttemtps());
                timer.cancel();
                try {
                    intent.putExtra(KEY_GET_TIME_USED, getTotalTime());
                } catch (ParseException ex) {

                }
                intent.putExtra(KEY_GET_OPTIONS_1, options1);
                intent.putExtra(KEY_GET_OPTIONS_2, options2);
                intent.putExtra(KEY_GET_OPTIONS_3, options3);
                intent.putExtra(KEY_GET_OPTIONS_4, options4);
                intent.putExtra(KEY_GET_QUESTION_LIST, list);
                startActivity(intent);
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

    class JLPTExamCountDownTimer extends CountDownTimer {

        private SimpleDateFormat simpleDateFormat;
        /**
         * Constructor for countdown timer
         * @param millisInFuture the number of milliseconds from start() is called to onFinish() is called
         * @param countDownInterval how long to call onTick
         */
        public JLPTExamCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            simpleDateFormat = new SimpleDateFormat("mm:ss");
        }

        @Override
        public void onFinish() {
            countdownTimer.setText(getString(R.string.time_up));
            SystemClock.sleep(1000);
            Intent intent = new Intent(JLPTExam.this, ExamResult.class);
            intent.putExtra(KEY_GET_POINTS, getPoints());
            intent.putExtra(KEY_GET_ATTEMPTS, getNumberOfAttemtps());
            intent.putExtra(KEY_GET_TIME_USED, "20:00");
            intent.putExtra(KEY_GET_OPTIONS_1, options1);
            intent.putExtra(KEY_GET_OPTIONS_2, options2);
            intent.putExtra(KEY_GET_OPTIONS_3, options3);
            intent.putExtra(KEY_GET_OPTIONS_4, options4);
            intent.putExtra(KEY_GET_QUESTION_LIST, list);
            startActivity(intent);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countdownTimer.setText(simpleDateFormat.format(millisUntilFinished));
        }
    }


}