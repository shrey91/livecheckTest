package com.liverton.livecheck.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sshah on 29/08/2016.
 */
public class ApplicationStatusForm {

    @NotEmpty(message = "Failure Count cannot be empty")
    private Integer failureCount;

    @NotEmpty(message = "Average Response cannot be empty")
    private String averageResponse;

    public ApplicationStatusForm() {
    }

    public ApplicationStatusForm(Integer failureCount, String averageResponse) {
        this.failureCount = failureCount;
        this.averageResponse = averageResponse;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getAverageResponse() {
        return averageResponse;
    }

    public void setAverageResponse(String averageResponse) {
        this.averageResponse = averageResponse;
    }

    @Override
    public String toString() {
        return "ApplicationStatusForm{" +
                "failureCount=" + failureCount +
                ", averageResponse='" + averageResponse + '\'' +
                '}';
    }
}
