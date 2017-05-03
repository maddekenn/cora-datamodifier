package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class TypeAsLinkInRecordInfoModifierTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<TypeAsLinkInRecordInfoModifier> constructor = TypeAsLinkInRecordInfoModifier.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethodWithLinkedTypeModifier()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.metadata.ModifierForLinkedTypeInRecordInfoSpy" };

		TypeAsLinkInRecordInfoModifier.main(args);
		ModifierForLinkedTypeInRecordInfoSpy recordTypeModifier = (ModifierForLinkedTypeInRecordInfoSpy) TypeAsLinkInRecordInfoModifier.dataModifier;
		assertEquals(recordTypeModifier.recordTypes.get(0), "presentationGroup");
		assertEquals(recordTypeModifier.recordTypes.get(1), "presentationSurroundingContainer");
		assertEquals(recordTypeModifier.recordTypes.get(2), "presentationRepeatingContainer");
		assertEquals(recordTypeModifier.recordTypes.get(3), "presentationResourceLink");
	}

	// @Test
	// public void testMainMethodWithStyleModifier()
	// throws ClassNotFoundException, NoSuchMethodException,
	// InvocationTargetException,
	// InstantiationException, IllegalAccessException {
	// String args[] = new String[] { "/home/madde/workspace/modify/",
	// "se.uu.ub.cora.datamodifier.presentation.ModifierForPresentationChildRefStyleSpy",
	// "false" };
	//
	// PresentationChildReferenceModifier.main(args);
	// ModifierForPresentationChildRefStyleSpy recordTypeModifier =
	// (ModifierForPresentationChildRefStyleSpy)
	// PresentationChildReferenceModifier.dataModifier;
	// assertEquals(recordTypeModifier.recordTypes.get(0), "presentationGroup");
	// assertEquals(recordTypeModifier.recordTypes.size(), 1);
	// }
	//
	// @Test
	// public void testMainMethodWithDefaultPresentationModifier()
	// throws ClassNotFoundException, NoSuchMethodException,
	// InvocationTargetException,
	// InstantiationException, IllegalAccessException {
	// String args[] = new String[] { "/home/madde/workspace/modify/",
	// "se.uu.ub.cora.datamodifier.presentation.ModifierForChildRefDefaultPresentationSpy",
	// "true" };
	//
	// PresentationChildReferenceModifier.main(args);
	// ModifierForChildRefDefaultPresentationSpy recordTypeModifier =
	// (ModifierForChildRefDefaultPresentationSpy)
	// PresentationChildReferenceModifier.dataModifier;
	// assertEquals(recordTypeModifier.recordTypes.get(0), "presentationGroup");
	// assertEquals(recordTypeModifier.recordTypes.get(1),
	// "presentationSurroundingContainer");
	// assertEquals(recordTypeModifier.recordTypes.get(2),
	// "presentationRepeatingContainer");
	// assertEquals(recordTypeModifier.recordTypes.get(3),
	// "presentationResourceLink");
	// }
}
