package org.bandahealth.idempiere.graphql.utils;

import org.bandahealth.idempiere.graphql.function.VoidFunction;

import java.util.List;

public class ModelUtil {

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
}
