package com.liverton.livecheck.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by sshah on 23/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRequest {

    private String text;
    private List<String> to;

    public MessageRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "text='" + text + '\'' +
                ", to=" + to +
                '}';
    }

    public MessageRequest(String text, List<String> to) {
        this.text = text;
        this.to = to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }
}
