package com.huotu.ymr.common;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Administrator on 2015/7/30.
 */
public class XmlHelper {
    /**
     * 读取xml内容,并转为bean
     *
     * @param xml
     * @param cls
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T convertToBean(String xml, Class<T> cls) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        T t = (T) unmarshaller.unmarshal(new StringReader(xml));
        return t;
    }

    /**
     * 读取xml文件,并转为bean
     *
     * @param file
     * @param cls
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T convertToBean(File file, Class<T> cls) throws Exception {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        T t = (T) unmarshaller.unmarshal(file);

        return t;
    }


    /**
     * bean转xml
     * @param object
     * @param encoding
     * @return
     * @throws JAXBException
     */
    public static String convertToXml(Object object, String encoding) throws JAXBException {
        String result = null;

        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        result = writer.toString();
        return result;
    }


}
