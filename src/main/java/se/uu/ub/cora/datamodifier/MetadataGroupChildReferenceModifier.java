package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollectorImp;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;
import se.uu.ub.cora.userpicker.UserPickerProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MetadataGroupChildReferenceModifier {

	protected static DataModifier dataModifier;

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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


		dataModifier.modifyByRecordType("metadataGroup");
		System.out.println("done");
	}
}
