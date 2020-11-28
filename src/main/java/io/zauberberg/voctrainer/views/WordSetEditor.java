package io.zauberberg.voctrainer.views;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import io.zauberberg.voctrainer.model.Word;
import io.zauberberg.voctrainer.model.WordSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WordSetEditor implements View {

	private static EventHandler<ActionEvent> require_different_languages(StringProperty source, StringProperty target) {
		return event -> {
			String lang = source.get();
			if (lang != null && lang.equals(target.get()))
				target.set(null);
		};
	}

	private WordSet wordset;

	private final StringProperty name, langA, langB;
	private final TableView<Word> table;
	private final Scene scene;
	
	public WordSetEditor(WordSet w, Consumer<WordSet> trainEvent, EventHandler<ActionEvent> closeEvent) {
		this(trainEvent, closeEvent);
		setWordSet(w);
	}

	@SuppressWarnings("unchecked")
	public WordSetEditor(Consumer<WordSet> trainEvent, EventHandler<ActionEvent> closeEvent) {

		name = new SimpleStringProperty();
		langA = new SimpleStringProperty();
		langB = new SimpleStringProperty();
		
		// Header
		Text title = new Text();
		title.textProperty().bind(name);
		title.setFont(new Font(title.getFont().getName(), 28));
		HBox header = new HBox(title);
		header.setAlignment(Pos.CENTER);
		
		// Change set name
		TextField nameInputField = new TextField();
		nameInputField.setPromptText("Set name");
		nameInputField.textProperty().bindBidirectional(name);
		Label nameLabel = new Label("Set name:", nameInputField);
		nameLabel.setContentDisplay(ContentDisplay.RIGHT);
		HBox nameInput = new HBox(nameLabel, nameInputField);
		nameInput.setSpacing(5);
		
		// Language selectors
		ObservableList<String> languages = FXCollections.observableList(Arrays.asList("DE", "EN", "IT", "FR"));
		ComboBox<String> langAInput = new ComboBox<>(languages),
				         langBInput = new ComboBox<>(languages);
		langAInput.valueProperty().bindBidirectional(langA);
		langBInput.valueProperty().bindBidirectional(langB);
		langAInput.setOnAction(require_different_languages(langA, langB));
		langBInput.setOnAction(require_different_languages(langB, langA));
		Label langALabel = new Label("Language 1:", langAInput),
			  langBLabel = new Label("Language 2:", langBInput);
		langALabel.setContentDisplay(ContentDisplay.RIGHT);
		langBLabel.setContentDisplay(ContentDisplay.RIGHT);
		HBox langInput = new HBox(langALabel, langAInput, langBLabel, langBInput);
		langInput.setSpacing(5);
		
		// Word set table
		table = new TableView<>();
		table.setEditable(true);
		
		// Word columns
		TableColumn<Word, String> columnA = new TableColumn<>(),
				                  columnB = new TableColumn<>();
		columnA.textProperty().bind(langA);
		columnB.textProperty().bind(langB);
		columnA.setCellValueFactory(cell -> cell.getValue().aProperty());
		columnB.setCellValueFactory(cell -> cell.getValue().bProperty());
		columnA.setCellFactory(TextFieldTableCell.forTableColumn());
		columnB.setCellFactory(TextFieldTableCell.forTableColumn());
		columnA.setMinWidth(150);
		columnB.setMinWidth(150);

		// Remove column
		TableColumn<Word, Button> remove = new TableColumn<>("Remove");
		remove.setCellFactory(ButtonTableCell.forTableColumn("Remove", word -> table.getItems().remove(word)));

		// Assemble table
		table.getColumns().addAll(columnA, columnB, remove);

		// Add words
		TextField inputA = new TextField(),
				  inputB = new TextField();
		inputA.promptTextProperty().bind(langA);
		inputB.promptTextProperty().bind(langB);
		Button addButton = new Button("Add");
		addButton.setOnAction(evt -> {
			if (addWord(inputA.getText(), inputB.getText())) {
				inputA.clear();
				inputB.clear();
			}
		});
		HBox add = new HBox(inputA, inputB, addButton);
		add.setSpacing(5);

		// Commands
		Button save  = new Button("Save & Close"),
			   close = new Button("Close"),
			   reset = new Button("Reset"),
			   train = new Button("Save & Train");
		close.setOnAction(closeEvent);
		save.setOnAction(event -> {
			if (saveWordSet()) {
				setWordSet(null);
				closeEvent.handle(event);
			}
		});
		reset.setOnAction(event -> resetWordSet());
		train.setOnAction(event -> {
			if (saveWordSet()) {
				WordSet w = wordset;
				setWordSet(null);
				trainEvent.accept(w);
			}
		});
		HBox commands = new HBox(reset, close, save, train);
		commands.setSpacing(10);
		commands.setAlignment(Pos.CENTER);

		// Assemble scene
		VBox layout = new VBox(header, nameInput, langInput, table, add, commands);
		layout.setSpacing(10);
		layout.setPadding(new Insets(10));
		scene = new Scene(layout);
	}

	public void setWordSet(WordSet w) {
		if (w == null) {
			if (wordset == null) return;
			
			// Unbind previous word set from view.
			table.setItems(null);
			name.unbindBidirectional(wordset.nameProperty());
			langA.unbindBidirectional(wordset.langAProperty());
			langB.unbindBidirectional(wordset.langBProperty());
			wordset = null;
		} else {
			
			// Bind new word set data to view.
			wordset = w;
			table.setItems(wordset.getWords());
			name.bindBidirectional(wordset.nameProperty());
			langA.bindBidirectional(wordset.langAProperty());
			langB.bindBidirectional(wordset.langBProperty());
		}
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public String getTitle() {
		return "Edit word set";
	}

	// Controller for add button.
	private boolean addWord(String a, String b) {
		if (a == null || b == null || a.isBlank() || b.isBlank())
			return false;
		Word word = new Word(a.trim(), b.trim());
		List<Word> words = wordset.getWords();
		if (!words.contains(word))
			words.add(word);
		return true;
	}

	// Controller for save button.
	private boolean saveWordSet() {
		if (!wordset.isValid())
			return false;
		try {
			wordset.writeFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Controller for reset button.
	private void resetWordSet() {
		try {
			wordset.readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
