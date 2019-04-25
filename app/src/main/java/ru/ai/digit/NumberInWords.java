package ru.ai.digit;

import java.text.DecimalFormat;

public class NumberInWords {
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
        else {return numNamesRus[number] + "сот" + soFar;}}} //TODO сто двести ста/сот
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
}
