package com.example.crowdtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.crowdtest.ExperimentManager;
import com.example.crowdtest.R;
import com.example.crowdtest.experiments.Count;
import com.example.crowdtest.experiments.Experiment;
/**
 * Fragment for the final stage (geolocation toggle) of experiment creation and publishing the
 * experiment to the database
 */
public class GeolocationToggleFragment extends Fragment {
    private Experiment experiment;
    private ExperimentManager manager;

    GeolocationToggleFragment(Experiment newExperiment, ExperimentManager expManager) {
        experiment = newExperiment;
        manager = expManager;
    }

    /**
     * Custom OnCreateView method for the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geolocation_toggle, container, false);
        Button cancelButton = (Button) view.findViewById(R.id.geolocationCancelButton);
        Button finishButton = (Button) view.findViewById(R.id.geolocationFinishButton);
        Switch toggle = (Switch) view.findViewById(R.id.geolocationSwitch);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (toggle.isChecked()) {
                    experiment.setGeoLocation(true);
                }
                else {
                    experiment.setGeoLocation(false);
                }
                manager.publishExperiment(experiment);
                getActivity().finish();

            }
        });


        return view;
    }

}
