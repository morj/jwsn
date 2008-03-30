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
		protected ImageIcon image;
		public Style(ImageIcon image) {
			this.image = image;
		}
		public ImageIcon getImage() {
			return image;
		}
	}
	
	protected List<? extends IActiveObject> objects;
	protected Map<String, Style> styles = new HashMap<String, Style>();

	public MyContentHandler(List<? extends IActiveObject> objects) {
		this.objects = objects;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		if (localName == "style") { // style record
			String name = atts.getValue("name");
			if (!styles.containsKey(name)) {
				styles.put(name, new Style(
					new ImageIcon(atts.getValue("img"))
				));
			}
		}
		
		if (localName == "mot") { // style record
			int id = Integer.parseInt(atts.getValue("id"));
			if ((0 <= id) && (id < objects.size())) {
				String style = atts.getValue("style");
				if (styles.containsKey(style)) {
					Style s = styles.get(style);
					((IActiveObject)objects.get(id)).newDesc(
						s.getImage(),
						atts.getValue("name"),
						Integer.parseInt(atts.getValue("x")),
						Integer.parseInt(atts.getValue("y"))
					);
				}
			}
		}
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
