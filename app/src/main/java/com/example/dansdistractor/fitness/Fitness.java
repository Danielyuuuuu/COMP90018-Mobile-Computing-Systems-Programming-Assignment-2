package com.example.dansdistractor.fitness;

import com.example.dansdistractor.R;

import java.util.ArrayList;

/**
 * @ClassName: Fitness
 * @Description: //TODO
 * @Author: wongchihaul
 * @CreateDate: 2021/9/24 10:24 下午
 */
public class Fitness {
    //icons of fitness summary
    private int icon;
    //categories of fitness summary, displayed next to icons
    private String category;
    //the volume of fitness, e.g. 34 KM, 19342 steps
    private float number;
    //

    public Fitness() {

    }

    public Fitness(int _fitnessIcon, String _fitnessCategory, float _fitnessNumber) {
        icon = _fitnessIcon;
        category = _fitnessCategory;
        number = _fitnessNumber;
    }


    public static ArrayList<Fitness> getFitness() {
        ArrayList<Fitness> fitnessList = new ArrayList<>();
        fitnessList.add(new Fitness(R.drawable.running, "STEPS", 0));
        fitnessList.add(new Fitness(R.drawable.speed, "SPEED", 0));
        fitnessList.add(new Fitness(R.drawable.distance, "DISTANCE", 0));
        return fitnessList;
    }

    public int getIcon() {
        return icon;
    }

    public String getCategory() {
        return category;
    }

    public float getNumber() {
        return number;
    }

    public static final class FitnessBuilder {
        private Fitness fitness;

        private FitnessBuilder() {
            fitness = new Fitness();
        }


        public FitnessBuilder withIcon(int icon) {
            fitness.icon = icon;
            return this;
        }

        public FitnessBuilder withCategory(String category) {
            fitness.category = category;
            return this;
        }

        public FitnessBuilder withNumber(long number) {
            fitness.number = number;
            return this;
        }

        public Fitness build() {
            return fitness;
        }
    }
}