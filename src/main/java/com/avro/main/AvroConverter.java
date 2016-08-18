package com.avro.main;

import com.avro.rootclasses.Root;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit Jindal on 7/2/2016.
 * In this class we make use of JAXB to convert the xml into java POJO objects.
 * Then we create the avro schema (avsc) and after that we create the avro file.
 *
 */

public class AvroConverter {

    private static Schema schema = null;
    public static void main(String args[]){
        try {
            createAvroSchema();
            createAvro();
            if (schema != null) {
                FileWriter schemaWriter = new FileWriter(new File("src/main/java/com/avro/output/root.avsc"));
                schemaWriter.append(schema.toString(true));
                schemaWriter.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    * This method is to create the avro schema file (avsc).
    */
    private static void createAvroSchema(){
        SchemaBuilder.FieldAssembler<Schema> schemaBuilder = SchemaBuilder.record("root").namespace(AvroConverter.class.getPackage().getName()).fields();
        try {
            //This schema is only for unmapped xml elements with fields 'elementName' and 'elementValue'.
            SchemaBuilder.FieldAssembler<Schema> sb1 = SchemaBuilder.record("subroot").namespace(AvroConverter.class.getPackage().getName()).fields();
            sb1.name("elementName").type().stringType().noDefault();
            sb1.name("elementValue").type().stringType().noDefault();
            Schema s = sb1.endRecord();

            extractPrimitiveFields(new Root(), schemaBuilder, null);
            schemaBuilder.name("UnknownFields").type().array().items(s).noDefault();
            schema = schemaBuilder.endRecord();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    * This method is to create the avro file.
    */
    private static void createAvro(){
        DataFileWriter<GenericRecord> writer = null;
        JSONObject jObject = new JSONObject();
        try {
            File xmlFile = new File("src/main/java/com/avro/xml/root.xml");

            JAXBContext jaxbContext1 = JAXBContext.newInstance(Root.class);
            Unmarshaller jaxbUnmarshaller1 = jaxbContext1.createUnmarshaller();
            Root data = (Root) jaxbUnmarshaller1.unmarshal(xmlFile);
            if(data != null){
                File avroFile = new File("src/main/java/com/avro/output/root.avro");
                writer = new DataFileWriter<GenericRecord>(new GenericDatumWriter<GenericRecord>());
                writer.create(schema, avroFile);
                DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);

                extractPrimitiveFields(data, null, jObject);

                List<JSONObject> a = createArrayForUnMappedFields(data.getUnmappedVariables());

                jObject.put("UnknownFields", a);
                JsonDecoder decoder = DecoderFactory.get().jsonDecoder(schema, jObject.toString());
                GenericRecord datum = reader.read(null, decoder);
                writer.append(datum);
            }
            if (writer != null){
                writer.flush();
                writer.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static List<JSONObject> createArrayForUnMappedFields(List<String> data){
        List<JSONObject> a = new ArrayList<JSONObject>();

        for(String str:data){
            JSONObject j = new JSONObject();
            j.put("elementName", str.split(":")[0]);
            j.put("elementValue", str.split(":")[1]);
            a.add(j);
        }
        return a;
    }
    /*
    * This is a common method which utilizes Reflection API which helps in create avro schema and file.
    */
    private static void extractPrimitiveFields(Object obj, SchemaBuilder.FieldAssembler<Schema> schemaBuilder, JSONObject jObject) throws Exception{

        Class<?> cls = obj.getClass();
        Field[] f1 = cls.getDeclaredFields();
        for(Field field: f1){
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            if(fieldType.equalsIgnoreCase("String") ||
                    fieldType.equalsIgnoreCase("int") ||
                    fieldType.equalsIgnoreCase("short") ||
                    fieldType.equalsIgnoreCase("float") ||
                    fieldType.equalsIgnoreCase("byte") ||
                    fieldType.equalsIgnoreCase("long") ||
                    fieldType.equalsIgnoreCase("double") ||
                    fieldType.equalsIgnoreCase("XMLGregorianCalendar"))
            {
                String fieldName = "";
                if(field.getAnnotation(XmlElement.class) != null &&
                        field.getAnnotation(XmlElement.class).name() != null &&
                        !field.getAnnotation(XmlElement.class).name().equals("##default")){
                    fieldName = field.getAnnotation(XmlElement.class).name();
                }else{
                    fieldName = field.getName();
                }
                String fieldValue = field.get(obj) == null ? "" : field.get(obj).toString();
                if(jObject != null)jObject.put(fieldName, fieldValue);
                if(schemaBuilder != null)schemaBuilder.name(fieldName).type().stringType().noDefault();
            }
        }
    }
}
