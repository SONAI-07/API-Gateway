package com.apiGateway.registry.model;


import lombok.*;


@Getter
@Setter

public class TimeoutTask {

   private String instanceID ;

   private int remainingRounds ;

   public boolean isCancelled ;



      public TimeoutTask(String instanceID, int remainingRounds ) {
        this.instanceID = instanceID;
        this.remainingRounds = remainingRounds;

    }





}
