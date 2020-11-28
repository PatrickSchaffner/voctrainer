package io.zauberberg.voctrainer.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

@ExtendWith(ApplicationExtension.class)
public class ButtonTableCellTest {
	
	private ObservableList<Object> data;
	private TableView<Object> table;
	private Property<Object> lastClicked; 

	@Start
	void init(Stage stage) {
		data = FXCollections.observableArrayList(new Object(), "Hans");
		table = new TableView<>(data);
		lastClicked = new SimpleObjectProperty<>();
		Callback<TableColumn<Object, Button>, TableCell<Object, Button>> createCell = ButtonTableCell.forTableColumn("text", item -> lastClicked.setValue(item));	
		TableColumn<Object, Button>col = new TableColumn<>();
		col.setCellFactory(c -> {
			ButtonTableCell<Object> cell = (ButtonTableCell<Object>) createCell.call(c);
			cell.getStyleClass().add(".buttoncell");
			return cell;
		});
		table.getColumns().add(col);
		stage.setScene(new Scene(new VBox(table)));
		stage.show();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testButtonColumn(FxRobot robot) {
		for (TableCell<Object, Button> cell : robot.lookup(".buttoncell").queryAllAs(TableCell.class)) {
			Button b = cell.getItem();
			assertNotNull(b);
			robot.clickOn(b);
			assertEquals(data.get(cell.getIndex()), lastClicked.getValue());
		}
	}
}
