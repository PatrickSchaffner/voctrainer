package io.zauberberg.voctrainer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class WordTest {
	
	@Test
	void testDefaultConstructor() {
		Word w = new Word();
		assertEquals("", w.getA());
		assertEquals("", w.getB());
		assertNotNull(w.aProperty());
		assertNotNull(w.bProperty());
		assertSame(w.getA(), w.aProperty().get());
		assertSame(w.getB(), w.bProperty().get());
	}
	
	@Test
	void testFullConstructor() {
		Word w = new Word("a","b");
		assertEquals("a", w.getA());
		assertEquals("b", w.getB());
		assertNotNull(w.aProperty());
		assertNotNull(w.bProperty());
		assertSame(w.getA(), w.aProperty().get());
		assertSame(w.getB(), w.bProperty().get());
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsAndHashcode() {
		Word x = new Word("a","b"), y = new Word("a", "b");
		assertTrue(x.equals(x));
		assertFalse(x.equals("not a word"));
		Supplier<Boolean> eq = () -> x.equals(y);
		Supplier<Boolean> hc = () -> x.hashCode() == y.hashCode();
		assertTrue(eq.get());
		assertTrue(hc.get());
		x.setA("0");
		assertFalse(eq.get());
		assertFalse(hc.get());
		y.setB("1");
		assertFalse(eq.get());
		assertFalse(hc.get());
		y.setA("0");
		assertFalse(eq.get());
		assertFalse(hc.get());
		x.setB("1");
		assertTrue(eq.get());
		assertTrue(hc.get());
	}
	
	@Test
	void testSettersAndProperties() {
		Word w = new Word("a","b");
		BiConsumer<String,String> assertContent = (a,b) -> {
			assertEquals(a, w.getA());
			assertSame(a, w.aProperty().get());
			assertEquals(b, w.getB());
			assertSame(b, w.bProperty().get());
		};
		
		w.setA("x");
		assertContent.accept("x", "b");
		
		w.setB("y");
		assertContent.accept("x", "y");
		
		w.aProperty().set("a");
		assertContent.accept("a", "y");
		
		w.bProperty().set("b");
		assertContent.accept("a", "b");
	}
}
