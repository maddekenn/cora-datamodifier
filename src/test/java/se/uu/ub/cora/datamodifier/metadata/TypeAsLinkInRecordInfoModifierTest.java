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
		Constructor<AllRecordTypesModifier> constructor = AllRecordTypesModifier.class
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
				"se.uu.ub.cora.datamodifier.metadata.ModifierForLinkedTypeInRecordInfoSpy",
				"se.uu.ub.cora.datamodifier.RecordStorageProviderSpy"};

		AllRecordTypesModifier.main(args);

		ModifierForLinkedTypeInRecordInfoSpy recordTypeModifier = (ModifierForLinkedTypeInRecordInfoSpy) AllRecordTypesModifier.dataModifier;
		assertEquals(recordTypeModifier.recordTypes.get(0), "book");
		assertEquals(recordTypeModifier.recordTypes.get(1), "place");
		assertEquals(recordTypeModifier.recordTypes.size(), 2);
	}

}
