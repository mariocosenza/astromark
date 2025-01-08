package it.astromark.commons.service;

public interface CrudService<T, R, RS, ID> {
    RS create(R r);
    RS update(ID id, R r);
    RS delete(ID id);
    RS getById(ID id);
}
