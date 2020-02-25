package com.midterm.microproject;


import java.util.Random;


public class GuessingGame {

    private final int totalTimes;
    private int remainingTimes = 0;
    private int minBounce= 0;
    private int maxBounce= 100;
    private final int goalNum;

    public GuessingGame(int times){
        if(times>0) {
            totalTimes = times;
            remainingTimes = totalTimes;
            Random tRan = new Random();
            goalNum = tRan.nextInt(maxBounce+1);
            System.out.println("goal number is "+ goalNum);
        }else
            throw new IllegalArgumentException("invalid input number!!");
    }

    public byte GuessNum(int num){
        byte result = 0;
        if(remainingTimes >0){
            remainingTimes--;
            if(num > goalNum)  result = 1;
            else if(num<goalNum) result =-1;
            else result =0;
        }
        return result;
    }
    public int GetRemainingTimes(){
        return remainingTimes;
    }
    public int GetAnswer(){
        return goalNum;
    }

}
