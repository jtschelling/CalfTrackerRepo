package com.calftracker.project.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jared on 11/16/2017.
 */

public class Task {


    private int year;
    private int month;
    private int day;

    private ArrayList<Calf> calvesToObserve;
    private ArrayList<ArrayList<VaccineTask>> vaccinesToAdminister;
    private ArrayList<VaccineTask> overdueVaccinations;

    private ArrayList<ArrayList<IllnessTask>> illnessTracker;

    public Task(Calendar dateLastUpdated, ArrayList<Calf> calvesToObserve,
                ArrayList<ArrayList<VaccineTask>> vaccinesToAdminister, ArrayList<VaccineTask> overdueVaccinations,
                ArrayList<ArrayList<IllnessTask>> illnessTracker) {
        super();
        this.year = dateLastUpdated.get(Calendar.YEAR);
        this.month = dateLastUpdated.get(Calendar.MONTH);
        this.day = dateLastUpdated.get(Calendar.DAY_OF_MONTH);
        this.calvesToObserve = calvesToObserve;
        this.vaccinesToAdminister = vaccinesToAdminister;
        this.overdueVaccinations = overdueVaccinations;
        this.illnessTracker = illnessTracker;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<ArrayList<IllnessTask>> getIllnessTracker() {
        return illnessTracker;
    }

    public void setIllnessTracker(ArrayList<ArrayList<IllnessTask>> illnessTracker) {
        this.illnessTracker = illnessTracker;
    }

    public Calendar makeDateLastUpdated() {
        return new GregorianCalendar(year, month, day);
    }

    public void putDateLastUpdated(Calendar dateLastUpdated) {
        this.year = dateLastUpdated.get(Calendar.YEAR);
        this.month = dateLastUpdated.get(Calendar.MONTH);
        this.day = dateLastUpdated.get(Calendar.DAY_OF_MONTH);
    }

    public ArrayList<Calf> getCalvesToObserve() {
        return calvesToObserve;
    }

    public void setCalvesToObserve(ArrayList<Calf> calvesToObserve) {
        this.calvesToObserve = calvesToObserve;
    }

    public ArrayList<ArrayList<VaccineTask>> getVaccinesToAdminister() {
        return vaccinesToAdminister;
    }

    public void setVaccinesToAdminister(ArrayList<ArrayList<VaccineTask>> vaccinesToAdminister) {
        this.vaccinesToAdminister = vaccinesToAdminister;
    }

    public ArrayList<VaccineTask> getOverdueVaccinations() {
        return overdueVaccinations;
    }

    public void setOverdueVaccinations(ArrayList<VaccineTask> overdueVaccinations) {
        this.overdueVaccinations = overdueVaccinations;
    }

    public void updateTasks()
    {
        Calendar today = Calendar.getInstance();
        Calendar dateLastUpdated = new GregorianCalendar(year, month, day);

        int daysPassedSinceUpdate = (int) Math.abs(calendarDaysBetween(today, dateLastUpdated));

        // do the stuff for vaccinetasks
        // you have to do this however many times a day has passed because I couldn't think of
        // a less confusing way to to it.
        for(int i = 0; i < daysPassedSinceUpdate; i++) {
            for(int j = 0; j < this.vaccinesToAdminister.size(); j++) {

                // enter here when deciding to move vaccines from the 0th index to overduelist
                if(j == 0) {
                    for (int k = 0; k < vaccinesToAdminister.get(j).size(); k++) {
                        if (!vaccinesToAdminister.get(j).get(k).isStart()) {
                            this.overdueVaccinations.add(vaccinesToAdminister.get(j).get(k));
                            vaccinesToAdminister.get(j).get(k).setStart(true);
                            VaccineTask toRemove = new VaccineTask(vaccinesToAdminister.get(j).get(k));

                            // search in vaccines.get(0) for instances of the vaccine to be moved to
                            // overdue. there will be two because of the start and end dates.
                            for (int m = vaccinesToAdminister.get(j).size() - 1; m >= 0; m--) {
                                if (vaccinesToAdminister.get(j).get(m).equals(toRemove))
                                    vaccinesToAdminister.get(j).remove(m);
                            }
                        }
                    }
                }
                // enter here when moving all other indices
                else {
                    while(!vaccinesToAdminister.get(j).isEmpty()) {
                        vaccinesToAdminister.get(j - 1).add(vaccinesToAdminister.get(j).remove(0));
                    }
                }
            }
        }

        // do the stuff for illnessTasks
        for(int i = 0; i < daysPassedSinceUpdate; i++)
            // dont touch index zero, all illnesstasks
            // at zero don't have active medicine
            for(int j = 1; j < illnessTracker.size(); j++)
                    while(!illnessTracker.get(j).isEmpty())
                        illnessTracker.get(j - 1).add(illnessTracker.get(j).remove(0));

        this.putDateLastUpdated(Calendar.getInstance());

    }

    public void editVaccine(Vaccine vaccine)
    {
        for(int i = 0; i < this.vaccinesToAdminister.size(); i++)
            for(int j = 0; j < this.vaccinesToAdminister.get(i).size(); j++)
                if(this.vaccinesToAdminister.get(i).get(j).getVaccine().getName().equals(vaccine.getName()))
                    this.vaccinesToAdminister.get(i).get(j).setVaccine(vaccine);

        for(int i = 0; i < this.overdueVaccinations.size(); i++)
            if(this.overdueVaccinations.get(i).getVaccine().getName().equals(vaccine.getName()))
                this.overdueVaccinations.get(i).setVaccine(vaccine);
    }

    public void administerMedication(IllnessTask illnessTask) {
        OUTER:for(int i = 0; i < this.getIllnessTracker().size(); i++)
            for(int j = 0; j < this.getIllnessTracker().get(i).size(); j++)
                if(this.getIllnessTracker().get(i).get(j).equals(illnessTask)) {
                    illnessTask = this.getIllnessTracker().get(i).remove(j);
                    break OUTER;
                }

        this.getIllnessTracker().get(illnessTask.getMedicine().getTimeActive()).add(illnessTask);

    }

    public void editMedication(Medicine medicine, IllnessTask illnessTask) {
        OUTER:for(int i = 0; i < this.getIllnessTracker().size(); i++)
            for(int j = 0; j < this.getIllnessTracker().get(i).size(); j++)
                if(this.getIllnessTracker().get(i).get(j).equals(illnessTask)) {
                    illnessTask.setMedicine(medicine);
                    break OUTER;
                }
    }

    public void removeIllnessTask(IllnessTask illnessTask) {
        OUTER:for(int i = 0; i < this.getIllnessTracker().size(); i++)
            for(int j = 0; j < this.getIllnessTracker().get(i).size(); j++)
                if(this.getIllnessTracker().get(i).get(j).equals(illnessTask)) {
                    this.getIllnessTracker().get(i).remove(j);
                    break OUTER;
                }
    }

    public void placeVaccineInTasks(Vaccine vaccine, Calf calf)
    {
        Calendar today = Calendar.getInstance();

        Calendar vaccStart = Calendar.getInstance();
        vaccStart.setTimeZone(calf.makeCalendarDOB().getTimeZone());
        vaccStart.setTimeInMillis(calf.makeCalendarDOB().getTimeInMillis());
        vaccStart.add(Calendar.DATE, vaccine.getToBeAdministered().get(0).getSpan()[0]);

        Calendar vaccEnd = Calendar.getInstance();
        vaccEnd.setTimeZone(calf.makeCalendarDOB().getTimeZone());
        vaccEnd.setTimeInMillis(calf.makeCalendarDOB().getTimeInMillis());
        vaccEnd.add(Calendar.DATE, vaccine.getToBeAdministered().get(0).getSpan()[1]);

        long daysBetweenEnd = calendarDaysBetween(today, vaccEnd);

        if(daysBetweenEnd < 0) {
            this.overdueVaccinations.add(new VaccineTask(vaccine, calf, false));
            return;
        } else {
            this.vaccinesToAdminister.get((int) daysBetweenEnd).add(new VaccineTask(vaccine, calf, false));
        }

        long daysBetweenStart = calendarDaysBetween(today, vaccStart);

        if(daysBetweenStart < 0) {
            this.vaccinesToAdminister.get(0).add(new VaccineTask(vaccine, calf, true));
        } else {
            this.vaccinesToAdminister.get((int) daysBetweenStart).add(new VaccineTask(vaccine, calf, true));
        }
    }

    public void removeVaccineTaskFromTask(Vaccine vaccine, Calf calf) {

    }

    public void placeIllnessInTasks(Illness illness, Medicine medicine, Calf calf)
    {

    }



    private static long calendarDaysBetween(Calendar today, Calendar dateToCompare)
    {

        // Create copies so we don't update the original calendars.

        Calendar start = Calendar.getInstance();
        start.setTimeZone(today.getTimeZone());
        start.setTimeInMillis(today.getTimeInMillis());

        Calendar end = Calendar.getInstance();
        end.setTimeZone(dateToCompare.getTimeZone());
        end.setTimeInMillis(dateToCompare.getTimeInMillis());

        // Set the copies to be at midnight, but keep the day information.

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        // At this point, each calendar is set to midnight on
        // their respective days. Now use TimeUnit.MILLISECONDS to
        // compute the number of full days between the two of them.

        return TimeUnit.MILLISECONDS.toDays(
                end.getTimeInMillis() - start.getTimeInMillis());
    }
}
