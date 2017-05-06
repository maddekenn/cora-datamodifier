package se.uu.ub.cora.datamodifier.metadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollectorImp;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.datamodifier.RecordStorageProvider;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class TypeAsLinkInRecordInfoModifier {
	protected static DataModifier dataModifier;

	private TypeAsLinkInRecordInfoModifier() {
	}

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException {
		String basePath = args[0];
		String modifierClassName = args[1];
		String recordStorageProviderClassName = args[2];

		RecordStorage recordStorage = getRecordStorage(basePath, recordStorageProviderClassName);

		DataRecordLinkCollector linkCollector = new DataRecordLinkCollectorImp(
				(MetadataStorage) recordStorage);

		createModifier(modifierClassName, recordStorage, linkCollector);

		modifyAllRecordTypes(recordStorage);
		System.out.println("done");
	}

	private static RecordStorage getRecordStorage(String basePath,
			String recordStorageProviderClassName)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> storageProviderConstructor = Class.forName(recordStorageProviderClassName)
				.getConstructor();
		RecordStorageProvider recordStorageProvider = (RecordStorageProvider) storageProviderConstructor
				.newInstance();
		return recordStorageProvider.getRecordStorageWithBasePath(basePath);
	}

	private static void createModifier(String modifierClassName, RecordStorage recordStorage,
			DataRecordLinkCollector linkCollector)
			throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(modifierClassName).getConstructor();
		dataModifier = (DataModifier) constructor.newInstance();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	private static void modifyAllRecordTypes(RecordStorage recordStorage) {
		Collection<DataGroup> recordTypes = recordStorage.readList("recordType");
		for (DataGroup recordType : recordTypes) {
			if (recordTypeIsNotAbstract(recordType)) {
				DataGroup recordInfo = recordType.getFirstGroupWithNameInData("recordInfo");
				String id = recordInfo.getFirstAtomicValueWithNameInData("id");
				System.out.println("starting recordType " + id);
				dataModifier.modifyByRecordType(id);
				System.out.println("finished recordType " + id);
			}
		}
	}

	private static boolean recordTypeIsNotAbstract(DataGroup recordType) {
		String abstractValue = recordType.getFirstAtomicValueWithNameInData("abstract");
		return "false".equals(abstractValue);
	}
}
