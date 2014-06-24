package org.hbhk.aili.orm.server.convertor;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.hbhk.aili.orm.share.model.Delete;
import org.hbhk.aili.orm.share.model.ObjectFactory;
import org.hbhk.aili.orm.share.model.Orm;
import org.hbhk.aili.orm.share.util.JAXBContextUtil;

public class OrmConvertor {
	/** The Constant CLZZ. */
	private static final Class<Orm> CLZZ = Orm.class;

	/** The log. */
	/** The context. */
	private static JAXBContext context = JAXBContextUtil.initContext(CLZZ);

	public Orm toMessage(String string) throws UnsupportedEncodingException {
		if (context == null) {
			JAXBContextUtil.initContext(CLZZ);// 再次尝试一次
			if (context == null) {
			}
		}
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(
					string.getBytes("UTF-8"));
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<Orm> element = unmarshaller.unmarshal(new StreamSource(
					stream), CLZZ);
			return element.getValue();
		} catch (JAXBException e) {
		}
		return null;
	}

	public String fromMessage(Orm value) {
		if (context == null) {
			JAXBContextUtil.initContext(CLZZ);// 再次尝试一次
			if (context == null) {
			}
		}
		if (value == null) {
			return null;
		}
		try {
			StringWriter stringWriter = new StringWriter();
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING,
					"UTF-8");
			JAXBElement<Orm> element = new ObjectFactory().createOrm(value);
			marshaller.marshal(element, stringWriter);
			stringWriter.flush();
			return stringWriter.toString();
		} catch (PropertyException e) {
		} catch (FactoryConfigurationError e) {
		} catch (JAXBException e) {
		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		Orm o = new Orm();
		Delete d = new Delete();
		d.setSql("sql111111111");
		d.setId("id");
		d.setClazz("class");
		List<Delete> ds = new ArrayList<Delete>();
		ds.add(d);
		OrmConvertor c = new OrmConvertor();
		o.setDelete(ds);
		String str = c.fromMessage(o);
		 Orm orm =c.toMessage(str);
		System.out.println(str);
		System.out.println(orm.getDelete().get(0).getSql());
	}
}