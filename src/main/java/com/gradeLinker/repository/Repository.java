package com.gradeLinker.repository;

public interface Repository<T> {
    T getById(String id);
    boolean existsById(String id);

    void save(String id, T object);

    boolean deleteById(String id);
}