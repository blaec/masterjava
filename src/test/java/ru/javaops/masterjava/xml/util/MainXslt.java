package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;

public class MainXslt {
    public static void main(String[] args) throws Exception {
        try (InputStream xslInputStream = Resources.getResource("projects.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource("payload.xml").openStream()) {
            Result outputXHTML = new StreamResult(new File("output.html"));

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            processor.transform(xmlInputStream, outputXHTML);
        }
    }
}
