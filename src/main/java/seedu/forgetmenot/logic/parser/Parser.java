package seedu.forgetmenot.logic.parser;

//@@author A0147619W
import static seedu.forgetmenot.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.forgetmenot.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.forgetmenot.commons.exceptions.IllegalValueException;
import seedu.forgetmenot.commons.util.StringUtil;
import seedu.forgetmenot.logic.commands.AddCommand;
import seedu.forgetmenot.logic.commands.ClearCommand;
import seedu.forgetmenot.logic.commands.ClearDoneCommand;
import seedu.forgetmenot.logic.commands.Command;
import seedu.forgetmenot.logic.commands.DeleteCommand;
import seedu.forgetmenot.logic.commands.DoneCommand;
import seedu.forgetmenot.logic.commands.EditCommand;
import seedu.forgetmenot.logic.commands.ExitCommand;
import seedu.forgetmenot.logic.commands.FindCommand;
import seedu.forgetmenot.logic.commands.HelpCommand;
import seedu.forgetmenot.logic.commands.IncorrectCommand;
import seedu.forgetmenot.logic.commands.RedoCommand;
import seedu.forgetmenot.logic.commands.SelectCommand;
import seedu.forgetmenot.logic.commands.SetStorageCommand;
import seedu.forgetmenot.logic.commands.ShowCommand;
import seedu.forgetmenot.logic.commands.UndoCommand;
import seedu.forgetmenot.logic.commands.UndoneCommand;
import seedu.forgetmenot.model.task.Time;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    
    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace
    
    private static final Pattern TASK_DATA_ARGS_FORMAT = Pattern.compile(
            "(?<name>([^;](?<! (at|from|to|by) ))*)" + "((?: (at|from) )(?<start>(([^;](?<! (to|by|every) ))|(\\[^/]))+))?"
                    + "((?: (to|by) )(?<end>(([^;](?<! every ))|(\\[^/]))+))?"
            		+ "((?: every )(?<recurring>(([^;](?<! p/))|(\\[^/]))+))?"
                    );
    
    private static final Pattern TASK_EDIT_ARGS_FORMAT = Pattern.compile( "(?<index>\\d+)"
    		+ "((?: )(?<name>([^/](?<! (at|from|to|by) ))*))?" + "((?: (at|from) )(?<start>(([^;](?<! (to|by) ))|(\\[^/]))+))?"
            + "((?: (to|by) )(?<end>(([^;](?<! (every) ))|(\\[^/]))+))?"
    		+ "((?: (every) )(?<recurring>(([^;](?<! p/))|(\\[^/]))+))?"
            );

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);
            
        case SetStorageCommand.COMMAND_WORD:
    		return prepareSetStorage(arguments);

        case ClearCommand.COMMAND_WORD:
            return prepareClear(arguments);

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ShowCommand.COMMAND_WORD:
			return prepareShow(arguments);
			
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();
        
        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();
            
        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        
        case DoneCommand.COMMAND_WORD:
        	return prepareDone(arguments);
            
        case UndoneCommand.COMMAND_WORD:
        	return prepareUndone(arguments);
        
        case ClearDoneCommand.COMMAND_WORD:
        	return new ClearDoneCommand();
        	
        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    
    //@@author A0139198N
    private Command prepareClear(String args) {
    	args = args.trim();
    	
    	if (args.equals("")) {
    		return new ClearCommand();
    	}
    	
    	else if (args.equals("done")) {
    		return new ClearDoneCommand();
    	}
    	
    	else
    		return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
    }
    
    private Command prepareShow(String args) {
    	
    	args = args.trim();
    	
    	if(args.equals("done")) {
    		return new ShowCommand("done");
    	}
    	
    	else if(args.equals("all")) {
    		return new ShowCommand("all");
    	}
    	else if (args.equals("")) {
    		return new ShowCommand("");
    	}
    	
        else if (args.equals("overdue")) {
            return new ShowCommand("overdue");
        }
    	
        else if (args.equals("floating")) {
        	return new ShowCommand("floating");
        }
    	
    	else {
            Time time;
            try {
                time = new Time(args);
                if (Time.isValidDate(time.appearOnUIFormatForDate())) {
                return new ShowCommand(time.appearOnUIFormatForDate());
            } 
                
            } catch (IllegalValueException e) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowCommand.MESSAGE_USAGE));
            }
            
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowCommand.MESSAGE_USAGE));
    	}
    }
    //@@author
    
    //@@author A0147619W
    /**
     * Trims the argument and sends it to SetStorageCommand
     * 
     * @param args
     * @return the prepared command
     */
    private Command prepareSetStorage(String args) {
    	if(args != null) {
    		args = args.trim();
    		return new SetStorageCommand(args);
    	}
    	else
    		return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStorageCommand.MESSAGE_USAGE));
    }
    
    //@@author A0139671X
    private Command prepareEdit(String args) {
        final Matcher matcher = TASK_EDIT_ARGS_FORMAT.matcher(args.trim());
        
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        return new EditCommand(matcher.group("index"), matcher.group("name"), matcher.group("start"), matcher.group("end"));
    }
    //@@author

    //@@author A0147619W
    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
    	final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim());

        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        
        String name = matcher.group("name");
        String done = "false";
    	String startTime = (matcher.group("start") == null) ? "" : matcher.group("start");
        String endTime = (matcher.group("end") == null) ? "" : matcher.group("end");
        String recurFreq = (matcher.group("recurring") == null)? "": matcher.group("recurring");
        
        try {
	            return new AddCommand(
	                    name,
	                    done,
	                    startTime,
	                    endTime,
	                    recurFreq
	            );       
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the done command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    
    //@@author A0139198N
    private Command prepareDone(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }

        return new DoneCommand(index.get());
    }
    
    private Command prepareUndone(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoneCommand.MESSAGE_USAGE));
        }

        return new UndoneCommand(index.get());
    }
    //@@author
    
    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

}