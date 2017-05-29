package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class DataGroupModifierRunnerTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Constructor<DataGroupModifierRunner> constructor = DataGroupModifierRunner.class
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

		DataGroupModifierRunner.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) DataGroupModifierRunner.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataGroup");
	}

	@Test
	public void testMainMethodForCollectionItem()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy",
				"metadataCollectionItem" };

		DataGroupModifierRunner.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) DataGroupModifierRunner.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataCollectionItem");
	}

	@Test
	public void testMainMethodForMetadataGroup()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy", "metadataGroup" };

		DataGroupModifierRunner.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) DataGroupModifierRunner.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataGroup");
	}
}
