package io.zauberberg.voctrainer;

import java.io.File;
import java.io.IOException;

import io.zauberberg.voctrainer.model.WordSet;
import io.zauberberg.voctrainer.views.View;
import io.zauberberg.voctrainer.views.WordSetChooser;
import io.zauberberg.voctrainer.views.WordSetEditor;
import io.zauberberg.voctrainer.views.WordSetTrainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private Stage stage = null;

	@Override
	public void start(Stage primary) throws IOException {
		stage = primary;
		stage.setOnCloseRequest(onClose -> exit());
		chooseWordSet();
		stage.show();
	}

	private void showView(View view) {
		stage.setScene(view.getScene());
		stage.setTitle(view.getTitle());
	}

	private void exit() {
		Platform.exit();
		System.exit(0);
	}
	
	private void chooseWordSet() {
		showView(new WordSetChooser(this::chooseAndLoadFile, this::editWordSet, this::startTraining));
	}

	private void editWordSet(WordSet wordset) {
		showView(new WordSetEditor(wordset, this::startTraining, onClose -> exit()));
	}

	private void startTraining(WordSet wordset) {
		showView(new WordSetTrainer(wordset));
	}
	
	private WordSet chooseAndLoadFile() {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word sets","*.words"));
		chooser.setInitialDirectory(new File("./vocabulary/"));
		File file = chooser.showOpenDialog(stage);
		if (file != null && file.exists()) {
			WordSet wordset;
			try {
				wordset = new WordSet(file.getPath());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return wordset;
		} else return null;
	}
}
