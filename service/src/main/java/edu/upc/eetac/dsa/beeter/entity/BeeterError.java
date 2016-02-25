package edu.upc.eetac.dsa.beeter.entity;

/**
 * Created by marc on 25/02/16.
 */
public class BeeterError {
    private int status;
    private String reason;

    public BeeterError() {
    }

    public BeeterError(int status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
