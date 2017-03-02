package se.uu.ub.se.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.data.DataPart;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;
import se.uu.ub.cora.storage.data.converter.JsonToDataConverter;
import se.uu.ub.cora.storage.data.converter.JsonToDataConverterFactory;
import se.uu.ub.cora.storage.data.converter.JsonToDataConverterFactoryImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class JsonModifier {
    String basePath;
    String filePrefix;
    Map<String, DividerGroup> dataGroupList = new HashMap<>();
    Map<String, Map<String, DividerGroup>> recordList = new HashMap<>();


    protected void readAndTransformJson(String filePrefix) {
        this.filePrefix = filePrefix;
        Stream<Path> list;
        try {
            list = Files.list(Paths.get(basePath));
            Iterator<Path> iterator = list.iterator();
            while (iterator.hasNext()) {
                readFileIfNotDirectory(iterator);
            }
            list.close();
            transformJSON();
            recordList.put(filePrefix, dataGroupList);

        } catch (IOException e) {
            throw DataStorageException.withMessage("can not read files from disk on init" + e);
        }
    }

    protected void readFileIfNotDirectory(Iterator<Path> iterator) throws IOException {
        Path path = iterator.next();
        if (!Files.isDirectory(path)) {
            readFileAndParseFileByPath(path);
        }
    }

    protected void readFileAndParseFileByPath(Path path) throws IOException {
        String fileNameTypePart = getTypeFromPath(path);

        if(filePrefix.equals(fileNameTypePart)){

            String dataDivider = getDataDividerFromPath(path);
            List<DataElement> recordTypes = extractChildrenFromFileByPath(path);
            parseAndStoreRecordsInMemory(fileNameTypePart, dataDivider, recordTypes);
        }
    }

    protected String getTypeFromPath(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf('_'));
    }

    protected String getDataDividerFromPath(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.substring(fileName.lastIndexOf('_') + 1, fileName.indexOf('.'));
    }

    protected List<DataElement> extractChildrenFromFileByPath(Path path) throws IOException {
        String json = readJsonFileByPath(path);
        DataGroup records = convertJsonStringToDataGroup(json);
        return records.getChildren();
    }

    protected String readJsonFileByPath(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line;
        StringBuilder jsonBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        return jsonBuilder.toString();
    }

    protected DataGroup convertJsonStringToDataGroup(String jsonRecord) {
        JsonParser jsonParser = new OrgJsonParser();
        JsonValue jsonValue = jsonParser.parseString(jsonRecord);
        JsonToDataConverterFactory jsonToDataConverterFactory = new JsonToDataConverterFactoryImp();
        JsonToDataConverter jsonToDataConverter = jsonToDataConverterFactory
                .createForJsonObject(jsonValue);
        DataPart dataPart = jsonToDataConverter.toInstance();
        return (DataGroup) dataPart;
    }

    protected void parseAndStoreRecordsInMemory(String fileNameTypePart, String dataDivider,
                                              List<DataElement> recordTypes) {

        for (DataElement dataElement : recordTypes) {
            parseAndStoreRecordInMemory(fileNameTypePart, dataDivider, dataElement);
        }
    }

    protected void parseAndStoreRecordInMemory(String fileNameTypePart, String dataDivider,
                                             DataElement dataElement) {
        DataGroup record = (DataGroup) dataElement;

        DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
        String recordId = recordInfo.getFirstAtomicValueWithNameInData("id");

        storeRecordByRecordTypeAndRecordId(fileNameTypePart, recordId, record, dataDivider);
    }

    protected void storeRecordByRecordTypeAndRecordId(String recordType, String recordId,
                                                      DataGroup recordIndependentOfEnteredRecord, String dataDivider) {
        dataGroupList.put(recordId, DividerGroup.withDataDividerAndDataGroup(dataDivider,
                recordIndependentOfEnteredRecord));
    }

    protected abstract void transformJSON();
}
