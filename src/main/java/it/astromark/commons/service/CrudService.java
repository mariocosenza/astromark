package it.astromark.commons.service;

public interface CrudService<T, R, RS, ID> {
    RS create(R r);
    RS update(ID id, R r);
    void delete(ID id);
    T getById(ID id);
}
