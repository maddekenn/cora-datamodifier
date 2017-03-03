package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollectorImp;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.spider.record.storage.RecordStorage;
import se.uu.ub.cora.storage.RecordStorageOnDisk;

public class MetadataGroupChildReferenceModifier {

	public static void main(String[] args) {

		RecordStorage recordStorage = RecordStorageOnDisk
				.createRecordStorageOnDiskWithBasePath("/home/madde/workspace/modify/");
		DataRecordLinkCollector linkCollector = new DataRecordLinkCollectorImp(
				(MetadataStorage) recordStorage);
		DataModifier dataModifier = new DataModifier(recordStorage, linkCollector);
		dataModifier.modifyByRecordType("metadataGroup");
		System.out.println("done");
	}
}
