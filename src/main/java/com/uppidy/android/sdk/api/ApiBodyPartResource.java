package com.uppidy.android.sdk.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.Assert;

/**
 * @author arudnev@uppidy.com
 *
 */
public class ApiBodyPartResource extends AbstractResource {

	private final ApiBodyPart part;


	/**
	 * Create a new ApiBodyPartResource.
	 * <p>
	 * @param part the part to get the stream from
	 */
	public ApiBodyPartResource(ApiBodyPart part) {
		Assert.notNull(part, "part must not be null");
		this.part = part;
	}

	/**
	 * Tries to open input stream.
	 */
	@Override
	public boolean exists() {
		try {
			InputStream is = getInputStream();
			is.close();
			return true;
		}
		catch (Throwable isEx) {
			return false;
		}
	}

	@Override
	public String getDescription() {
		return "part [" + this.part + "]";
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStreamSource source = part.getData();
		if (source == null) {
			throw new FileNotFoundException(getDescription() + " does not have input stream source");
		}
		InputStream inputStream = source.getInputStream();
		if (inputStream == null) {
			throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
		}
		return inputStream;
	}
	
	@Override
	public String getFilename() {
		return part.getFileName();
	}
	
	@Override
	public long contentLength() throws IOException {
		return -1;
	}

	/**
	 * This implementation compares the parts of the resources.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj == this || (obj instanceof ApiBodyPartResource && this.part.equals(((ApiBodyPartResource) obj).part)));
	}

	/**
	 * This implementation returns the hash code of the part.
	 */
	@Override
	public int hashCode() {
		return this.part.hashCode();
	}

}
