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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;
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
public class Notification  {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory evinfo = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory singupJSON = Json.createBuilderFactory(Collections.emptyMap());

	public static EvoProvider asp = new EvoProvider("test");
	public static OracleDatabase db = asp.dbConnect();

	@Inject
	void Notification() {
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
             System.out.println("resultDoc: -------" + resultDoc.getContentAsString());
            // System.out.println(resultDoc.equals(null));

            if (resultDoc != null) {
                System.out.println("1-------------------------");
                JsonParser jParser=  Json.createParser(new ByteArrayInputStream(resultDoc.getContentAsString().getBytes()));
                while(jParser.hasNext()){
                    jParser.next();
                    JsonObject jsonObject = jParser.getObject();
          
        if(userid.equals(jsonObject.getString("userid"))) {
            System.out.println("3-------------------------");
        JsonObject evinfoObj  = (JsonObject) jsonObject.get("evinfo");
                JsonObject evopodObj  = (JsonObject) jsonObject.get("evopod");

                JsonValue firstname,lastname,customertype,mobile,address,chargeamount,currency,model,manufacturer,efficiency_kwh,efficiency_info,socketype,voltage,amperage,phase,latitude,longitude = jsonObject.NULL;

                 firstname = jsonObject.isNull("firstname") ? jsonObject.NULL : jsonObject.get("firstname");
                 lastname =  jsonObject.isNull("lastname") ? jsonObject.NULL : jsonObject.get("lastname");
                 customertype = jsonObject.isNull("customertype") ? jsonObject.NULL : jsonObject.get("customertype");
                 mobile = jsonObject.isNull("mobile") ? jsonObject.NULL : jsonObject.get("mobile");
                 address = jsonObject.isNull("address") ? jsonObject.NULL : jsonObject.get("address");
                 chargeamount = jsonObject.isNull("chargeamount") ? jsonObject.NULL : jsonObject.get("chargeamount");
                 currency = jsonObject.isNull("currency") ? jsonObject.NULL : jsonObject.get("currency");
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
                                    .add( "chargeamount", chargeamount)
                                    .add( "currency", currency)
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

public JsonObject setNotification(String userid,String password,String mobile) {

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
                                    .add( "chargeamount", 10)
                                    .add( "currency", "$")
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
                .add( "chargeamount", 10)
                .add( "currency", "$")
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
                                                        .add( "chargeamount", jsonObject.get("chargeamount"))
                                                        .add( "currency", jsonObject.get("currency"))
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
    

public JsonObject notificationBroker(JsonObject jsonObject) {
    System.out.println("Inside notification Broker---------------------");

    try {
        
    
            // Get a collection with the name "socks".
			// This creates a database table, also named "socks", to store the collection.
			OracleCollection col = this.db.admin().createCollection("notification");
            // Find all documents in the collection.
            OracleDocument oraDoc,
            resultDoc = null;
            String jsonFormattedString = null;

            if(jsonObject.getString("action").equals("set")) {
                JsonValue userid = jsonObject.get("userid");
                JsonValue touserid = jsonObject.get("touserid");
                JsonValue customertype = jsonObject.get("customertype");
                JsonValue action = jsonObject.get("action");
                JsonValue message = jsonObject.get("message");

                JsonObject docObject = JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "active")
                .build();
                // Create a JSON document.
                OracleDocument doc = db.createDocumentFromString(docObject.toString());
                OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : " + jsonObject.get("userid") + ", \"touserid\" : " + jsonObject.get("touserid") + "}");
                // Insert the document into a collection.
                col.find().filter(filterSpec).remove();
                System.out.println("removed previous notification successfully ---------------------");
                col.insert(doc);
                System.out.println("Inserted notification successfully ---------------------");
                return JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "active")
                .build();
            }

            if(jsonObject.getString("action").equals("accept")) {

                JsonValue userid = jsonObject.get("userid");
                JsonValue touserid = jsonObject.get("touserid");
                JsonValue customertype = jsonObject.get("customertype");
                JsonValue action = jsonObject.get("action");
                JsonValue message = jsonObject.get("message");

                JsonObject docObject = JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "accepted")
                .build();
                // Create a JSON document.
                OracleDocument doc = db.createDocumentFromString(docObject.toString());
                OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : " + jsonObject.get("userid") + ", \"touserid\" : " + jsonObject.get("touserid") + "}");
                // Insert the document into a collection.
                col.find().filter(filterSpec).remove();
                System.out.println("removed previous notification successfully ---------------------");
                col.insert(doc);
                System.out.println("Inserted notification successfully ---------------------");
                return JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "accepted")
                .build();

            }
            if(jsonObject.getString("action").equals("decline")) {
                
                JsonValue userid = jsonObject.get("userid");
                JsonValue touserid = jsonObject.get("touserid");
                JsonValue customertype = jsonObject.get("customertype");
                JsonValue action = jsonObject.get("action");
                JsonValue message = jsonObject.get("message");

                JsonObject docObject = JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "declined")
                .build();
                // Create a JSON document.
                OracleDocument doc = db.createDocumentFromString(docObject.toString());
                OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : " + jsonObject.get("userid") + ", \"touserid\" : " + jsonObject.get("touserid") + "}");
                // Insert the document into a collection.
                col.find().filter(filterSpec).remove();
                System.out.println("removed previous notification successfully ---------------------");
                col.insert(doc);
                System.out.println("Inserted notification successfully ---------------------");
                return JSON.createObjectBuilder()
                .add( "userid", jsonObject.get("userid"))
                .add( "touserid",  touserid)
                .add( "customertype",  customertype)
                .add( "action",  action)
                .add( "message", message )
                .add( "status", "declined")
                .build();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

return JSON.createObjectBuilder()
            .add( "userid", jsonObject.get("userid"))
            .add("exists", false)
            .build();
}

