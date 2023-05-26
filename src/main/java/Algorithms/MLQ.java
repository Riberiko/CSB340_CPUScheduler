/** Multilevel Queue */

package Algorithms;
import Utilities.*;
import java.util.*;

public class MLQ implements AlgorithmsInterface {
    private Scheduler schedule;
    private Dispatcher dispatch;
    private Queue<ProcessControlBlock> ready;

    public MLQ(ArrayList<AlgorithmsInterface> ready) {
        this.schedule = new Scheduler(ready);
        this.dispatch = new Dispatcher();
        this.ready = this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getReady();
    }

    /** {@inheritDoc} */
    public AlgorithmTypes getAlgorithmType() {
        return AlgorithmTypes.MLQ;
    }

    /** {@inheritDoc} */
    public void scheduleNextProcess() {
        this.schedule.getNextRunningProcess(ready, this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getDispatcher());
        this.dispatch.setRunningProcess(this.schedule.getReadyList().get(this.schedule.getReadyIndex()).getDispatcher().getRunningProcess());

    }

    /** {@inheritDoc} */
    public void dispatchNextProcess(ProcessControlBlock running) {
        schedule.getReadyList().get(schedule.getReadyIndex()).dispatchNextProcess(running);
        this.dispatch.updateMultiTimer(schedule);
        if(running != null && running.getState() == ProcessControlBlock.ProcessState.COMPLETE) {
            schedule.flagProcessAsComplete(running);
        }
        if(schedule.getReadyList().get(schedule.getReadyIndex()).isCompleted()) {
            schedule.incrementReadyIndex();
            if(!isCompleted()) {
                ready = schedule.getReadyList().get(schedule.getReadyIndex()).getReady();
                schedule.getReadyList().get(getScheduler().getReadyIndex()).getDispatcher().setExecutionTimer(dispatch.getExecutionTimer());
                schedule.getReadyList().get(getScheduler().getReadyIndex()).getDispatcher().setIdleTimer(dispatch.getIdleTimer());
            }
        }
    }

    /** {@inheritDoc} */
    public Boolean isCompleted() {
        return this.schedule.getActive().isEmpty();
    }

    /** {@inheritDoc} */
    public Scheduler getScheduler() {
        return this.schedule;
    }

    /** {@inheritDoc} */
    public Dispatcher getDispatcher() {
        return this.dispatch;
    }

    /** {@inheritDoc} */
    public Queue<ProcessControlBlock> getReady() { return this.ready; }
}