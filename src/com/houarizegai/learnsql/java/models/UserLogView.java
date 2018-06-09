package com.houarizegai.learnsql.java.models;

import java.sql.Date;
import java.sql.Time;

public class UserLogView {

    private int idLog;
    private Date dateLog;
    private Time timeLog;
    private Time durationLogin;

    public UserLogView(int idLog, Date dateLog, Time timeLog, Time durationLogin) {
        this.idLog = idLog;
        this.dateLog = dateLog;
        this.timeLog = timeLog;
        this.durationLogin = durationLogin;
    }

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public Date getDateLog() {
        return dateLog;
    }

    public void setDateLog(Date dateLog) {
        this.dateLog = dateLog;
    }

    public Time getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(Time timeLog) {
        this.timeLog = timeLog;
    }

    public Time getDurationLogin() {
        return durationLogin;
    }

    public void setDurationLogin(Time durationLogin) {
        this.durationLogin = durationLogin;
    }

}
