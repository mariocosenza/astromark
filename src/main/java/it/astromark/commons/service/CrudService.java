package it.astromark.commons.service;

/**
 * Generic service interface for managing CRUD (Create, Read, Update, Delete) operations.
 *
 * @param <T>  the entity type
 * @param <R>  the request type for creating or updating an entity
 * @param <RS> the response type for returning entity details
 * @param <ID> the identifier type used for the entity
 */
public interface CrudService<T, R, RS, ID> {
    /**
     * Creates a new resource.
     *
     * @param r the resource object to be created
     * @return an `RS` object representing the created resource
     * Pre-condition: The `r` must not be null and must contain valid data for creation.
     * Post-condition: A new resource is created and returned as an `RS` object.
     */
    RS create(R r);

    /**
     * Updates an existing resource by its ID.
     *
     * @param id the ID of the resource to be updated
     * @param r  the updated resource data
     * @return an `RS` object representing the updated resource
     * Pre-condition: The `id` must not be null and must refer to an existing resource. The `r` must not be null and must contain valid data for updating.
     * Post-condition: The resource is updated and returned as an `RS` object.
     */
    RS update(ID id, R r);

    /**
     * Deletes a resource by its ID.
     *
     * @param id the ID of the resource to be deleted
     * @return true if the resource was successfully deleted, false otherwise
     * Pre-condition: The `id` must not be null and must refer to an existing resource.
     * Post-condition: The resource is deleted if it exists, and the method returns true.
     */
    boolean delete(ID id);

    /**
     * Retrieves a resource by its ID.
     *
     * @param id the ID of the resource
     * @return a `T` object representing the resource, or null if not found
     * Pre-condition: The `id` must not be null and must refer to an existing resource.
     * Post-condition: Returns the resource as a `T` object if it exists, otherwise null.
     */
    T getById(ID id);

}
