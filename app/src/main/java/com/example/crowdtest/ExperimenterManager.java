package com.example.crowdtest;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to manage the accessing, removing, and updating data related to Experimenter objects in the firestore database
 */
public class ExperimenterManager extends DatabaseManager {

    // ExperimenterManager attributes
    final private String collectionPath = "Users";
    String TAG = "GetExperimenterData";

    /**
     * ExperimenterManager constructor
     */
    public ExperimenterManager() {
        super();
    }

    public ExperimenterManager(FirebaseFirestore db) {

        super(db);
    }

    /**
     * Function for generating a unique username
     * @return
     */
    public String generateUsername() {
        return generateDocumentID("user", collectionPath);
    }


    /**
     * Method used to retrieve the signed in Experimenter, if already in the database, or create new one.
     * Uses helper interface RetrieveExperimenterResults to allow use of experimenter after firestore query is run
     * @param installationID
     *    The id representing the app's installation instance
     * @param retrieveExperimenterResults
     *     interface that is created and utilized to work with retrieved experiment
     */
    public void retrieveExperimenter(String installationID, RetrieveExperimenterResults retrieveExperimenterResults) {
        
        Query query = database.collection(collectionPath).whereEqualTo("installationID", installationID);

        //run query to see if the installation id is already in the database
        database.collection(collectionPath)
                .whereEqualTo("installationID", installationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Experimenter experimenter;

                        if (task.isSuccessful()) {

                            //if the query returns an empty result, create a new experiment using current installation ID
                            if (task.getResult().isEmpty()) {

                                experimenter = addNewExperimenter(installationID);

                                //call helper method
                                retrieveExperimenterResults.onRetrieveExperimenter(experimenter);

                            } else {
                                //if the query is not empty, it will have one result.
                                //Get the data from the query result and create an Experimenter object representing the signed in user
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    String userName = document.getId();
                                    String email = (String) document.getData().get("email");
                                    String phoneNumber = (String) document.getData().get("phoneNumber");
                                    String status = (String) document.getData().get("status");
                                    ArrayList<String> subscribed = (ArrayList<String>) document.getData().get("subscribed");

                                    experimenter = new Experimenter(new UserProfile(userName, installationID, email, phoneNumber));
                                    experimenter.setSubscribedExperimentIDs(subscribed);
                                    experimenter.setStatus(status);

                                    //call helper method
                                    retrieveExperimenterResults.onRetrieveExperimenter(experimenter);

                                }

                            }

                            return;

                        } else {

                            //if firestore query is unsuccessful, log an error and return
                            Log.d(TAG, "Error getting documents: ", task.getException());

                            return;

                        }

                    }
                });

    }


    /**
     * Function for adding an experimenter to the database
     * @param installationID
     *    An id representing the installation instance for the app
     * @return
     *     Returns the new experimenter created with the installationID
     */
    public Experimenter addNewExperimenter(String installationID) {
        // Generate initial unique username and create an experimenter
        String username = generateUsername();
        Experimenter experimenter = new Experimenter(new UserProfile(username, installationID));

        // Retrieve Experimenter data
        String status = experimenter.getStatus();
        ArrayList<String> subscribedExperiments = experimenter.getSubscribedExperimentIDs();

        // Retrieve UserProfile data
        UserProfile userProfile = experimenter.getUserProfile();
        String email = userProfile.getEmail();
        String phoneNumber = userProfile.getPhoneNumber();

        // Add experimenter data to HashMap
        HashMap<String, Object> experimenterData = new HashMap<>();
        experimenterData.put("status", status);
        experimenterData.put("installationID", installationID);
        experimenterData.put("email", email);
        experimenterData.put("phoneNumber", phoneNumber);
        experimenterData.put("subscribed", subscribedExperiments);

        // Add experimenter data to database
        addDataToCollection(collectionPath, username, experimenterData);

        return  experimenter;
    }


    /**
     * Function for updating a given experimenter in the database
     * @param experimenter
     */
    public void updateExperimenter(Experimenter experimenter) {

        // Retrieve Experimenter data
        String status = experimenter.getStatus();
        ArrayList<String> subscribedExperiments = experimenter.getSubscribedExperimentIDs();

        // Retrieve UserProfile data
        UserProfile userProfile = experimenter.getUserProfile();
        String username = userProfile.getUsername();
        String email = userProfile.getEmail();
        String phoneNumber = userProfile.getPhoneNumber();
        String installationID = userProfile.getInstallationID();

        // Add experimenter data to HashMap
        HashMap<String, Object> experimenterData = new HashMap<>();
        experimenterData.put("status", status);
        experimenterData.put("email", email);
        experimenterData.put("phoneNumber", phoneNumber);
        experimenterData.put("subscribed", subscribedExperiments);
        experimenterData.put("installationID", installationID);

        // Add experimenter data to database
        addDataToCollection(collectionPath, username, experimenterData);
    }

    /**
     * Function for deleting a experimenter from the database
     * @param experimenter
     */
    public void deleteExperimenter(Experimenter experimenter) {
        // Retrieve username
        UserProfile userProfile = experimenter.getUserProfile();
        String username = userProfile.getUsername();

        // Remove experimenter profile from database
        removeDataFromCollection(collectionPath, username);
    }


    /**
     * Method used for getting the Experimenter based on the given userID
     * @param userID The userID of the experimenter
     * @return The Experimenter
     */
    public static Experimenter getUser(String userID){
        DocumentSnapshot ds = FirebaseFirestore.getInstance().collection("Users").document(userID).get().getResult();
        Experimenter experimenter = new Experimenter(new UserProfile(userID, (String) ds.getData().get("installationID"), (String) ds.getData().get("email"), (String) ds.getData().get("phoneNumber")));
        return experimenter;
    }

}
