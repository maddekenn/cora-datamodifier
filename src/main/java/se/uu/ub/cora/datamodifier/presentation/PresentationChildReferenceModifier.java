package se.uu.ub.cora.datamodifier.presentation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollectorImp;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;

public class PresentationChildReferenceModifier {
	protected static DataModifier dataModifier;

	private PresentationChildReferenceModifier() {

	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String basePath = args[0];
		String modifierClassName = args[1];
		RecordStorage recordStorage = RecordStorageOnDisk
				.createRecordStorageOnDiskWithBasePath(basePath);
		DataRecordLinkCollector linkCollector = new DataRecordLinkCollectorImp(
				(MetadataStorage) recordStorage);

		Constructor<?> constructor = Class.forName(modifierClassName).getConstructor();
		dataModifier = (DataModifier) constructor.newInstance();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);

		dataModifier.modifyByRecordType("presentationGroup");
		dataModifier.modifyByRecordType("presentationSurroundingContainer");
		dataModifier.modifyByRecordType("presentationRepeatingContainer");
		dataModifier.modifyByRecordType("presentationResourceLink");
		System.out.println("done");
	}
}
