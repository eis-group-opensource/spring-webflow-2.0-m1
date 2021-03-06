/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.webflow.execution.support;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.webflow.execution.ViewSelection;

/**
 * Concrete response type that requests the rendering of a local, internal application view resource such as a JSP,
 * Velocity, or FreeMarker template.
 * <p>
 * This is typically the most common type of view selection.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public final class ApplicationView extends ViewSelection {

	/**
	 * The name of the view (or page or other response) to render. This name may identify a <i>logical</i> view
	 * resource or may be a <i>physical</i> path to an internal view template.
	 */
	private final String viewName;

	/**
	 * A map of the application data to make available to the view for rendering.
	 */
	private final Map model;

	/**
	 * Creates a new application view.
	 * @param viewName the name (or resource identifier) of the view that should be rendered
	 * @param model the map of application model data to make available to the view during rendering; entries consist of
	 * model names (Strings) to model objects (Objects), model entries may not be null, but the model Map may be null if
	 * there is no model data
	 */
	public ApplicationView(String viewName, Map model) {
		if (model == null) {
			model = Collections.EMPTY_MAP;
		}
		this.viewName = viewName;
		this.model = model;
	}

	/**
	 * Returns the name of the view to render.
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Return the view's application model that should be made available during the rendering process. Never returns
	 * null. The returned map is unmodifiable.
	 */
	public Map getModel() {
		return Collections.unmodifiableMap(model);
	}

	public boolean equals(Object o) {
		if (!(o instanceof ApplicationView)) {
			return false;
		}
		ApplicationView other = (ApplicationView) o;
		return ObjectUtils.nullSafeEquals(viewName, other.viewName) && model.equals(other.model);
	}

	public int hashCode() {
		return (viewName != null ? viewName.hashCode() : 0) + model.hashCode();
	}

	public String toString() {
		return "'" + viewName + "' [" + model.keySet() + "]";
	}
}