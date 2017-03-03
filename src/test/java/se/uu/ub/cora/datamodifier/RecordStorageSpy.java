package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);

	}

	@Override
	public Collection<DataGroup> readList(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
