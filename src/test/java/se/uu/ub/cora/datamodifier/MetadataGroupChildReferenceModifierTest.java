package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.testng.annotations.Test;

public class MetadataGroupChildReferenceModifierTest {

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy" };

		MetadataGroupChildReferenceModifier.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy) MetadataGroupChildReferenceModifier.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataGroup");
	}
}
