package com.midterm.microproject;

import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuessingNumberGame {

    private final int totalTimes;
    private int remainingTimes = 0;
    private final int goalNum;
    List<Integer> numberPool = new ArrayList<Integer>(9);


    public GuessingNumberGame(int times){
        if(times>0) {
            totalTimes = times;
            remainingTimes = times;
            goalNum =  InitNumber();
            System.out.println("goal number is "+ goalNum);
        }else
            throw new IllegalArgumentException("invalid input number!!");
    }

    int InitNumber(){
        for (int i=1;i<10;i++) numberPool.add(i);
        int result =0;
        for(int i=0;i<4;i++){
            Random tRan = new Random();
            int tNum = tRan.nextInt(numberPool.size()-i);
            result +=  numberPool.get(tNum)*Math.pow(10,i);
            numberPool.remove(numberPool.get(tNum));
        }
        return result;
    }

    public String GuessNumber(String pGuessNum){
        if(remainingTimes>0) {
            remainingTimes--;
            String tGoalNum = String.valueOf(goalNum);
            //check A   means correct num in correct position
            int countA = 0;
            //check B   means correct num in wrong position
            int countB = 0;
            for (int i = 0; i < 4; i++) {
                String tStr = pGuessNum.substring(i, i + 1);
                String tRealStr = tGoalNum.substring(i, i + 1);
                if (tRealStr.equals(tStr)) {
                    countA++;
                } else {
                    if (tGoalNum.contains(tStr) && tGoalNum.indexOf(tStr) != i ) {
                        if(!pGuessNum.substring(tGoalNum.indexOf(tStr),tGoalNum.indexOf(tStr)+1).equals(tStr) ) {
                            Log.i("Goal num ", " goal num " +i+"  "+ tGoalNum.indexOf(tStr));
                            countB++;
                        }
                    }
                }
            }

            if (countA == 4) return "1" ;
            return pGuessNum+" \t\t  " +  countA + " A " + countB + " B ";
        }else
            return "-1";
    }

    public int GetRemainingTimes(){
        return remainingTimes;
    }


}
