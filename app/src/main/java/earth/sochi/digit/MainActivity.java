package earth.sochi.digit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Chronometer chrono;
    boolean running;

    String[] numbersRange;  //{"0-10","11-20","21-30","31-40","41-50",}
                            //{"51-60","61-70","71-80","81-90","91-100",}
    String number;
    String[] menuList;//{"Обучение","Тренировка","Cтатистика","Выход"};
    String[] timerList;//{"No time","1сек","5сек","10сек","30сек"};
    int numb;
    int timerListChose =1,numbersRangeChose=0,
        menuListChose=0,pressedButton=1;//0-btNumbers 1-btHome 2-btTimer
    //Interface

    ListView listNumbers;
    TextView tvNumber,tvNumberInWords,tvStatistic;
    Button btOrder;
    ImageButton btRight,btLeft;


    Handler handler;
    Runnable runnable;
    Random random;
    NumberInWords numberInWords;
    SharedPreferences sharedPreferences;


    private int soundId=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numbersRange = getResources().getStringArray(R.array.numbersRange);//{"0-10","11-100","101-1000"};
        menuList = getResources().getStringArray(R.array.menuList);//{"Обучение","Тренировка","Cтатистика","Выход"};
        timerList = getResources().getStringArray(R.array.timerList);//{"No time","1сек","5сек","10сек","30сек"};
        btOrder = findViewById(R.id.btOrder);
        btRight = findViewById(R.id.btRight);
        btLeft = findViewById(R.id.btLeft);
        tvStatistic = findViewById(R.id.tvStatistic);


        listNumbers = findViewById(R.id.listNumbers);
        listNumbers.setSelector(R.drawable.selector);
        listNumbers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                controlPressed(view,position);
            }
        });
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        Filemanage filemanage = new Filemanage();
        number  = filemanage.readFromFile(this,1);
        if (number !="") {
             String [] arrString = number.split(":");
             NumberInWords.countMin = Integer.parseInt(arrString[0]);
             NumberInWords.countMax = Integer.parseInt(arrString[1]);
        } else {filemanage.writeToFile("0:10",this,1);}
        tvNumber = findViewById(R.id.number);
        tvNumberInWords = findViewById(R.id.numberInWords);
        chrono = findViewById(R.id.chrono);
        chrono.setFormat("Time: %s");
        chrono.setBase(SystemClock.elapsedRealtime());
        numberInWords =  new NumberInWords(getBaseContext());

        random = new Random();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                numb=NumberInWords.countMin+
                        random.nextInt(NumberInWords.countMax-NumberInWords.countMin);

                tvNumber.setText(String.valueOf(numb));
                //Completed number in Words
                tvNumberInWords.setText(numberInWords.convertRus(numb));
                if (!numberInWords.speakNumber(numb)){
                    Toast.makeText(getBaseContext(),"Can't load voice files",Toast.LENGTH_LONG).show();
                }
                handler.postDelayed(this,NumberInWords.timer);
            }
        };
        running = false;
    }
    @Override
    protected void onResume() {
        tvStatistic.setVisibility(View.INVISIBLE);
        super.onResume();
    }
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_BACK & tvStatistic.getVisibility() == View.VISIBLE)
        {
            tvStatistic.setVisibility(View.INVISIBLE);
            return false;
        } else if  (keyCode ==KeyEvent.KEYCODE_BACK & listNumbers.getVisibility() == View.VISIBLE)
        {
            listNumbers.setVisibility(View.INVISIBLE);
            return false;
        } else return super.onKeyDown(keyCode,event);
    }
    void startLeaning() {
        if (running) {
            running=false;
            btOrder.setBackgroundResource(R.drawable.start);
            handler.removeCallbacks(runnable);
            numberInWords.soundPool.stop(numberInWords.soundPoolMap.get(1));
            chrono.stop();
        } else {

            btOrder.setBackgroundResource(R.drawable.stop);
            //COMPLETED run task for learning
            handler.postDelayed(runnable,NumberInWords.timer); //COMPLETED delayMillis
            running =  true;
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    void controlPressed(View view,int position) {
        listNumbers.setVisibility(ListView.INVISIBLE);
        switch (pressedButton) {
            case 1: {menuListChose=position;
                switch (position) {
                    case 0:{Intent intent = new Intent(this,MainActivity.class);
                            startActivity(intent);
                            break;}
                    case 1:{
                        if (running) startLeaning();
                        Intent intent = new Intent(this,Traing.class);
                        startActivity(intent);
                        break;}
                    case 2:{
                        tvStatistic.setVisibility(View.VISIBLE);
                        Filemanage filemanage =  new Filemanage();
                        String s;
                        s = filemanage.readFromFile(this,2);
                        if (s.equals("")) s=Traing.STAT;
                        String [] statArray                 = s.split(":");
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
                        tvStatistic.setText(Html.fromHtml(s));
                        break;}
                    case 3:{
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        break;
                    }
                }
            };break;
            case 2: {
                timerListChose=position;
                if (position ==0) {
                    btRight.setVisibility(View.VISIBLE);
                    btLeft.setVisibility(View.VISIBLE);
                } else {
                    btRight.setVisibility(View.INVISIBLE);
                    btLeft.setVisibility(View.INVISIBLE);
                }
                switch (position) {
                    case 0:NumberInWords.timer=0;break;
                    case 1:NumberInWords.timer=2000;break;
                    case 2:NumberInWords.timer=5000;break;
                    case 3:NumberInWords.timer=10000;break;
                    case 4:NumberInWords.timer=30000;break;
                }break;
            }
        }
    }
    //pressedBotton 0-btNumbers 1-btHome 2-btTimer
    public void btNumbersClick (View view ){ //btNumbers
        pressedButton=0;
        Intent intent = new Intent(this,NumberActivity.class);
        startActivity(intent);
    }
    public void btHomeClick (View view) { //btHome
        pressedButton=1;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,menuList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
        listNumbers.requestFocusFromTouch();
        listNumbers.setItemChecked(1,true);
    }
    public void btTimerClick (View view) { //btTimer
        pressedButton=2;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,timerList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
        listNumbers.setSelection(1);
    }
    public void btOrderClick (View view) {
        if (NumberInWords.timer == 0 ) {
            btRightClick(view);
        } else
            startLeaning();
    }
    public void btRightClick (View view) {
        numb=NumberInWords.countMin+
                random.nextInt(NumberInWords.countMax-NumberInWords.countMin);
        tvNumber.setText(String.valueOf(numb));
        //Completed number in Words
        tvNumberInWords.setText(numberInWords.convertRus(numb));
        if (!numberInWords.speakNumber(numb)){
            Toast.makeText(getBaseContext(),"Can't load voice files",Toast.LENGTH_LONG).show();
        }
    }
    public void btLeftClick (View view) {
        tvNumber.setText(String.valueOf(numb));
        //Completed number in Words
        tvNumberInWords.setText(numberInWords.convertRus(numb));
        if (!numberInWords.speakNumber(numb)){
            Toast.makeText(getBaseContext(),"Can't load voice files",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
