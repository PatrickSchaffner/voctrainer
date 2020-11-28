package io.zauberberg.voctrainer.views;

import io.zauberberg.voctrainer.model.WordSet;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class WordSetTrainer implements View {
	
	private final WordSet wordset;
	private final Scene scene;
	
	public WordSetTrainer(WordSet w) {
		wordset = w;
		
		// TODO
		
		scene = new Scene(new BorderPane(new Text("Hullu "+wordset.getName())));
	}

	@Override
	public Scene getScene() {
		return scene;
	}
	
	@Override
	public String getTitle() {
		return "Training "+wordset.getName();
	}

}
