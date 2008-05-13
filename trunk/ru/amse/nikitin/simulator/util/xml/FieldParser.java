package ru.amse.nikitin.simulator.util.xml;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;

import ru.amse.nikitin.simulator.IActiveObject;

import javax.swing.ImageIcon;
import javax.xml.parsers.*;

class MyContentHandler extends DefaultHandler {
	protected class Style {
		private Map<String, String> variables = new HashMap<String, String>();
		public void put(String var, String val) {
			variables.put(var, val);
		}
		public String get(String var) {
			return variables.get(var);
		}
		public boolean contains(String var) {
			return variables.containsKey(var);
		}
	}
	
	protected List<? extends IActiveObject> objects;
	protected Map<String, Style> styles = new HashMap<String, Style>();

	public MyContentHandler(List<? extends IActiveObject> objects) {
		this.objects = objects;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		if (qName == "style") { // style record
			String name = atts.getValue("name");
			if (!styles.containsKey(name)) {
				Style style = new Style();
				for(int i = 0; i < atts.getLength(); i++) {
					String n = atts.getLocalName(i);
					String s = atts.getValue(i);
					if(!"name".equals(n)) {
						style.put(n, s);
					}
				}
				styles.put(name, style);
			}
		}
		
		if (qName == "mot") { // mot record
			int id = Integer.parseInt(atts.getValue("id"));
			if ((0 <= id) && (id < objects.size())) {
				String style = atts.getValue("style");
				if (styles.containsKey(style)) {
					Style s = styles.get(style);
					((IActiveObject)objects.get(id)).newDesc(
						new ImageIcon(checkParam("img", s, atts)),
						atts.getValue(checkParam("name", s, atts)),
						Integer.parseInt(checkParam("x", s, atts)),
						Integer.parseInt(checkParam("y", s, atts))
					);
				}
			}
		}
	}
	
	private String checkParam(String name, Style style, Attributes atts) {
		return style.contains(name) ? style.get(name) : atts.getValue(name);
	}
	
}

public class FieldParser {
	
	public static FieldInfo ReadStyles (InputStream byteStream,
			List<? extends IActiveObject> objects) {
		FieldInfo res = new FieldInfo(objects);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(byteStream, new MyContentHandler(objects));
		} catch (ParserConfigurationException pce) {
			System.err.println("Parser Config Error");
		} catch (IOException ioe) {
			System.err.println("I/O Error");
		}  catch (SAXException sae) {
			System.err.println(sae.getMessage());
		}
		
		return res;
	}
}
