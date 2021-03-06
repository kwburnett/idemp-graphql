package org.bandahealth.idempiere.graphql.utils;

import org.bandahealth.idempiere.graphql.function.VoidFunction;
import org.compiere.model.PO;

import java.util.List;
import java.util.function.Function;

/**
 * A utility class to work with iDempiere models
 */
public class ModelUtil {
	/**
	 * The delimiter to use when creating model keys (which are used in the cache)
	 */
	private static final String keyDelimiter = ".";

	/**
	 * A simplified method to set a property on an entity if it's present on another entity, typically an input model
	 *
	 * @param propertyValue  The property value to set, if it's not null
	 * @param propertySetter The setter method on the entity, if the property is present
	 * @param <T>            The type of the property
	 */
	public static <T> void setPropertyIfPresent(T propertyValue, VoidFunction<T> propertySetter) {
		if (propertyValue == null) {
			return;
		}
		if (propertyValue instanceof String && !StringUtil.isNullOrEmpty(propertyValue.toString())) {
			propertySetter.apply(propertyValue);
		} else if (propertyValue instanceof List<?> && !((List<?>) propertyValue).isEmpty()) {
			propertySetter.apply(propertyValue);
		} else {
			propertySetter.apply(propertyValue);
		}
	}

	/**
	 * Generate a model key specific to this entity
	 *
	 * @param entity The entity to which the property applies
	 * @param id     The ID property to use
	 * @param <T>    The model type, extending from an iDempiere PO
	 * @return A key to supply to the cache to ensure no collisions occur (such as when using primary keys and foreign
	 * keys to fetch data)
	 */
	public static <T extends PO> String getModelKey(T entity, Integer id) {
		return getModelKey(entity.get_TableName(), id);
	}

	/**
	 * Generate a model key specific to this entity
	 *
	 * @param modelName The model's name to which the property applies
	 * @param id        The ID property to use
	 * @return A key to supply to the cache to ensure no collisions occur (such as when using primary keys and foreign
	 * keys to fetch data)
	 */
	public static String getModelKey(String modelName, Integer id) {
		return modelName + keyDelimiter + id.toString();
	}

	/**
	 * Get the ID value that was part of the key
	 *
	 * @param key The key generated by getModelKey above
	 * @return The ID to use in DB queries
	 */
	public static Integer getIdFromKey(String key) {
		return Integer.valueOf(key.split("\\" + keyDelimiter)[1]);
	}

	/**
	 * The model name that was used for this key
	 *
	 * @param key The key generated by getModelKey above
	 * @return The model name that was used for this key
	 */
	public static String getModelFromKey(String key) {
		return key.split("\\" + keyDelimiter)[0];
	}
}
