package se.uu.ub.cora.datamodifier.presentation;

import org.testng.annotations.Test;
import se.uu.ub.cora.datamodifier.DataModifierForRecordTypeSearchLinkSpy;
import se.uu.ub.cora.datamodifier.RecordTypeSearchLinkModifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PresentationChildReferenceModifierTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<PresentationChildReferenceModifier> constructor =  PresentationChildReferenceModifier.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.presentation.DataModifierForPresentationChildReferenceSpy" };

		PresentationChildReferenceModifier.main(args);
		DataModifierForPresentationChildReferenceSpy recordTypeModifier =
				(DataModifierForPresentationChildReferenceSpy) PresentationChildReferenceModifier.dataModifier;
		assertEquals(recordTypeModifier.recordType, "presentationGroup");
	}
}
