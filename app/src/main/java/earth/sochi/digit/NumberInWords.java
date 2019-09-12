package earth.sochi.digit;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class NumberInWords {
        static int countMin = 0;
        static int countMax=101;
        static int timer = 5000;
        SoundPool soundPool;
        HashMap<Integer,Integer> soundPoolMap;
        int maxStreams=5;
        public  NumberInWords(Context context) {
            try {
                soundPoolMap = new HashMap<Integer,Integer>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    soundPool = new SoundPool.Builder()
                            .setMaxStreams(maxStreams)
                            .build();
                } else {
                    soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
                }

                //soundPoolBilder = new SoundPool.Builder();
                //soundPoolBilder.setMaxStreams(5);
                //soundPool = soundPoolBilder.build();
                soundPoolMap.put(0,soundPool.load(context, R.raw.zero, 5));
                soundPoolMap.put(1,soundPool.load(context, R.raw.one, 5));
                soundPoolMap.put(2,soundPool.load(context, R.raw.two, 5));
                soundPoolMap.put(3,soundPool.load(context, R.raw.three, 5));
                soundPoolMap.put(4,soundPool.load(context, R.raw.four, 5));
                soundPoolMap.put(5,soundPool.load(context, R.raw.five, 5));
                soundPoolMap.put(6,soundPool.load(context, R.raw.six, 5));
                soundPoolMap.put(7,soundPool.load(context, R.raw.seven, 5));
                soundPoolMap.put(8,soundPool.load(context, R.raw.eight, 5));
                soundPoolMap.put(9,soundPool.load(context, R.raw.nine, 5));
                soundPoolMap.put(10,soundPool.load(context, R.raw.ten, 5));
                soundPoolMap.put(11,soundPool.load(context, R.raw.eleven, 5));
                soundPoolMap.put(12,soundPool.load(context, R.raw.twelve, 5));
                soundPoolMap.put(13,soundPool.load(context, R.raw.thirteen, 5));
                soundPoolMap.put(14,soundPool.load(context, R.raw.fourteen, 5));
                soundPoolMap.put(15,soundPool.load(context, R.raw.fifteen, 5));
                soundPoolMap.put(16,soundPool.load(context, R.raw.sixteen, 5));
                soundPoolMap.put(17,soundPool.load(context, R.raw.seventeen, 5));
                soundPoolMap.put(18,soundPool.load(context, R.raw.eighteen, 5));
                soundPoolMap.put(19,soundPool.load(context, R.raw.nineteen, 5));
                soundPoolMap.put(20,soundPool.load(context, R.raw.twenty, 5));
                soundPoolMap.put(30,soundPool.load(context, R.raw.thirty, 5));
                soundPoolMap.put(40,soundPool.load(context, R.raw.forty, 5));
                soundPoolMap.put(50,soundPool.load(context, R.raw.fifty, 5));
                soundPoolMap.put(60,soundPool.load(context, R.raw.sixty, 5));
                soundPoolMap.put(70,soundPool.load(context, R.raw.seventy, 5));
                soundPoolMap.put(80,soundPool.load(context, R.raw.eighty, 5));
                soundPoolMap.put(90,soundPool.load(context, R.raw.ninety, 5));
                soundPoolMap.put(100,soundPool.load(context, R.raw.hundred, 5));
            } catch ( Exception e) {
                Toast.makeText(context,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        private static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty",
                    " fifty", " sixty", " seventy", " eighty", " ninety" };
        private static final String[] tensNamesRus = { "", " десять", " двадцать", " тридцать", " сорок",
            " пятьдесят", " шестьдесят", " семьдесят", " восемьдесят", " девяносто" };

        private static final String[] numNames = { "", " one", " two", " three", " four", " five",
                " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen",
                " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen" };
        private static final String[] numNamesRus = { "", " один", " два", " три", " четыре", " пять",
            " шесть", " семь", " восемь", " девять", " десять", " одинадцать", " двенадцать", " тринадцать",
            " четырнадцать", " пятьнадцать", " шестьнадцать", " семьнадцать", " восемьнадцать", " девятьнадцать" };
        private static String convertXXX(int number)
        {
            String soFar;
            if (number % 100 < 20)
            {
                soFar = numNames[number % 100];
                number /= 100;
            } else
            {
                soFar = numNames[number % 10];
                number /= 10;

                soFar = tensNames[number % 10] + soFar;
                number /= 10;
            }
            if (number == 0)
                return soFar;
            return numNames[number] + " hundred" + soFar;
        }

    public static String[] playXXX(int number) //ToDo delete
    {
        String [] playArray=new String[1];
        playArray[0] = "1";

        return playArray;
    }

    private static String convertXXXRus(int number) {
        String soFar;
        if (number % 100 < 20)
        {
            soFar = numNamesRus[number % 100];
            number /= 100;
        } else
        {
            soFar = numNamesRus[number % 10];
            number /= 10;

            soFar = tensNamesRus[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0)
            return soFar;
        if (number<2) {return " сто" + soFar;}
        else {if (number<3) {return " двести"+soFar;}
        else {if (number <5) {return numNamesRus[number]+"ста"+soFar;}
        else {return numNamesRus[number] + "сот" + soFar;}}} //Completed сто двести ста/сот
    }
    public static String convert(long number)
    {
        // 0 to 999 999 999 999
        if (number == 0)
        {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions)
        {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertXXX(billions) + " billion ";
                break;
            default:
                tradBillions = convertXXX(billions) + " billion ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions)
        {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertXXX(millions) + " million ";
                break;
            default:
                tradMillions = convertXXX(millions) + " million ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands)
        {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "one thousand ";
                break;
            default:
                tradHundredThousands = convertXXX(hundredThousands) + " thousand ";
        }
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertXXX(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
    public static String convertRus(long number)
    {
        // 0 to 999 999 999 999
        if (number == 0)
        {
            return "Ноль";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions)
        {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertXXXRus(billions) + " миллиард ";
                break;
            default:
                tradBillions = convertXXXRus(billions) + " миллиард ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions)
        {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertXXX(millions) + " миллион ";
                break;
            default:
                tradMillions = convertXXX(millions) + " миллион ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands)
        {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "одна тысяча ";
                break;
            default:
                tradHundredThousands = convertXXX(hundredThousands) + " тысяч ";
        }
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertXXXRus(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
    public boolean speakNumber (int numb) {
        //Completed add little delay
        try {
            Thread.sleep(500);
            if (numb<21) {
                this.soundPool.play(this.soundPoolMap.get(numb), 1, 1, 5, 0, 1f);
            } else if (numb<30 ) {
                soundPool.play(soundPoolMap.get(20), 1, 1, 1, 0, 1f);
                //Completed step by step
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-20), 1, 1, 3, 0, 1f);
            } else if (numb <40) {
                soundPool.play(soundPoolMap.get(30), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-30), 1, 1, 3, 0, 1f);
            } else if (numb <50) {
                soundPool.play(soundPoolMap.get(40), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-40), 1, 1, 3, 0, 1f);
            } else if (numb <60) {
                soundPool.play(soundPoolMap.get(50), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-50), 1, 1, 3, 0, 1f);
            } else if (numb<70) {
                soundPool.play(soundPoolMap.get(60), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-60), 1, 1, 3, 0, 1f);
            } else if (numb < 80) {
                soundPool.play(soundPoolMap.get(70), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-70), 1, 1, 3, 0, 1f);
            } else if (numb <90 ) {
                soundPool.play(soundPoolMap.get(80), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-80), 1, 1, 3, 0, 1f);
            } else if (numb <100) {
                soundPool.play(soundPoolMap.get(90), 1, 1, 1, 0, 1f);
                Thread.sleep(900);
                soundPool.play(soundPoolMap.get(numb-90), 1, 1, 3, 0, 1f);
            } else if (numb==100) {
                soundPool.play(soundPoolMap.get(100), 1, 1, 1, 0, 1f);
            }
        } catch (Exception e) {
            Log.d("VOICE",e.toString());
            return false;
        }
        return true;
    }
}
