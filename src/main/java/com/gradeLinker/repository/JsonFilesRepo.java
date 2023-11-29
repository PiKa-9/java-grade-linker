package com.gradeLinker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* Storing objects in files(located in 'dirPath')
   The name of file - id(string) of object */
public class JsonFilesRepo<T> implements Repository<T> {
    private final String dirPath;
    private final ObjectMapper jsonMapper;
    private final TypeReference<? extends T> jsonTypeReference;
    public JsonFilesRepo(String dirPath, TypeReference<? extends T> jsonTypeReference) {
        this.dirPath = dirPath;
        this.jsonTypeReference = jsonTypeReference;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }


    @Override
    public T getById(String id) {
        File file = new File(dirPath + "_" + id + ".json");
        try {
            return jsonMapper.readValue(file, jsonTypeReference);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean existsById(String id) {
        File file = new File(dirPath + "_" + id + ".json");
        return file.exists();
    }

    @Override
    public void save(String id, T object) {
        File file = new File(dirPath + "_" + id + ".json");
        synchronized (file) {
            try {
                jsonMapper.writeValue(file, object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean deleteById(String id) {
        File file = new File(dirPath + "_" + id + ".json");
        return file.delete();
    }
}
