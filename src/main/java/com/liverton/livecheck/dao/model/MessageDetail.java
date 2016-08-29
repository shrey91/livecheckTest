package com.liverton.livecheck.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by sshah on 24/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDetail implements Serializable {

    private Boolean accepted;
    private String to;
    private String apiMessageId;

    public MessageDetail() {
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getApiMessageId() {
        return apiMessageId;
    }

    public void setApiMessageId(String apiMessageId) {
        this.apiMessageId = apiMessageId;
    }

    @Override
    public String toString() {
        return "MessageDetail{" +
                "accepted=" + accepted +
                ", to='" + to + '\'' +
                ", apiMessageId='" + apiMessageId + '\'' +
                '}';
    }
}
