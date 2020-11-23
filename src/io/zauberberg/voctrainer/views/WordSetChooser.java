package io.zauberberg.voctrainer.views;

import java.util.function.Consumer;
import java.util.function.Supplier;

import io.zauberberg.voctrainer.model.WordSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WordSetChooser implements View {
	
	private static EventHandler<ActionEvent> chooseNotNull(Supplier<WordSet> fileChooser, Consumer<WordSet> event) {
		return action -> {
			WordSet w = fileChooser.get();
			if (w!=null) event.accept(w);
		};
	}

	private final Scene scene;

	public WordSetChooser(Supplier<WordSet> fileChooser, Consumer<WordSet> editEvent, Consumer<WordSet> trainEvent) {

		// Title
		Text title = new Text("Vocabulary Trainer");
		title.setFont(new Font(title.getFont().getName(), 28));
		HBox header = new HBox(title);
		header.setAlignment(Pos.CENTER);

		// Menu buttons
		Button edit = new Button("Edit word set..."), train = new Button("Training...");
		edit.setOnAction(chooseNotNull(fileChooser, editEvent));
		train.setOnAction(chooseNotNull(fileChooser, trainEvent));
		HBox commands = new HBox(edit, train);
		commands.setAlignment(Pos.CENTER);
		commands.setSpacing(25);

		// Assemble layout.
		VBox layout = new VBox(title, commands);
		layout.setPadding(new Insets(50));
		layout.setSpacing(25);
		scene = new Scene(layout);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

}
