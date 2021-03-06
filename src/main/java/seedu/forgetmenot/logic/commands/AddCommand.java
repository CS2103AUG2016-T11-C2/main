package seedu.forgetmenot.logic.commands;

import seedu.forgetmenot.commons.exceptions.IllegalValueException;
import seedu.forgetmenot.model.task.Done;
import seedu.forgetmenot.model.task.Name;
import seedu.forgetmenot.model.task.Recurrence;
import seedu.forgetmenot.model.task.Task;
import seedu.forgetmenot.model.task.Time;
import static seedu.forgetmenot.commons.core.Messages.MESSAGE_INVALID_START_AND_END_TIME;

import seedu.forgetmenot.commons.core.EventsCenter;
import seedu.forgetmenot.commons.events.ui.JumpToListRequestEvent;

//@@author A0147619W 
/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to ForgetMeNot. "
            + "\tParameters: TASKNAME DATE"
            + "\tExample: " + COMMAND_WORD
            + " Homework by tomorrow 6pm";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_CLASH_WARNING = "WARNING! This task clashes with one of the tasks in ForgetMeNot. Type undo if you want to undo the previous add.";
    
    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid           
     */
    public AddCommand(String name, String date, String start, String end, String recur)
            throws IllegalValueException {
        
        Time startTime = new Time(start);
        Time endTime = new Time(end);
        
        if(!Time.checkOrderOfDates(start, end)) {
        	throw new IllegalValueException(MESSAGE_INVALID_START_AND_END_TIME);
        }
        
        this.toAdd = new Task(
                new Name(name),
                new Done(false),
                startTime,
                endTime,
                new Recurrence(recur)
        );
    }

	@Override
    public CommandResult execute() throws IllegalValueException {
        assert model != null;
        	
        boolean clashCheck = false;
    	if(model.isClashing(toAdd))
    		clashCheck = true;
    	
        model.saveToHistory();
        model.addTask(toAdd);
        if (toAdd.getRecurrence().getValue())
            model.addRecurringTask(toAdd);
        model.updateFilteredTaskListToShowNotDone();
        
        jumpToTask();
        
    	return clashCheck? new CommandResult(MESSAGE_CLASH_WARNING + "\n" + String.format(MESSAGE_SUCCESS, toAdd)):
    					new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

	/**
	 * Jumps to the added Task
	 */
	private void jumpToTask() {
		int targetIndex = model.getFilteredTaskList().indexOf(toAdd);
        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
	}
}
