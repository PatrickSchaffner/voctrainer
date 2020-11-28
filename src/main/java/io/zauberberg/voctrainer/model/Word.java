package io.zauberberg.voctrainer.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Word {

	private final StringProperty a, b;

	public Word() {
		a = new SimpleStringProperty("");
		b = new SimpleStringProperty("");
	}

	public Word(String a, String b) {
		this();
		setA(a);
		setB(b);
	}

	public String getA() {
		return a.get();
	}

	public void setA(String a) {
		this.a.set(a);
	}

	public StringProperty aProperty() {
		return a;
	}

	public String getB() {
		return b.get();
	}

	public void setB(String b) {
		this.b.set(b);
	}

	public StringProperty bProperty() {
		return b;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Word))
			return false;
		Word other = (Word) obj;
		return getA().equals(other.getA()) && getB().equals(other.getB());
	}

	@Override
	public int hashCode() {
		return 3 - getA().hashCode() * (12 + getB().hashCode());
	}
	
	
}
