
package com.example.leaverequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageObject {

    @SerializedName("f_from")
    @Expose
    private String f_from;
    @SerializedName("f_reason")
    @Expose
    private String f_reason;
    @SerializedName("f_sender_id")
    @Expose
    private String f_senderId;
    @SerializedName("f_status")
    @Expose
    private String f_status;
    @SerializedName("f_till")
    @Expose
    private String f_till;
    @SerializedName("f_to_number")
    @Expose
    private String f_toNumber;

    public MessageObject(){}

    public String getFrom() {
        return f_from;
    }

    public void setFrom(String f_from) {
        this.f_from = f_from;
    }

    public String getReason() {
        return f_reason;
    }

    public void setReason(String f_reason) {
        this.f_reason = f_reason;
    }

    public String getSenderId() {
        return f_senderId;
    }

    public void setSenderId(String f_senderId) {
        this.f_senderId = f_senderId;
    }

    public String getStatus() {
        return f_status;
    }

    public void setStatus(String f_status) {
        this.f_status = f_status;
    }

    public String getTill() {
        return f_till;
    }

    public void setTill(String f_till) {
        this.f_till = f_till;
    }

    public String getToNumber() {
        return f_toNumber;
    }

    public void setToNumber(String f_toNumber) {
        this.f_toNumber = f_toNumber;
    }

}
