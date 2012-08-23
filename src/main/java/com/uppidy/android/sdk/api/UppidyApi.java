package com.uppidy.android.sdk.api;

import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

/**
 * Defines low-level operations against Uppidy's API
 * 
 * @author arudnev@uppidy.com
 */
public interface UppidyApi {

	/**
	 * Fetches an object, extracting it into the given Java type.
	 * 
	 * Requires appropriate permission to fetch the object.
	 * 
	 * @param objectId
	 *            the Uppidy object's ID
	 * @param type
	 *            the Java type to fetch
	 * @param queryParams
	 *            query parameters to include in the request
	 *            
	 * @return an Java object representing the requested Uppidy object.
	 */
	<T> T fetchObject(String objectId, Class<T> type, MultiValueMap<String, String> queryParams);

	/**
	 * Fetches connections, extracting them into a collection of the given Java
	 * type Requires appropriate permission to fetch the object connection.
	 * 
	 * @param objectId
	 *            the ID of the object to retrieve the connections for.
	 * @param connectionName
	 *            the connection name.
	 * @param type
	 *            the Java type of each connection.
	 * @param queryParams
	 *            query parameters to include in the request
	 *            
	 * @return a list of Java objects representing the Uppidy objects in the
	 *         connections.
	 */
	<T> List<T> fetchConnections(String objectId, String connectionName, Class<T> type, MultiValueMap<String, String> queryParams);

	/**
	 * Fetches an image as an array of bytes.
	 * 
	 * @param objectId
	 *            the object ID
	 * @param connectionName
	 *            the connection name
	 * @param imageType
	 *            the type of image to retrieve (eg., small, normal, large, or square)
	 *            
	 * @return an image as an array of bytes.
	 */
	byte[] fetchImage(String objectId, String connectionName, String imageType);

	/**
	 * Publishes data to an object's connection. Requires appropriate permission
	 * to publish to the object connection.
	 * 
	 * @param objectId
	 *            the object ID to publish to.
	 * @param connectionName
	 *            the connection name to publish to.
	 * @param type
	 *            the Java type that is expected in return
	 * @param data
	 *            the data to publish to the connection.
	 *            
	 * @return result of publishing of the newly published object.
	 */
	<T> T publish(String objectId, String connectionName, Class<T> type, Object data);

	/**
	 * Publishes data to an object's connection. 
	 * 
	 * Requires appropriate permission to publish to the object connection.
	 * 
	 * @param objectId
	 *            the object ID to publish to.
	 * @param connectionName
	 *            the connection name to publish to.
	 * @param data
	 *            the data to publish to the connection.
	 *            
	 * @return result of publishing as a map
	 */
	Map<String, Object> publish(String objectId, String connectionName, Object data);

	/**
	 * Deletes an object. Requires appropriate permission to delete the object.
	 * 
	 * @param objectId
	 *            the object ID
	 */
	void delete(String objectId);

	/**
	 * Deletes an object connection.
	 * 
	 * Requires appropriate permission to delete the object connection.
	 * 
	 * @param objectId
	 *            the object ID
	 * @param connectionName
	 *            the connection name
	 */
	void delete(String objectId, String connectionName);

}
