package ru.ai.digit;

import android.content.Intent;
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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Chronometer chrono;
    boolean running;
    ListView listNumbers;
    String[] numbersRange;//{"0-10","11-100","101-1000"};
    String[] menuList;//{"Обучение","Тренировка","Cтатистика","Выход"};
    String[] timerList;//{"1сек","5сек","10сек","30сек"};
    int numb;
    int timer=5000,timerListChose =1,numbersRangeChose=0,
        menuListChose=0,pressedButton=1;//0-btNumbers 1-btHome 2-btTimer
    TextView tvNumber,tvNumberInWords;
    Button btOrder;
    int countMax=10;
    Handler handler;
    Runnable runnable;
    Random random;
    NumberInWords numberInWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numbersRange = getResources().getStringArray(R.array.numbersRange);//{"0-10","11-100","101-1000"};
        menuList = getResources().getStringArray(R.array.menuList);//{"Обучение","Тренировка","Cтатистика","Выход"};
        timerList = getResources().getStringArray(R.array.timerList);//{"1сек","5сек","10сек","30сек"};
        btOrder = findViewById(R.id.btOrder);
        listNumbers = findViewById(R.id.listNumbers);
        listNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controlPressed(view,position);
            }
        });
        tvNumber = findViewById(R.id.number);
        tvNumberInWords = findViewById(R.id.numberInWords);
        chrono = findViewById(R.id.chrono);
        chrono.setFormat("Time: %s");
        chrono.setBase(SystemClock.elapsedRealtime());
        numberInWords = new NumberInWords();
        random = new Random();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                numb=random.nextInt(countMax);
                tvNumber.setText(String.valueOf(numb));
                //TODO number in Words
                tvNumberInWords.setText(numberInWords.convertRus(numb));

                handler.postDelayed(this,timer);
            }
        };
        running = false;
    }
    void startLeaning() {
        if (running) {
            Toast.makeText(this,"True",Toast.LENGTH_LONG).show();
            running=false;
            btOrder.setText(getResources().getString(R.string.order));
            handler.removeCallbacks(runnable);
            chrono.stop();
        } else {
            Toast.makeText(this,"False",Toast.LENGTH_LONG).show();
            btOrder.setText(getResources().getString(R.string.stop));
            //COMPLETED run task for learning
            handler.postDelayed(runnable,timer); //TODO delayMillis
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
            case 0: {numbersRangeChose=position;
                switch (position) {
                    case 0:countMax=10;break;
                    case 1:countMax=100;break;
                    case 2:countMax=1000;break;
                }break;} //
            case 1: menuListChose=position;break;
            case 2: {
                timerListChose=position;
                switch (position) {
                    case 0:timer=1000;break;
                    case 1:timer=5000;break;
                    case 2:timer=10000;break;
                    case 3:timer=30000;break;
                }break;
            }
        }
        Snackbar.make(view, "countMax"+countMax,Snackbar.LENGTH_LONG).show();
    }
    //pressedBotton 0-btNumbers 1-btHome 2-btTimer
    public void btNumbersClick (View view ){ //btNumbers
        pressedButton=0;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>
                (this,android.R.layout.simple_expandable_list_item_1,numbersRange);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
    }
    public void btHomeClick (View view) { //btHome
        pressedButton=1;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,menuList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
    }
    public void btTimerClick (View view) { //btTimer
        pressedButton=2;
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,timerList);
        listNumbers.setAdapter(stringArrayAdapter);
        listNumbers.setVisibility(ListView.VISIBLE);
    }
    public void btOrderClick (View view) {
        startLeaning();
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
