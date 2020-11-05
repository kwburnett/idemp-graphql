package org.bandahealth.idempiere.graphql.utils;

import org.bandahealth.idempiere.graphql.function.VoidFunction;
import org.compiere.model.PO;

import java.util.List;
import java.util.function.Function;

public class ModelUtil {

	private static String keyDelimiter = ".";

	public static <T> void setPropertyIfPresent(T property, VoidFunction<T> propertySetter) {
		if (property == null) {
			return;
		}
		if (property instanceof String && !StringUtil.isNullOrEmpty(property.toString())) {
			propertySetter.apply(property);
		} else if (property instanceof List<?> && !((List<?>) property).isEmpty()) {
			propertySetter.apply(property);
		} else {
			propertySetter.apply(property);
		}
	}

	public static <T extends PO> String getModelKey(T entity, Integer property) {
		return getModelKey(entity.get_TableName(), property);
	}

	public static String getModelKey(String modelName, Integer property) {
		return modelName + keyDelimiter + property.toString();
	}

	public static Integer getPropertyFromKey(String key) {
		return Integer.valueOf(key.split("\\" + keyDelimiter)[1]);
	}

	public static String getModelFromKey(String key) {
		return key.split("\\" + keyDelimiter)[0];
	}
}
