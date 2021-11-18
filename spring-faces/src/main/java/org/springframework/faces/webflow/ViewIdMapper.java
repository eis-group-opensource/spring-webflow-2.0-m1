/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.webflow;

import javax.faces.application.ViewHandler;

/**
 * Interface to be implemented by objects that can map Web Flow view names to JSF view identifiers. JSF view identifiers
 * are used to determine if the current view has changed and to create views by delegating to the application's
 * {@link ViewHandler}.
 * 
 * A view handler typically treats a JSF view id as the physical location of a view template encapsulating a page
 * layout. The JSF view id normally specifies the physical location of the view template minus a suffix. View handlers
 * typically replace the suffix of any view id with their own default suffix (e.g. ".jsp" or ".xhtml") and then try to
 * locate a physical template view.
 * 
 * @author Colin Sampaleanu
 */
public interface ViewIdMapper {

	/**
	 * Map the given Spring Web Flow view name to a JSF view identifier.
	 * @param viewName name of the view to map
	 * @return the corresponding JSF view id
	 */
	public String mapViewId(String viewName);

}