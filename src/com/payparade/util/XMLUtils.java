package com.payparade.util;

public class XMLUtils extends Object {
	protected static Logger logger_ = Logger.getLogger(XMLUtils.class
			.getSimpleName());
	private String xmlString_ = null;

	public XMLUtils(String xmlString) {
		xmlString_ = xmlString;
	}

	public String getNext(String name) {
		String value = null;

		int i1 = xmlString_.indexOf("<" + name);
		int i2 = xmlString_.indexOf("</" + name, i1 + 1);

		if (i1 != -1 && i2 != -1) {
			value = xmlString_.substring(i1, i2);
			xmlString_ = xmlString_.substring(i2 + 1);
		}

		return value;

	}

	public String getValue(String name, String element) {
		String value = null;

		int i1 = element.indexOf("<" + name);
		int i2 = element.indexOf(">", i1 + 1);
		int i3 = element.indexOf("</" + name, i2 + 1);

		if (i1 != -1 && i2 != -1 && i3 != -1) {
			value = element.substring(i2 + 1, i3);
		}

		return value;

	}

}
