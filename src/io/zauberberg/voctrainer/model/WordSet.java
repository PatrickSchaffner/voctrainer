package io.zauberberg.voctrainer.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collections;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WordSet {

	private final ObservableList<Word> words = FXCollections.observableArrayList();
	private final StringProperty name, langA, langB;
	private String filename;
	
	public WordSet() {
		name = new SimpleStringProperty();
		langA = new SimpleStringProperty();
		langB = new SimpleStringProperty();
	}
	
	public WordSet(String filename) throws IOException {
		this();
		readFile(filename);
	}
	
	public WordSet(String name, String a, String b) {
		this();
		setName(name);
		setLangA(a);
		setLangB(b);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public String getLangA() {
		return langA.get();
	}
	
	public void setLangA(String a) {
		langA.set(a);
	}
	
	public StringProperty langAProperty() {
		return langA;
	}
	
	public String getLangB() {
		return langB.get();
	}
	
	public void setLangB(String b) {
		langB.set(b);
	}
	
	public StringProperty langBProperty() {
		return langB;
	}
	
	public ObservableList<Word> getWords() {
		return words;
	}
	
	public void readFile() throws IOException {
		readFile(null);
	}
	
	public boolean isValid() {
		if (getLangA().equals(getLangB()) || getName().isBlank()) return false;
		boolean valid = true;
		for (Word w : words)
			if (w.getA().isBlank() || w.getB().isBlank()) valid = false;
		return valid;
	}
	
	public void readFile(String file) throws IOException {
		if (file==null) file = filename;
		else filename = file;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			setName(in.readLine().trim());
			String[] lang = in.readLine().split(",");
			setLangA(lang[0].trim().toUpperCase());
			setLangB(lang[1].trim().toUpperCase());
			words.clear();
			String line;
			while ((line = in.readLine())!=null) {
				String[] word = line.split(",");
				words.add(new Word(word[0].trim(), word[1].trim()));
			}
		}
	}
	
	public void writeFile() throws IOException {
		writeFile(null);
	}
	
	public void writeFile(String file) throws IOException {
		if (file==null) file = filename;
		else filename = file;
		try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
			out.println(getName());
			out.println(getLangA()+","+getLangB());
			for (Word w : words) out.println(w.getA()+","+w.getB());
		}
	}
	
	public void randomOrdering() {
		Collections.shuffle(words);
	}
}
