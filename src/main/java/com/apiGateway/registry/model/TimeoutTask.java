package com.apiGateway.registry.model;


import lombok.*;

@Getter


public class TimeoutTask {

   private String InstanceID ;

    private int remainingRounds ;

    private boolean isCancelled=false;

      public TimeoutTask(String instanceID, int remainingRounds) {
        this.InstanceID = instanceID;
        this.remainingRounds = remainingRounds;
    }





}
