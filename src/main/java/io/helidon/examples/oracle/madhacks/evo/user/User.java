/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.oracle.madhacks.evo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;




import org.eclipse.microprofile.opentracing.Traced;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

///////////////////

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.eclipse.microprofile.opentracing.Traced;

import java.nio.file.* ;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import java.io.* ;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import oracle.soda.rdbms.OracleRDBMSClient;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleCursor;
import oracle.soda.OracleCollection;
import oracle.soda.OracleDocument;
import oracle.soda.OracleException;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;


import javax.json.stream.JsonParser;

// import org.json.*; 

import java.time.LocalDateTime;

import java.util.Collections;

import io.helidon.examples.oracle.madhacks.evo.EvoProvider;

/**
 * An implementation of
 * {@link io.helidon.examples.sockshop.carts.CartRepository} that that uses
 * MongoDB as a backend data store.
 */

@ApplicationScoped@Alternative@Priority(APPLICATION)@Traced
public class User  {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory evinfo = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory singupJSON = Json.createBuilderFactory(Collections.emptyMap());

	public static EvoProvider asp = new EvoProvider("test");
	public static OracleDatabase db = asp.dbConnect();

	@Inject
	void User() {
		try {
			String UserResponse = createData();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public JsonObject findUser(String userid) {
        try {
            OracleCollection col = this.db.admin().createCollection("users");
            // Find all documents in the collection.
            OracleDocument oraDoc,
            resultDoc = null;
            String jsonFormattedString = null;

            OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : \"" + userid + "\"}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());

            resultDoc = col.find().filter(filterSpec).getOne();
            // System.out.println("resultDoc: -------" + resultDoc.getContentAsString());
            // System.out.println(resultDoc.equals(null));

            if (resultDoc != null) {
                System.out.println("1-------------------------");
                JsonParser jParser=  Json.createParser(new ByteArrayInputStream(resultDoc.getContentAsString().getBytes()));
                while(jParser.hasNext()){
                    jParser.next();
                    JsonObject jsonObject = jParser.getObject();
          
       System.out.println("2-------------------------");
       System.out.println(jsonObject.get("userid").toString());
       System.out.println(jsonObject.get("userid"));
       System.out.println(userid + "");
       System.out.println(userid.toString());
        if(userid.toString().equals(jsonObject.get("userid").toString())) {
            System.out.println("3-------------------------");
        return JSON.createObjectBuilder()
                    .add( "userid", userid)
                    .add( "firstname", "samuel")
                    .add( "lastname", "varthaman")
                    .add( "customertype", "ev")
                    .add( "mobile", "randy")
                    .add( "address", "myaddress")
                    .add( "evinfo", JSON.createObjectBuilder()
                                        .add( "model", "randy")
                                        .add( "manufacturer", "randy")
                                        .add( "efficiency_kwh", "10.2")
                                        .add( "efficiency_info", "10.2 kWh/100 km")
                                        .build())
                    .add( "evopod", JSON.createObjectBuilder()
                                        .add( "socketype", "230V, 16A")
                                        .add( "voltage", "230 V")
                                        .add( "amperage", "16 A")
                                        .add( "phase", "1-phase")
                                        .add( "latitude", "12.9200782")
                                        .add( "longitude", "77.5307203")
                                        .build())
                    .build();
    }
}
            }
            System.out.println("4-------------------------");

            } catch (Exception e) {
               e.printStackTrace();
            }
            System.out.println("5-------------------------");
            return JSON.createObjectBuilder()
            .add( "userid", userid)
            .build();
}

public JsonObject signUpUser(String userid,String password,String mobile) {

    try {
        
    
            // Get a collection with the name "socks".
			// This creates a database table, also named "socks", to store the collection.
			OracleCollection col = this.db.admin().createCollection("users");
            // Find all documents in the collection.
            OracleDocument oraDoc,
            resultDoc = null;
            String jsonFormattedString = null;

            OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : \"" + userid + "\"}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());

            resultDoc = col.find().filter(filterSpec).getOne();
            // System.out.println("resultDoc: -------" + resultDoc.getContentAsString());
            // System.out.println(resultDoc.equals(null));

            if (resultDoc != null) {

                //JSONObject jsonobject = new JSONObject(resultDoc.getContentAsString()); 
                System.out.println("User already exists successfully ---------------------");

                JsonParser jParser= Json.createParser(new ByteArrayInputStream(resultDoc.getContentAsString().getBytes()));

                while(jParser.hasNext()){
                jParser.next();
				JsonObject jsonObject = jParser.getObject();
                JsonObject evinfoObj  = (JsonObject) jsonObject.get("evinfo");
                JsonObject evopodObj  = (JsonObject) jsonObject.get("evopod");

                return singupJSON.createObjectBuilder()
                .add("exists", true)
                .add("data", JSON.createObjectBuilder()
                                    .add( "userid", jsonObject.get("userid"))
                                    .add( "firstname",  jsonObject.get("firstname"))
                                    .add( "lastname",  jsonObject.get("lastname"))
                                    .add( "customertype",  jsonObject.get("customertype"))
                                    .add( "mobile",  jsonObject.get("mobile"))
                                    .add( "address",  jsonObject.get("address"))
                                    .add( "evinfo", JSON.createObjectBuilder()
                                                        .add( "model", "")
                                                        .add( "manufacturer", "")
                                                        .add( "efficiency_kwh", "")
                                                        .add( "efficiency_info", "")
                                                        .build())
                                    .add( "evopod", JSON.createObjectBuilder()
                                                        .add( "socketype", "")
                                                        .add( "voltage", "")
                                                        .add( "amperage", "")
                                                        .add( "phase", "")
                                                        .add( "latitude", "")
                                                        .add( "longitude", "")
                                                        .build())
                                    .build())
                .build();
                }

            } else {
                //String _document = "{\"userid\":\"" + userid + "\", \"password\":\"" + password + "\",\"mobile\":\"" + mobile + "\",\"firstname\":\"\",\"lastname\":\"\",\"customertype\":\"\",\"address\":\"\",\"evinfo\": {},\"evopod\": {}}";

                JsonObject docObject = JSON.createObjectBuilder()
                .add( "userid", userid)
                .add( "firstname",  password )
                .add( "lastname",  "")
                .add( "customertype",  "")
                .add( "mobile",  mobile)
                .add( "address",  "")
                .add( "evinfo", JSON.createObjectBuilder()
                                    .add( "model", "")
                                    .add( "manufacturer", "")
                                    .add( "efficiency_kwh", "")
                                    .add( "efficiency_info", "")
                                    .build())
                .add( "evopod", JSON.createObjectBuilder()
                                    .add( "socketype", "")
                                    .add( "voltage", "")
                                    .add( "amperage", "")
                                    .add( "phase", "")
                                    .add( "latitude", "")
                                    .add( "longitude", "")
                                    .build())
                .build();
				// Create a JSON document.
				OracleDocument doc = db.createDocumentFromString(docObject.toString());

				// Insert the document into a collection.
                col.insert(doc);
                System.out.println("Inserted successfully ---------------------");
                OracleDocument newDoc = col.find().filter(filterSpec).getOne();
                System.out.println("Found one record successfully ---------------------");
                System.out.println("singup " + newDoc.getContentAsString() +" .... 200OK");
                
                
                JsonParser jParser=  Json.createParser(new ByteArrayInputStream(newDoc.getContentAsString().getBytes()));
                while(jParser.hasNext()){
                jParser.next();
				JsonObject jsonObject = jParser.getObject();
                JsonObject evinfoObj  = (JsonObject) jsonObject.get("evinfo");
                JsonObject evopodObj  = (JsonObject) jsonObject.get("evopod");

                return singupJSON.createObjectBuilder()
                                    .add("exists", false)
                                    .add("data", JSON.createObjectBuilder()
                                                        .add( "userid", jsonObject.get("userid"))
                                                        .add( "firstname",  jsonObject.get("firstname"))
                                                        .add( "lastname",  jsonObject.get("lastname"))
                                                        .add( "customertype",  jsonObject.get("customertype"))
                                                        .add( "mobile",  jsonObject.get("mobile"))
                                                        .add( "address",  jsonObject.get("address"))
                                                        .add( "evinfo", JSON.createObjectBuilder()
                                                                                .add( "model", "")
                                                                                .add( "manufacturer", "")
                                                                                .add( "efficiency_kwh", "")
                                                                                .add( "efficiency_info", "")
                                                                                .build())
                                                        .add( "evopod", JSON.createObjectBuilder()
                                                                                .add( "socketype", "")
                                                                                .add( "voltage", "")
                                                                                .add( "amperage", "")
                                                                                .add( "phase", "")
                                                                                .add( "latitude", "")
                                                                                .add( "longitude", "")
                                                                                .build())
                                    .build())
                                    .build();

            }
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

return JSON.createObjectBuilder()
            .add( "userid", userid)
            .add("exists", false)
            .build();
}
    

public JsonObject updateUser(JsonObject jsonObject) {

    try {
        
    
            // Get a collection with the name "socks".
			// This creates a database table, also named "socks", to store the collection.
			OracleCollection col = this.db.admin().createCollection("users");
            // Find all documents in the collection.
            OracleDocument oraDoc,
            resultDoc = null;
            String jsonFormattedString = null;

            OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : " + jsonObject.get("userid") + "}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());

            resultDoc = col.find().filter(filterSpec).getOne();
            // System.out.println("resultDoc: -------" + resultDoc.getContentAsString());
            System.out.println(jsonObject.toString());

            if (resultDoc != null) {

                //JSONObject jsonobject = new JSONObject(resultDoc.getContentAsString()); 
                System.out.println("User already exists, updated the user  ---------------------");

                JsonParser jParser= Json.createParser(new ByteArrayInputStream(resultDoc.getContentAsString().getBytes()));

                while(jParser.hasNext()){
                jParser.next();
                JsonObject evinfoObj  = (JsonObject) jsonObject.get("evinfo");
                JsonObject evopodObj  = (JsonObject) jsonObject.get("evopod");

                JsonValue firstname,lastname,customertype,mobile,address,model,manufacturer,efficiency_kwh,efficiency_info,socketype,voltage,amperage,phase,latitude,longitude = jsonObject.NULL;

                 firstname = jsonObject.isNull("firstname") ? jsonObject.NULL : jsonObject.get("firstname");
                 lastname =  jsonObject.isNull("lastname") ? jsonObject.NULL : jsonObject.get("lastname");
                 customertype = jsonObject.isNull("customertype") ? jsonObject.NULL : jsonObject.get("customertype");
                 mobile = jsonObject.isNull("mobile") ? jsonObject.NULL : jsonObject.get("mobile");
                 address = jsonObject.isNull("address") ? jsonObject.NULL : jsonObject.get("address");
                 model = evinfoObj.isNull("model") ? jsonObject.NULL : evinfoObj.get("model");
                 manufacturer = evinfoObj.isNull("manufacturer") ? jsonObject.NULL : evinfoObj.get("manufacturer");
                 efficiency_kwh = evinfoObj.isNull("efficiency_kwh") ? jsonObject.NULL : evinfoObj.get("efficiency_kwh");
                 efficiency_info = evinfoObj.isNull("efficiency_info") ? jsonObject.NULL : evinfoObj.get("efficiency_info");
                 socketype = evopodObj.isNull("socketype") ? jsonObject.NULL : evopodObj.get("socketype");
                 voltage = evopodObj.isNull("voltage") ? jsonObject.NULL : evopodObj.get("voltage");
                 amperage = evopodObj.isNull("amperage") ? jsonObject.NULL : evopodObj.get("amperage");
                 phase = evopodObj.isNull("phase") ? jsonObject.NULL : evopodObj.get("phase");
                 latitude = evopodObj.isNull("latitude") ? jsonObject.NULL : evopodObj.get("latitude");
                 longitude = evopodObj.isNull("longitude") ? jsonObject.NULL : evopodObj.get("longitude");

                return singupJSON.createObjectBuilder()
                .add("exists", true)
                .add("data", JSON.createObjectBuilder()
                                    .add( "userid", jsonObject.get("userid"))
                                    .add( "firstname",  firstname)
                                    .add( "lastname",  lastname)
                                    .add( "customertype",  customertype)
                                    .add( "mobile", mobile )
                                    .add( "address", address)
                                    .add( "evinfo", JSON.createObjectBuilder()
                                                        .add( "model",model )
                                                        .add( "manufacturer",manufacturer )
                                                        .add( "efficiency_kwh",efficiency_kwh )
                                                        .add( "efficiency_info", efficiency_info )
                                                        .build())
                                    .add( "evopod", JSON.createObjectBuilder()
                                                        .add( "socketype",socketype )
                                                        .add( "voltage", voltage)
                                                        .add( "amperage",amperage )
                                                        .add( "phase", phase)
                                                        .add( "latitude", latitude)
                                                        .add( "longitude", longitude)
                                                        .build())
                                    .build())
                .build();               
            

            } 
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

return JSON.createObjectBuilder()
            .add( "userid", jsonObject.get("userid"))
            .add("exists", false)
            .build();
}
	
	public String createData() {

		try {
			OracleCollection col = this.db.admin().createCollection("users");
			col.admin().truncate();
		} catch(OracleException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "successfully created users collection !!!";
	}

}