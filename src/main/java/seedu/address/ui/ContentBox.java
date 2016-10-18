package seedu.address.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.commons.core.LogsCenter;

import java.util.logging.Logger;

/**
 * Panel containing the list of tasks.
 */
public class ContentBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(ContentBox.class);
    private static final String FXML = "ContentBox.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    
    @FXML
    private ImageView imagetest;
    
    @FXML
    private AnchorPane summarypane;
    
    @FXML
    private Label summary;
    
    @FXML
    private GridPane gridpane;
    
    @FXML
    private Label alltask;
    
    @FXML
    private Label today;
    
    @FXML
    private Label tomorrow;
    
    @FXML
    private Label someday;
    
    @FXML
    private Label upcoming;
    
    @FXML
    private Label dummy1;
    
    @FXML
    private Label dummy2;

    @FXML
    private Label dummy3;
    
    @FXML
    private Label dummy4;
    
    @FXML
    private Label dummy5;
    

    public ContentBox() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static ContentBox load(Stage primaryStage, AnchorPane ContentBoxPlaceholder,
                                       ObservableList<ReadOnlyTask> taskList) {
        ContentBox contentbox =
                UiPartLoader.loadUiPart(primaryStage, ContentBoxPlaceholder, new ContentBox());
        contentbox.configure(taskList);
        return contentbox;
    }

    private void configure(ObservableList<ReadOnlyTask> taskList) {
//        setConnections(taskList);
        addToPlaceholder();
    }

/*    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }
*/
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }
/*
    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        public TaskListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(TaskCard.load(task, getIndex() + 1).getLayout());
            }
        }
    }
*/
}