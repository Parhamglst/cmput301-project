package com.example.crowdtest.experiments;

import com.example.crowdtest.Experimenter;

import java.util.ArrayList;
import java.util.Collection;

public class NonNegative extends Experiment {

    /**
     * Experiment constructor
     *
     * @param owner         Owner of the experiment
     * @param experimentID  A unique ID for this experiment
     */
    public NonNegative(String owner, String experimentID) {
        super(owner, experimentID);
        trials = new ArrayList<>();
        this.type = "nonnegative";
    }

    @Override
    public void addTrial(String trialID) {
        trials.add(trialID);
    }

}
