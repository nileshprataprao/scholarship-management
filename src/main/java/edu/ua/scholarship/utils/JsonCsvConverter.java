package edu.ua.scholarship.utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonCsvConverter {

    private static InputStreamResource convertJsonToCsv(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> input = mapper.readValue(json, new TypeReference<>() {
        });


        // Flatten the maps
        List<Map<String, Object>> flattened = new ArrayList<>();
        for (Map<String, Object> item : input) {
            Map<String, Object> flatMap = new LinkedHashMap<>();
            flatten(item, "", flatMap);
            flattened.add(flatMap);
        }

        // Write to CSV
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema.Builder schemaBuilder = CsvSchema.builder();
        if (!flattened.isEmpty()) {
            for (String col : flattened.get(0).keySet()) {
                schemaBuilder.addColumn(col);
            }
        }

        CsvSchema schema = schemaBuilder.build().withHeader();

        // Write CSV to in-memory stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        csvMapper.writer(schema).writeValue(baos, flattened);
        return new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));

    }

    private static void flatten(Map<String, Object> map, String prefix, Map<String, Object> out) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "_" + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flatten((Map<String, Object>) value, key, out);
            } else {
                out.put(key, value);
            }
        }
    }

    public static ResponseEntity<InputStreamResource> responseEntityAsCSV(String studentsStr, String fileName) throws IOException {
        InputStreamResource resource = JsonCsvConverter.convertJsonToCsv(studentsStr);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("csv/text;charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}