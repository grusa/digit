package earth.sochi.digit;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Filemanage {
    private String fileNameNumb="numbers.txt";//i=1
    private String fileNameStat="stat.txt";//i=2
    protected void writeToFile(String data, Context context,int i) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(getFileName(i), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    protected String readFromFile(Context context,int i) {

        String ret = "";

        try {

            InputStream inputStream = context.openFileInput(getFileName(i));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    private String getFileName(int i) {
        if (i==1) return fileNameNumb;
        else return fileNameStat;

    }
    public static String getStatLine (String count,String rightCount) {
        if (Integer.parseInt(count) != 0 )
        {
            float  f = Float.parseFloat(rightCount)/Float.parseFloat(count)*100;
            return count+" | "+ String.format("%.0f",f);
        }
        else return count+" | 0";
    }
}

