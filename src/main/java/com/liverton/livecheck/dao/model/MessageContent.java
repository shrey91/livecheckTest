package com.liverton.livecheck.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sshah on 24/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContent implements Serializable {

    private List<MessageDetail> message;


    public MessageContent() {
    }

    public List<MessageDetail> getMessageDetails() {
        return message;
    }

    public void setMessageDetails(List<MessageDetail> messageDetails) {
        this.message = messageDetails;
    }



    @Override
    public String toString() {
        return "MessageResponse{" +
                "messageDetails=" + message +
                '}';
    }
}
