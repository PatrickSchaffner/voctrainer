package io.zauberberg.voctrainer.views;

import javafx.scene.Scene;

public interface View {

	public Scene getScene();
	
	public default String getTitle() {
		return "Vokabeltrainer";
	};
}
