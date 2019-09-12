package earth.sochi.digit;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;

public class ListSettings {
    ListView listNumbers;
    String[] numbersRange;//{"0-10","11-100","101-1000"};
    String[] menuList;//{"Обучение","Тренировка","Cтатистика","Выход"};
    String[] timerList;//{"1сек","5сек","10сек","30сек"};
    int numb;
    static int timer=5000,timerListChose =1,numbersRangeChose=0,
            menuListChose=0,pressedButton=1,//0-btNumbers 1-btHome 2-btTimer
            countMax = 100;

    }

