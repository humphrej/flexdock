/*
 * Created on Feb 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.windowing.plaf.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.flexdock.windowing.plaf.Configurator;
import org.flexdock.windowing.plaf.PropertySet;
import org.flexdock.windowing.plaf.XMLConstants;
import org.w3c.dom.Element;

/**
 * @author Christopher Butler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResourceHandlerFactory implements XMLConstants {
	private static final HashMap RESOURCE_HANDLERS = loadResourceHandlers();
	private static final HashMap PROPERTY_HANDLERS = loadPropertyHandlers();
	
	public static ResourceHandler getResourceHandler(String handlerName) {
		return (ResourceHandler)RESOURCE_HANDLERS.get(handlerName);
	}
	
	public static String getPropertyHandler(String propertyType) {
		return (String)PROPERTY_HANDLERS.get(propertyType);
	}
	
	private static HashMap loadResourceHandlers() {
		HashMap elements = Configurator.getNamedElementsByTagName(HANDLER_KEY);
		HashMap handlers = new HashMap(elements.size());

		for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
			Element elem = (Element)it.next();
			String key = elem.getAttribute(NAME_KEY);
			String className = elem.getAttribute(VALUE_KEY);
			ResourceHandler handler = createResourceHandler(className);
			if(handler!=null)
				handlers.put(key, handler);
		}
		// add constructor handlers to the set
		HashMap constructors = loadConstructors();
		handlers.putAll(constructors);
		
		return handlers;
	}
	
	private static ResourceHandler createResourceHandler(String className) {
		if(Configurator.isNull(className))
			return null;

		try {
			Class clazz = Class.forName(className);
			return (ResourceHandler)clazz.newInstance();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static HashMap loadPropertyHandlers() {
		HashMap elements = Configurator.getNamedElementsByTagName(PROP_HANDLER_KEY);
		HashMap propHandlers = new HashMap(elements.size());

		for(Iterator it=elements.keySet().iterator(); it.hasNext();) {
			Element elem = (Element)it.next();
			String tagName = elem.getAttribute(NAME_KEY);
			String handlerName = elem.getAttribute(VALUE_KEY);
			if(!Configurator.isNull(tagName) && !Configurator.isNull(handlerName))
				propHandlers.put(tagName, handlerName);
		}
		return propHandlers;
	}
	
	private static HashMap loadConstructors() {
		PropertySet[] constructors = Configurator.getProperties(CONSTRUCTOR_KEY);
		HashMap map = new HashMap(constructors.length);
		
		for(int i=0; i<constructors.length; i++) {
			ConstructorHandler handler = createConstructorHandler(constructors[i]);
			if(handler!=null) {
				map.put(constructors[i].getName(), handler);
			}
		}
		return map;
	}
	
	private static ConstructorHandler createConstructorHandler(PropertySet props) {
		String className = props.getString(CLASSNAME_KEY);
		if(Configurator.isNull(className))
			return null;
		
		try {
			List argKeys = props.getNumericKeys(true);
			ArrayList params = new ArrayList(argKeys.size());
			for(Iterator it=argKeys.iterator(); it.hasNext();) {
				String key = (String)it.next();
				Class paramType = props.toClass(key);
				if(!paramType.isPrimitive() && paramType!=String.class)
					throw new IllegalArgumentException("ConstructorHandler can only parse primitive and String arguments: " + paramType);
				params.add(paramType);
			}
			
			Class type = Class.forName(className);
			Class[] paramTypes = (Class[])params.toArray(new Class[0]);
			Constructor constructor = type.getConstructor(paramTypes);

			return new ConstructorHandler(constructor);
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

}