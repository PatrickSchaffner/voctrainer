package io.zauberberg.voctrainer.views;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author https://stackoverflow.com/a/49066796/919681
 */
public class ButtonTableCell<T> extends TableCell<T, Button> {
	
	public static <T> Callback<TableColumn<T, Button>, TableCell<T,Button>> forTableColumn(String label, Consumer<T> handler) {
		return param -> new ButtonTableCell<>(label, handler);
	}

	private final Button button;

	public ButtonTableCell(String label, Consumer<T> handler) {
		button = new Button(label);
		button.setOnAction(clicked ->  handler.accept(getTableView().getItems().get(getIndex())));
		button.setMaxWidth(Double.MAX_VALUE);
	}
	
	@Override
	public void updateItem(Button item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(empty ? null : button);
	}
}
