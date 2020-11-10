package org.bandahealth.idempiere.graphql.function;

/**
 * Java provides a Function interface, but it needs two arguments and void is not allowed. So, this allows for void
 * functions
 *
 * @param <T> The type of the needed argument
 */
@FunctionalInterface
public interface VoidFunction<T> {
	void apply(T var1);
}