public JsonArray findNotification(JsonObject jsonObject) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    try {
        OracleCollection col = this.db.admin().createCollection("notification");
        // Find all documents in the collection.
        OracleDocument oraDoc,resultDoc = null;
        OracleDocument filterSpec = this.db.createDocumentFromString("{}");
        String jsonFormattedString = null;

        JsonValue userid = jsonObject.get("userid");
        JsonValue status = jsonObject.get("status");

        if(jsonObject.getString("status").equals("active")) {
            filterSpec = this.db.createDocumentFromString("{\"$or\" : [ { \"userid\" : " + jsonObject.get("userid") + ", \"status\": \"active\"},{ \"touserid\" : " + jsonObject.get("userid") + ", \"status\": \"active\"} ]}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());
        }

        if(jsonObject.getString("status").equals("accepted")) {
            filterSpec = this.db.createDocumentFromString("{\"$or\" : [ { \"userid\" : " + jsonObject.get("userid") + ", \"status\": \"accepted\"},{ \"touserid\" : " + jsonObject.get("userid") + ", \"status\": \"accepted\"} ]}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());
        }

        if(jsonObject.getString("status").equals("declined")) {
            filterSpec = this.db.createDocumentFromString("{\"$or\" : [ { \"userid\" : " + jsonObject.get("userid") + ", \"status\": \"declined\"},{ \"touserid\" : " + jsonObject.get("userid") + ", \"status\": \"declined\"} ]}");
            System.out.println("filterSpec: -------" + filterSpec.getContentAsString());

        }

        

        //resultDoc = col.find().filter(filterSpec).getOne();

        OracleCursor c = col.find().filter(filterSpec).getCursor();

        while (c.hasNext()) {
            resultDoc = c.next();
         System.out.println("resultDoc: -------" + resultDoc.getContentAsString());
        // System.out.println(resultDoc.equals(null));  
        //,{\"evopod\": {\"$not\" : {\"$eq\" : {}}}}
        //{\"evopod\": {\"amperage\": {\"$not\" : {\"$eq\" : \"\"}}}}

        if (resultDoc != null) {
            System.out.println("1-------------------------");
            JsonParser jParser=  Json.createParser(new ByteArrayInputStream(resultDoc.getContentAsString().getBytes()));
            while(jParser.hasNext()){
                jParser.next();
                jsonObject = jParser.getObject();

        System.out.println("3-------------------------");


            JsonValue _userid = jsonObject.isNull("userid") ? jsonObject.NULL : jsonObject.get("userid");
            JsonValue _touserid = jsonObject.isNull("touserid") ? jsonObject.NULL : jsonObject.get("touserid");
            JsonValue _customertype = jsonObject.isNull("customertype") ? jsonObject.NULL : jsonObject.get("customertype");
            JsonValue _action = jsonObject.isNull("action") ? jsonObject.NULL : jsonObject.get("action");
            JsonValue _message = jsonObject.isNull("message") ? jsonObject.NULL : jsonObject.get("message");
            JsonValue _status = jsonObject.isNull("status") ? jsonObject.NULL : jsonObject.get("status");
            


           

             builder.add(singupJSON.createObjectBuilder()
            .add("exists", true)
            .add("data", JSON.createObjectBuilder()
                                .add( "userid", _userid)
                                .add( "touserid",  _touserid)
                                .add( "customertype",  _customertype)
                                .add( "action",  _action)
                                .add( "message", _message )
                                .add( "status", _status)
                                .build())
            .build()   
             );

}
}
        
        System.out.println("4-------------------------");
        }
        JsonArray arr = builder.build();
        return arr;
        } catch (Exception e) {
           e.printStackTrace();
        }
        System.out.println("5-------------------------");
        builder.add( JSON.createObjectBuilder()
        .add( "atpsoda", "found 0")
        .build());
        JsonArray arr = builder.build();
        return arr;
}


	
	public String createData() {

		try {
			OracleCollection col = this.db.admin().createCollection("notification");
			col.admin().truncate();
		} catch(OracleException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "successfully created users collection !!!";
	}

}