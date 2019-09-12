package earth.sochi.digit;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Traing extends AppCompatActivity {
    static final String STAT = "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0";
    //Statistic writing  into array with pair
    //1 pair is 0-10 count all and count right. etc.
    final String TAG = "TR";
    String [] statArray;
    ListView listNumbers;
    String[] numbersRange;//{"0-10","11-100","101-1000"};
    String[] menuList;//{"Обучение","Тренировка","Cтатистика","Выход"};
    String[] timerList;//{"1сек","5сек","10сек","30сек"};
    Random random;
    TextView numberTV;
    NumberInWords numberInWords;
    Handler handler;
    Runnable runnable;
    ImageView correct,wrong;
    TextView wrongNumber,statTW;
    Button btForvard,btLeft;
    int numb;
    int timerListChose =1,numbersRangeChose=0,
            menuListChose=0,pressedButton=1;//0-btNumbers 1-btHome 2-btTimer
    int [] stat;
    Filemanage filemanage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        numbersRange = getResources().getStringArray(R.array.numbersRange);//{"0-10","11-100","101-1000"};
        menuList = getResources().getStringArray(R.array.menuList);//{"Обучение","Тренировка","Cтатистика","Выход"};
        timerList = getResources().getStringArray(R.array.timerList);//{"1сек","5сек","10сек","30сек"};
        numberTV = findViewById(R.id.number);
        statTW = findViewById(R.id.statTW);

        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        btForvard = findViewById(R.id.forward);
        btForvard.setText("START");
        btLeft = findViewById(R.id.left);
        wrongNumber = findViewById(R.id.wrongNumber);
        listNumbers = findViewById(R.id.listNumbers);
        listNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controlPressed(view,position);
            }
        });
        random = new Random();

        numberInWords = new NumberInWords(this);
        setSupportActionBar(toolbar);

    }
    @Override
    protected void onResume() {
        //read statistic
        statTW.setVisibility(View.INVISIBLE);
        stat = new int [10];
        filemanage = new Filemanage();
        String statString = filemanage.readFromFile(this,2);
        if (statString == "")
        {
            filemanage.writeToFile(STAT,this,2);
            statString = STAT;
        }
        statArray = statString.split(":");
        super.onResume();
    }
    @Override
    protected  void onPause () {
        String s="";
        for (int i=0; i<19;i++) {s=s+statArray[i]+":";}
        s=s+statArray[19];
        filemanage.writeToFile(s,this,2);
        super.onPause();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK & statTW.getVisibility()==View.VISIBLE)
        {
            statTW.setVisibility(View.INVISIBLE);
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK & listNumbers.getVisibility()==View.VISIBLE)
        {
            listNumbers.setVisibility(View.INVISIBLE);
            return false;
        } else return super.onKeyDown(keyCode,event);
    }
    public void startTraining(View view,boolean repeat) {
        //Clean
        btForvard.setText("FORWARD");
        btLeft.setText("REPEAT");
        correct.setVisibility(View.INVISIBLE);
        wrong.setVisibility(View.INVISIBLE);
        wrongNumber.setVisibility(View.INVISIBLE);
        //Fill
        if (numberTV.getText()!="") {
            statWriteLine(numb,Integer.toString(numb).equals(numberTV.getText()));
            if (Integer.toString(numb).equals(numberTV.getText())) {
                correct.setVisibility(View.VISIBLE);

            } else {
                wrong.setVisibility(View.VISIBLE);
                wrongNumber.setText(Integer.toString(numb));
                wrongNumber.setVisibility(View.VISIBLE);
            }
            try {Thread.sleep(numberInWords.timer);}
            catch ( Exception e ) {Snackbar.make(view,e.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();}
        }
        try {
            Thread.sleep(500);
        } catch ( Exception e ) {
            Snackbar.make(view,e.getLocalizedMessage(),Snackbar.LENGTH_LONG).show();
        }
        //Next number

        if (!repeat) numb=NumberInWords.countMin+
                random.nextInt(NumberInWords.countMax-NumberInWords.countMin);

        numberTV.setText("");
        //Completed number in Words
        try {
            if (numb<21) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb), 1, 1, 5, 0, 1f);
            } else if (numb<30 ) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(20), 1, 1, 1, 0, 1f);
                //Completed step by step
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-20), 1, 1, 3, 0, 1f);
            } else if (numb <40) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(30), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-30), 1, 1, 3, 0, 1f);
            } else if (numb <50) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(40), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-40), 1, 1, 3, 0, 1f);
            } else if (numb <60) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(50), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-50), 1, 1, 3, 0, 1f);
            } else if (numb<70) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(60), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-60), 1, 1, 3, 0, 1f);
            } else if (numb < 80) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(70), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-70), 1, 1, 3, 0, 1f);
            } else if (numb <90 ) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(80), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-80), 1, 1, 3, 0, 1f);
            } else if (numb <100) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(90), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(numb-90), 1, 1, 3, 0, 1f);
            } else if (numb==100) {
                numberInWords.soundPool.play(numberInWords.soundPoolMap.get(100), 1, 1, 1, 0, 1f);
            }
        } catch (Exception e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    void controlPressed(View view,int position) {
        listNumbers.setVisibility(ListView.INVISIBLE);
        switch (pressedButton) {
            case 0: {
                break;
            } //
            case 1: {
                menuListChose = position;
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        break;
                    }
                    case 2:
                        statTW.setVisibility(View.VISIBLE);
                        String s;
                        s = "<h1>Статистика</h1>";
                        s = s+"<h2>0-10 | "+Filemanage.getStatLine(statArray[0],statArray[1])+"% | </h2>";
                        s = s+"<h2>11-20 | "+Filemanage.getStatLine(statArray[2],statArray[3])+"% | </h2>";
                        s = s+"<h2>21-30 | "+Filemanage.getStatLine(statArray[4],statArray[5])+"% | </h2>";
                        s = s+"<h2>31-40 | "+Filemanage.getStatLine(statArray[6],statArray[7])+"% | </h2>";
                        s = s+"<h2>41-50 | "+Filemanage.getStatLine(statArray[8],statArray[9])+"% | </h2>";
                        s = s+"<h2>51-60 | "+Filemanage.getStatLine(statArray[10],statArray[11])+"% | </h2>";
                        s = s+"<h2>61-70 | "+Filemanage.getStatLine(statArray[12],statArray[13])+"% | </h2>";
                        s = s+"<h2>71-80 | "+Filemanage.getStatLine(statArray[14],statArray[15])+"% | </h2>";
                        s = s+"<h2>81-90 | "+Filemanage.getStatLine(statArray[16],statArray[17])+"% | </h2>";
                        s = s+"<h2>91-100 | "+Filemanage.getStatLine(statArray[18],statArray[19])+"% | </h2>";
                        statTW.setText(Html.fromHtml(s));
                        break;
                    case 3:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        break;
                }
            };
            break;
            case 2: {
                timerListChose = position;
                switch (position) {
                    case 0:
                        NumberInWords.timer = 1000;
                        break;
                    case 1:
                        NumberInWords.timer = 5000;
                        break;
                    case 2:
                        NumberInWords.timer = 10000;
                        break;
                    case 3:
                        NumberInWords.timer = 30000;
                        break;
                }
                break;
            }
        }
    }
    public void oneClick (View view) {
        numberTV.setText(numberTV.getText()+"1");
    }
    public void twoClick (View view) {
        numberTV.setText(numberTV.getText()+"2");
    }
    public void treeClick (View view) {
        numberTV.setText(numberTV.getText()+"3");
    }
    public void fourClick (View view) {
        numberTV.setText(numberTV.getText()+"4");
    }
    public void fiveClick (View view) {
        numberTV.setText(numberTV.getText()+"5");
    }
    public void sixClick (View view) {
        numberTV.setText(numberTV.getText()+"6");
    }
    public void sevenClick (View view) {
        numberTV.setText(numberTV.getText()+"7");
    }
    public void eightClick (View view) {
        numberTV.setText(numberTV.getText()+"8");
    }
    public void nineClick (View view) {
        numberTV.setText(numberTV.getText()+"9");
    }
    public void zeroClick (View view) {
        numberTV.setText(numberTV.getText()+"0");
    }
    public void btHomeClick(View view) {
        pressedButton = 1;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, menuList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
    }
    public void btNumbersClick (View view ){ //btNumbers
        Intent intent = new Intent(this,NumberActivity.class);
        startActivity(intent);
    }
    public void btForwardClick(View view) {
        startTraining(view,false);
    }
    public void btBackClick(View view) {
        startTraining(view,true);
    }
    public void btTimerClick (View view) { //btTimer
        pressedButton=2;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,timerList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
    }
    void statWriteLine(int num , boolean correct) {
        //TODO num have to convert to
        int i = (num-1)/10;
        statArray[i*2] = Integer.toString( Integer.parseInt(statArray[2*i])+1);
        if (correct) statArray[2*i+1] = Integer.toString(Integer.parseInt(statArray[2*i+1])+1);
        Log.v(TAG,"num = "+num+"| i="+i);
    }
}
