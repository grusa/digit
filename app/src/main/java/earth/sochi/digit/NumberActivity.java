package earth.sochi.digit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NumberActivity extends AppCompatActivity {
    Button btNullTen,btTwenty,btThirty,btForty,btFifty,btSixty,
    btSeventy,btEighty,btNinety,btHundred,btOk;
    earth.sochi.digit.Filemanage filemanage;
    String numberString;
    public static  byte[] pressedButton= {0,0,0,0,0, 0,0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btNullTen = findViewById(R.id.nullTen);
        btTwenty = findViewById(R.id.twenty);
        btThirty = findViewById(R.id.thirty);
        btForty = findViewById(R.id.forty);
        btFifty = findViewById(R.id.fifty);
        btSixty = findViewById(R.id.sixty);
        btSeventy = findViewById(R.id.seventy);
        btEighty = findViewById(R.id.eighty);
        btNinety = findViewById(R.id.ninety);
        btHundred = findViewById(R.id.hundred);
        btOk=findViewById(R.id.btOk);

    }
    @Override
    public void onStart () {
        super.onStart();
        filemanage = new Filemanage();
        numberString = filemanage.readFromFile(this,1); // 1 - config.txt
        Log.v("FM",numberString);
        if (numberString !="") {
            String [] arrString = numberString.split(":");
            NumberInWords.countMin = Integer.parseInt(arrString[0]);
            NumberInWords.countMax = Integer.parseInt(arrString[1]);
        } else {filemanage.writeToFile("0:10",this,1);}
        fillButton();
    }
    @Override
    public void onStop() {
        super.onStop();
        filemanage.writeToFile(NumberInWords.countMin
                +":"+NumberInWords.countMax,this,1);
        Log.v("FM",NumberInWords.countMin
                +":"+NumberInWords.countMax);

    }
    private void setNull () {
        btNullTen.setBackgroundResource(R.drawable.table);
        btTwenty.setBackgroundResource(R.drawable.table);
        btThirty.setBackgroundResource(R.drawable.table);
        btForty.setBackgroundResource(R.drawable.table);
        btFifty.setBackgroundResource(R.drawable.table);
        btSixty.setBackgroundResource(R.drawable.table);
        btSeventy.setBackgroundResource(R.drawable.table);
        btEighty.setBackgroundResource(R.drawable.table);
        btNinety.setBackgroundResource(R.drawable.table);
        btHundred.setBackgroundResource(R.drawable.table);
    }
    private void fillButton () {
        //
        setNull();
        int start=100,end=100;
        for (int i=0;i<=9;i++) {
            if (pressedButton[i]==1 && start==100) start=i;
            if (start != 100 && pressedButton[i]==1) end=i;
        }
        for (int i = start+1;i<end;i++) {
            pressedButton[i] = 1;
        }
        for (int i=0;i<=9;i++) {
            if (pressedButton[i]==1) {
                switch (i) {
                    case 0:btNullTen.setBackgroundResource(R.drawable.tablepressed);break;
                    case 1:btTwenty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 2:btThirty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 3:btForty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 4:btFifty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 5:btSixty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 6:btSeventy.setBackgroundResource(R.drawable.tablepressed);break;
                    case 7:btEighty.setBackgroundResource(R.drawable.tablepressed);break;
                    case 8:btNinety.setBackgroundResource(R.drawable.tablepressed);break;
                    case 9:btHundred.setBackgroundResource(R.drawable.tablepressed);break;
                }
            }
        }
    }
    public void btNullTenClick(View view) {
       if (pressedButton[0] != 1) pressedButton[0] = 1; else pressedButton[0] = 0;
       fillButton();
    }
    public void btTwentyClick (View view) {
        if (pressedButton[1] != 1) pressedButton[1] = 1; else pressedButton[1] = 0;
        fillButton();
    }
    public void btThirtyClick (View view) {
        if (pressedButton[2] != 1) pressedButton[2] = 1; else pressedButton[2] = 0;
        fillButton();
    }
    public void btFortyClick (View view) {
        if (pressedButton[3] != 1) pressedButton[3] = 1; else pressedButton[3] = 0;
        fillButton();
    }
    public void btFiftyClick (View view) {
        if (pressedButton[4] != 1) pressedButton[4] = 1; else pressedButton[4] = 0;
        fillButton();
    }
    public void btSixtyClick (View view) {
        if (pressedButton[5] != 1) pressedButton[5] = 1; else pressedButton[5] = 0;
        fillButton();
    }
    public void btSeventyClick (View view) {
        if (pressedButton[6] != 1) pressedButton[6] = 1; else pressedButton[6] = 0;
        fillButton();
    }
    public void btEightyClick (View view) {
        if (pressedButton[7] != 1) pressedButton[7] = 1; else pressedButton[7] = 0;
        fillButton();
    }
    public void btNinetyClick (View view) {
        if (pressedButton[8] != 1) pressedButton[8] = 1; else pressedButton[8] = 0;
        fillButton();
    }
    public void btHundredClick (View view) {
        if (pressedButton[9] != 1) pressedButton[9] = 1; else pressedButton[9] = 0;
        fillButton();
    }
    public void btOkPressed(View view) {
        boolean min = true;
        for (int i =0;i<=9;i++) {
            if (pressedButton[i] ==1) {
                if (min) {
                    NumberInWords.countMin = i*10+1;
                    min = false;
                }
                NumberInWords.countMax = (i+1)*10;
                onBackPressed();
            }
        }
    }

}
