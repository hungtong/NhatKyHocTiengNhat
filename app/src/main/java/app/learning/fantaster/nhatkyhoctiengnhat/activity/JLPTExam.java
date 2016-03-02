package app.learning.fantaster.nhatkyhoctiengnhat.activity;

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
import android.view.Menu;
import android.view.MenuItem;
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

import app.learning.fantaster.nhatkyhoctiengnhat.R;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Answer;
import app.learning.fantaster.nhatkyhoctiengnhat.data.Question;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.QuestionDAO;
import app.learning.fantaster.nhatkyhoctiengnhat.database.question.QuestionDatabaseHelper;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.OptionInLandscapeFragment;
import app.learning.fantaster.nhatkyhoctiengnhat.fragment.OptionInPortraitFragment;

/**
 * - This is our exam
 * - The Activity will first get all questions in 3 categories : 5 Grammar, 15 Vocab and 5 Reading. Initially
 * A is always the correct answer so it is crucial to shuffleOptions
 * - Every time clicking in an option, it will be highlighted and its id will be stored so that it is easy to
 * retrieve when moving backward or forward when all the buttons have to become default state.
 * - To move backward or forward we only need to adjust current number of questions and display data which were
 * initialized at the beginning.
 */
public class JLPTExam extends AppCompatActivity implements OptionInLandscapeFragment.OptionClickListener, OptionInPortraitFragment.OptionClickListener {

    private TextView questionCounting;
    private TextView countdownTimer;

    public static String KEY_GET_ANSWERS_LIST = "key to get answers list";
    public static String KEY_GET_TIME_USED = "key to get time used";
    public static String KEY_GET_ATTEMPTS = "key to get attempts";
    public static String KEY_GET_QUESTION_LIST = "key to get question list";
    public static String KEY_GET_POINTS = "key to get points";
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
    public static long TOTAL_TIME_TO_FINISH_EXAM = 1200000;
    public static long INTERVAL = 1000;

    public static int currentNumberOfQuestions = 0;

    private ArrayList<Question> listQuestion;
    private ArrayList<Answer> listAnswer;
    private String[] options1, options2, options3, options4;
    private int[] chosenOptionId;
    private boolean[] correctOrNot;
    private JLPTExamCountDownTimer timer;

    public String getQuestionAt(int whichQuestions) {
        return listQuestion.get(whichQuestions).question;
    }

    public int getQuestionTypeAt(int whichQuestions) {
        return listQuestion.get(whichQuestions).questionType;
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
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentTransaction.replace(R.id.question_container, new OptionInLandscapeFragment());
        else fragmentTransaction.replace(R.id.question_container, new OptionInPortraitFragment());
        fragmentTransaction.commit();
    }

    /**
     * Prepare all materials needed to build this activity
     */
    private void initialize() {
        currentNumberOfQuestions = 0;

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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentTransaction.replace(R.id.question_container, new OptionInLandscapeFragment());
        else fragmentTransaction.replace(R.id.question_container, new OptionInPortraitFragment());

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
        Answer answer = listAnswer.get(currentNumberOfQuestions);
        answer.answer = optionContent;
        answer.attemptedOrNot = 1;
        this.chosenOptionId[currentNumberOfQuestions] = chosenOptionId;
        if (optionContent.equalsIgnoreCase(listQuestion.get(currentNumberOfQuestions).correctAnswer)) {
            correctOrNot[currentNumberOfQuestions] = true;
            answer.correctOrNot = 1;
        }

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

        QuestionDAO dao = new QuestionDAO(databaseHelper);
        listQuestion = dao.getRandomTypeQuestions(NUMBER_OF_VOCAB_QUESTIONS, VOCAB_TYPE);
        listQuestion.addAll(dao.getRandomTypeQuestions(NUMBER_OF_GRAMMAR_QUESTIONS, GRAMMAR_TYPE));
        listQuestion.addAll(dao.getRandomTypeQuestions(NUMBER_OF_READING_QUESTIONS, READING_TYPE));

        listAnswer = new ArrayList<>();
        options1 = new String[TOTAL_QUESTIONS];
        options2 = new String[TOTAL_QUESTIONS];
        options3 = new String[TOTAL_QUESTIONS];
        options4 = new String[TOTAL_QUESTIONS];

        chosenOptionId = new int[TOTAL_QUESTIONS];
        correctOrNot = new boolean[TOTAL_QUESTIONS];

        Question newQuestion;
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            newQuestion = listQuestion.get(i);
            shuffleOptions(newQuestion, i);
            listAnswer.add(new Answer(newQuestion.question, newQuestion.correctAnswer));
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
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                fragmentTransaction.replace(R.id.question_container, new OptionInLandscapeFragment());
            else fragmentTransaction.replace(R.id.question_container, new OptionInPortraitFragment());
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
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                fragmentTransaction.replace(R.id.question_container, new OptionInLandscapeFragment());
            else fragmentTransaction.replace(R.id.question_container, new OptionInPortraitFragment());
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
                Intent intent = new Intent(JLPTExam.this, app.learning.fantaster.nhatkyhoctiengnhat.activity.ExamResult.class);
                intent.putExtra(KEY_GET_POINTS, getPoints());
                intent.putExtra(KEY_GET_ATTEMPTS, getNumberOfAttemtps());
                timer.cancel();
                try {
                    intent.putExtra(KEY_GET_TIME_USED, getTotalTime());
                } catch (ParseException ex) {
                    Log.d("ParseException", ex.toString() );
                }
                intent.putParcelableArrayListExtra(KEY_GET_ANSWERS_LIST, listAnswer);
                intent.putParcelableArrayListExtra(KEY_GET_QUESTION_LIST, listQuestion);
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
            Intent intent = new Intent(JLPTExam.this, app.learning.fantaster.nhatkyhoctiengnhat.activity.ExamResult.class);
            intent.putExtra(KEY_GET_POINTS, getPoints());
            intent.putExtra(KEY_GET_ATTEMPTS, getNumberOfAttemtps());
            intent.putExtra(KEY_GET_TIME_USED, "20:00");
            intent.putParcelableArrayListExtra(KEY_GET_ANSWERS_LIST, listAnswer);
            intent.putParcelableArrayListExtra(KEY_GET_QUESTION_LIST, listQuestion);
            startActivity(intent);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countdownTimer.setText(simpleDateFormat.format(millisUntilFinished));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}