package org.bandahealth.idempiere.graphql.function;

@FunctionalInterface
public interface VoidFunction<T> {
	void apply(T var1);
}
