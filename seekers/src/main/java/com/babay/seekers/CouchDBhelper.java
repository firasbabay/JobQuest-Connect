package com.babay.seekers;


import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.lightcouch.Attachment;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.lightcouch.Params;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;



@RequiredArgsConstructor

public class CouchDBhelper {
    private static CouchDbClient db ;


    public static CouchDbClient getInstance(){
        if (db == null) {
            db = new CouchDbClient("pdf", true, "http", "pdfdb", 5984, "couchdb", "1234");
        }
        return db;
    }

    public static void saveDocument(String username , InputStream file){
        byte[] bytes = null;
        try{
            bytes = IOUtils.toByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = Base64.getEncoder().encodeToString(bytes) ;
        Attachment attachment = new Attachment(data , "application/pdf");
        PdfDocument pdf = new PdfDocument();
        pdf.setId(username);
        pdf.addAttachment(username + "_curriculum.pdf", attachment);

        getInstance().save(pdf);
    }
    public static InputStream getDocument(String username) {
        try {
            PdfDocument pdf =  getInstance().find(PdfDocument.class, username, new Params().attachments());
            String base64Data = pdf.getAttachments().get(username + "_curriculum.pdf").getData();
            byte[] doc = Base64.getDecoder().decode(base64Data);
            return new ByteArrayInputStream(doc);
        } catch (NoDocumentException e) {
            return null;
        }
    }

    public static void deleteDocument(String username) {
        getInstance().remove(getInstance().find(PdfDocument.class, username));
    }

}
