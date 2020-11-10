package org.bandahealth.idempiere.graphql.model;

/**
 * The available statuses of an order. This is the Java version of what's defined in order-status.graphqls
 */
public enum OrderStatus {
	WAITING, DISPENSING, PENDING, PENDING_COMPLETION, COMPLETED
}
