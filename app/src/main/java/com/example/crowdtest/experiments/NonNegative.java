package com.example.crowdtest.experiments;

import android.location.Location;

import com.example.crowdtest.DatabaseManager;
import com.example.crowdtest.Question;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Class to represent a NonNegative experiment
 */
public class NonNegative extends Experiment {
    private ArrayList <NonNegativeTrial> trials;

    /**
     * Experiment constructor
     *
     * @param owner         Owner of the experiment
     * @param experimentID  A unique ID for this experiment
     */
    public NonNegative(String owner, String experimentID) {
        super(owner, experimentID);
        trials = new ArrayList<>();
    }

    public NonNegative() {
    }

    /**
     * Constructor for uploading from the database
     *
     * @param owner
     * @param experimentID
     * @param status
     * @param title
     * @param description
     * @param region
     * @param subscribers
     * @param questions
     * @param geoLocation
     * @param datePublished
     * @param minTrials
     */
    public NonNegative(String owner, String experimentID, String status, String title,
                       String description, String region, ArrayList<String> subscribers,
                       ArrayList<String> blackListedUsers, ArrayList<Question> questions,
                       boolean geoLocation, Date datePublished, int minTrials,
                       ArrayList<NonNegativeTrial> trials, boolean published) {
        super(owner, experimentID, status, title, description, region, subscribers, blackListedUsers, questions, geoLocation, datePublished, minTrials, published);
        this.trials = trials;
    }

    /**
     * Adds a new trial to the experiment
     * @param trialInput
     *  The trial that is going to be submitted in the experiment
     */
    public void addTrial(int trialInput, Location location) throws Exception {
        NonNegativeTrial trial = new NonNegativeTrial(trialInput);
        if (geolocationEnabled) trial.setLocation(location);
        trials.add(trial);
        addTrialToDB(trial);
    }

    public void addTrialToDB (NonNegativeTrial trial){
        DatabaseManager db = new DatabaseManager();
        HashMap<String, Object> trialData = new HashMap<>();
        trialData.put("locationLong", trial.getLocationLong());
        trialData.put("locationLat", trial.getLocationLat());
        trialData.put("timestamp", trial.getTimestamp());
        trialData.put("count", trial.getCount());
        db.addDataToCollection("Experiments/"+experimentID+"/trials", "trial#" + String.format("%05d", trials.size() - 1), trialData);
    }

    /**
     * Function for setting the trials for the non-negative experiment
     * @param trials
     *  ArrayList of non-negative trials
     */
    public void setTrials(ArrayList<NonNegativeTrial> trials) {
        this.trials = trials;
    }

    /**
     * Function for getting the trials for the non-negative experiment
     * @return
     *  ArrayList of non-negative trials
     */
    public ArrayList<NonNegativeTrial> getTrials() {
        return trials;
    }

    public Hashtable<String, Double> getStatistics(){

        Hashtable<String, Double> statistics = new Hashtable<>();

        statistics.put("Mean", 4.0);

        return statistics;
    };

}
