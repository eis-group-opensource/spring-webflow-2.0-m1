/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.faces.ui;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.shale.remoting.Mechanism;
import org.apache.shale.remoting.XhtmlHelper;

public class ExtJsRenderer extends Renderer {

	private static final String EXT_CSS = "/org/springframework/faces/ui/ext/resources/css/ext-all.css";

	private static final String EXT_SCRIPT = "/org/springframework/faces/ui/ext/ext.js";

	private static final String SPRING_FACES_SCRIPT = "/org/springframework/faces/ui/SpringFaces.js";

	private XhtmlHelper resourceHelper = new XhtmlHelper();

	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		ExtJsComponent extJsComponent = (ExtJsComponent) component;

		ResponseWriter writer = context.getResponseWriter();

		if (extJsComponent.getIncludeExtStyles().equals(Boolean.TRUE))
			resourceHelper.linkStylesheet(context, extJsComponent, writer, Mechanism.CLASS_RESOURCE, EXT_CSS);

		if (extJsComponent.getIncludeExtScript().equals(Boolean.TRUE))
			resourceHelper.linkJavascript(context, extJsComponent, writer, Mechanism.CLASS_RESOURCE, EXT_SCRIPT);

		resourceHelper.linkJavascript(context, extJsComponent, writer, Mechanism.CLASS_RESOURCE, SPRING_FACES_SCRIPT);
	}
}
