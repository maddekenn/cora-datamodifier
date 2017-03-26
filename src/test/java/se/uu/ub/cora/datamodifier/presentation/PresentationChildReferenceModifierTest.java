package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class PresentationChildReferenceModifierTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<PresentationChildReferenceModifier> constructor = PresentationChildReferenceModifier.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.presentation.ModifierForPresentationChildRefLinkedTypeSpy" };

		PresentationChildReferenceModifier.main(args);
		ModifierForPresentationChildRefLinkedTypeSpy recordTypeModifier = (ModifierForPresentationChildRefLinkedTypeSpy) PresentationChildReferenceModifier.dataModifier;
		assertEquals(recordTypeModifier.recordTypes.get(0), "presentationGroup");
		assertEquals(recordTypeModifier.recordTypes.get(1), "presentationSurroundingContainer");
		assertEquals(recordTypeModifier.recordTypes.get(2), "presentationRepeatingContainer");
		assertEquals(recordTypeModifier.recordTypes.get(3), "presentationResourceLink");
	}
}
