package com.example.dansdistractor;/**
 * Created by wongchihaul on 2021/9/24
 */

import java.util.ArrayList;

/**
 * @ClassName: Fitness
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 10:24 下午
 */
public class Fitness {
    //icons of fitness summary
    private int fitnessIcon;
    //categories of fitness summary, displayed next to icons
    private String fitnessCategory;
    //the volume of fitness, e.g. 34 KM, 19342 steps
    private long fitnessNumber;


    public Fitness(int _fitnessIcon, String _fitnessCategory, long _fitnessNumber) {
        fitnessIcon = _fitnessIcon;
        fitnessCategory = _fitnessCategory;
        fitnessNumber = _fitnessNumber;
    }

    public static ArrayList<Fitness> getFitness() {
        ArrayList<Fitness> fitnessList = new ArrayList<>();
        fitnessList.add(new Fitness(R.drawable.running, "STEPS", 0));
        fitnessList.add(new Fitness(R.drawable.speed, "SPEED", 0));
        fitnessList.add(new Fitness(R.drawable.distance, "DISTANCE", 0));
        return fitnessList;
    }

    public int getFitnessIcon() {
        return fitnessIcon;
    }

    public void setFitnessIcon(int fitnessIcon) {
        this.fitnessIcon = fitnessIcon;
    }

    public String getFitnessCategory() {
        return fitnessCategory;
    }

    public void setFitnessCategory(String fitnessCategory) {
        this.fitnessCategory = fitnessCategory;
    }

    public long getFitnessNumber() {
        return fitnessNumber;
    }

    public void setFitnessNumber(long fitnessNumber) {
        this.fitnessNumber = fitnessNumber;
    }
}
