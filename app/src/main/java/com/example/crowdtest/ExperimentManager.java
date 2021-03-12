package com.example.crowdtest;

import java.util.ArrayList;
import java.util.Arrays; //TODO: remove this
import java.util.HashMap;

/**
 *
 */
public class ExperimentManager extends DatabaseManager {

    // ExperimentManager attributes
    private ArrayList<Experiment> experiments = new ArrayList<Experiment>(
            Arrays.asList(
                    new Experiment(new Experimenter("User1"), "1"),
                    new Experiment(new Experimenter("User2"), "2"),
                    new Experiment(new Experimenter("User2"), "3"))
    ); // TODO: remove this

    final private String collectionPath = "Experiments";

    /**
     * ExperimentManager constructor
     */
    public ExperimentManager() {
        super();
        experiments = new ArrayList<>();
    }

    /**
     * Function for generating a unique experiment ID
     * @return
     */
    public String generateExperimentID() {
        return generateDocumentID("experiment", collectionPath);
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getAllExperimentIDs() {
        return getAllDocuments(collectionPath);
    }

    /**
     *
     * @return
     */
    public ArrayList<Experiment> getAllExperimentInfo() {
        return experiments;
    }

    /**
     * Get owned experiments for the signed in user
     * @param owner
     *     The user whose owned experiments are being obtained
     * @return
     *     ArrayList of experiments owned  by given user
     */
    public ArrayList<Experiment> getOwnedExperiments(Experimenter owner) {

        return experiments;
    }

    /**
     *
     * @param subscriber
     *    The currently signed in user
     * @return
     *     An array list of all the experiments that user is subscribed to
     */
    public ArrayList<Experiment> getSubscribedExperiments(Experimenter subscriber) {

        return experiments;
    }

    /**
     * Function for adding an experiment to the database
     */
    public void publishExperiment(Experimenter owner) {
        // Generate unique experiment ID and create experiment
        String experimentID = generateExperimentID();
        Experiment experiment = new Experiment(owner, experimentID);
        experiments.add(experiment);

        // Retrieve experiment owner's profile
        UserProfile ownerProfile = owner.getUserProfile();

        // Add experiment data to HashMap
        HashMap<String, Object> experimentData = new HashMap<>();
        experimentData.put("owner", ownerProfile.getUsername());
        experimentData.put("status", experiment.getStatus());
        experimentData.put("title", experiment.getTitle());
        experimentData.put("description", experiment.getDescription());
        experimentData.put("region", experiment.getRegion());
        experimentData.put("subscribers", experiment.getSubscribers());

        // Add experiment to database
        // TODO: add questions as a sub-collection
        addDataToCollection(collectionPath, experimentID, experimentData);
    }

    /**
     * Function for updating a given experiment in the database
     * @param experiment
     */
    public void updateExperiment(Experiment experiment) {
        // Retrieve experiment owner's profile
        Experimenter owner = experiment.getOwner();
        UserProfile ownerProfile = owner.getUserProfile();

        // Add experiment data to HashMap
        HashMap<String, Object> experimentData = new HashMap<>();
        experimentData.put("owner", ownerProfile.getUsername());
        experimentData.put("status", experiment.getStatus());
        experimentData.put("description", experiment.getDescription());
        experimentData.put("region", experiment.getRegion());
        experimentData.put("subscribers", experiment.getSubscribers());

        // Add experiment to database
        addDataToCollection(collectionPath, experiment.getExperimentID(), experimentData);
    }

    /**
     * Function for deleting an experiment from the database
     * @param experiment
     */
    public void unpublishExperiment(Experiment experiment) {
        // Retrieve experimentID
        String experimentID = experiment.getExperimentID();

        // Remove experiment from database
        removeDataFromCollection(collectionPath, experimentID);
    }
}