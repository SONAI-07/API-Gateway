package com.apiGateway.registry.service;



import com.apiGateway.registry.model.TimeoutTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;


@Component
public class instanceEviction {

    private final RegisterService registerService;

    private int currentTick = 0;

    public ConcurrentLinkedQueue<TimeoutTask>[] wheel = new ConcurrentLinkedQueue[60];

    public instanceEviction(RegisterService registerService) {
        this.registerService = registerService;
    }



    public void initializer ()
    {
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new ConcurrentLinkedQueue<>();
        }
    }




    // future cart calculation on Gateway ping
    public TimeoutTask scheduleTimeout (String instanceId, int delaySeconds) {


        int ticksToMove = delaySeconds;

        int newCart = (currentTick + ticksToMove) % 60;

        int rounds = ticksToMove / 60 ;

        TimeoutTask newTask = new TimeoutTask(instanceId, rounds);

        wheel[newCart].add(newTask);

        return newTask;

    }



      @Scheduled(fixedDelay = 1000)
      public void advanceWheel () {
          ConcurrentLinkedQueue<TimeoutTask> cart = wheel[currentTick];

          currentTick = (currentTick + 1) % 60;

          for (TimeoutTask task : cart) {
              if (task.isCancelled) {
                  cart.remove(task); // Throw it in the trash
                  continue;
              }
              if (task.getRemainingRounds() > 0) {
                  task.setRemainingRounds(task.getRemainingRounds() - 1); // Subtract a round, leave it in the cart
                  continue;
              }

              // IF WE GET HERE: It's not cancelled, and rounds == 0.
              // EXECUTE THE EVICTION!
              executeEviction(task.getInstanceID());
              cart.remove(task); // Remove from wheel
          }


      }

         public void executeEviction  (String instanceID)
         {

     }



}

