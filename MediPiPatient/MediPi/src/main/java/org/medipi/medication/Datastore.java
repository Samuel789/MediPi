package org.medipi.medication;

import com.sun.prism.impl.Disposer;
import org.medipi.MediPi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sam on 02/08/17.
 */
public class Datastore {

    private List<Schedule> patientSchedules;
    private ArrayList<RecordedDose> recordedDoses;
    private MediPi medipi;

    public Datastore(MediPi medipi){
        this.medipi = medipi;
        recordedDoses = new ArrayList<>();
    }

    public List<Schedule> getPatientSchedules() {
        return patientSchedules;
    }

    public void replacePatientSchedules(List<Schedule> patientSchedules) {
        this.patientSchedules = patientSchedules;
    }

    public void recordDose(RecordedDose dose) {
        recordedDoses.add(dose);
    }

    public ArrayList<RecordedDose> getRecordedDoses() {
        return recordedDoses;
    }


}
