package com.liverton.livecheck.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by sshah on 23/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse implements Serializable {

    private MessageContent messageContent;

    public MessageResponse() {
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "messageContent=" + messageContent +
                '}';
    }
}
