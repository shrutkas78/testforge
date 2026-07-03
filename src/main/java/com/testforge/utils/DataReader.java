package com.testforge.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads JSON test data files from src/test/resources/testdata
 * and converts them into TestNG DataProvider-friendly Object[][] arrays.
 */
public final class DataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DataReader() {
    }

    /**
     * Reads a JSON array of objects and returns each object's field values
     * (in the given field order) as one Object[] row.
     */
    public static Object[][] readJsonAsDataProvider(String fileName, String... fields) {
        try (InputStream is = DataReader.class.getClassLoader()
                .getResourceAsStream("testdata/" + fileName)) {
            if (is == null) {
                throw new IllegalStateException("Test data file not found: testdata/" + fileName);
            }
            JsonNode root = MAPPER.readTree(is);
            List<Object[]> rows = new ArrayList<>();
            for (JsonNode node : root) {
                Object[] row = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    row[i] = node.get(fields[i]).asText();
                }
                rows.add(row);
            }
            return rows.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read test data: " + fileName, e);
        }
    }
}
