package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class MetadataGroupChildReferenceModifierTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<MetadataGroupChildReferenceModifier> constructor = MetadataGroupChildReferenceModifier.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy", "metadataGroup" };

		MetadataGroupChildReferenceModifier.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) MetadataGroupChildReferenceModifier.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataGroup");
	}

	@Test
	public void testMainMethodForCollectionItem()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy",
				"metadataCollectionItem" };

		MetadataGroupChildReferenceModifier.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) MetadataGroupChildReferenceModifier.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataCollectionItem");
	}
}
