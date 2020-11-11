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
        if(userid.equals("shashi")) {
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
                .add("exists", false)
                .add("data", JSON.createObjectBuilder()
                                    .add( "userid", jsonObject.get("userid").toString())
                                    .add( "firstname",  jsonObject.get("firstname").toString())
                                    .add( "lastname",  jsonObject.get("lastname").toString())
                                    .add( "customertype",  jsonObject.get("customertype").toString())
                                    .add( "mobile",  jsonObject.get("mobile").toString())
                                    .add( "address",  jsonObject.get("address").toString())
                                    .add( "evinfo", JSON.createObjectBuilder()
                                                        .add( "model", evinfoObj.get("model").toString())
                                                        .add( "manufacturer", evinfoObj.get("manufacturer").toString())
                                                        .add( "efficiency_kwh", evinfoObj.get("efficiency_kwh").toString())
                                                        .add( "efficiency_info", evinfoObj.get("efficiency_info").toString())
                                                        .build())
                                    .add( "evopod", JSON.createObjectBuilder()
                                                        .add( "socketype", evopodObj.get("socketype").toString())
                                                        .add( "voltage", evopodObj.get("voltage").toString())
                                                        .add( "amperage", evopodObj.get("amperage").toString())
                                                        .add( "phase", evopodObj.get("phase").toString())
                                                        .add( "latitude", evopodObj.get("latitude").toString())
                                                        .add( "longitude", evopodObj.get("longitude").toString())
                                                        .build())
                                    .build())
                .build();
                }

            } else {
                String _document = "{\"userid\":\"" + userid + "\", \"password\":\"" + password + "\",\"mobile\":\"" + mobile + "\",\"firstname\":\"\",\"lastname\":\"\",\"customertype\":\"\",\"address\":\"\",\"evinfo\": {},\"evopod\": {}}";

				// Create a JSON document.
				OracleDocument doc = db.createDocumentFromString(_document);

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
                                                        .add( "userid", jsonObject.get("userid").toString())
                                                        .add( "firstname",  jsonObject.get("firstname").toString())
                                                        .add( "lastname",  jsonObject.get("lastname").toString())
                                                        .add( "customertype",  jsonObject.get("customertype").toString())
                                                        .add( "mobile",  jsonObject.get("mobile").toString())
                                                        .add( "address",  jsonObject.get("address").toString())
                                                        .add( "evinfo", JSON.createObjectBuilder()
                                                                            .add( "model", evinfoObj.get("model").toString())
                                                                            .add( "manufacturer", evinfoObj.get("manufacturer").toString())
                                                                            .add( "efficiency_kwh", evinfoObj.get("efficiency_kwh").toString())
                                                                            .add( "efficiency_info", evinfoObj.get("efficiency_info").toString())
                                                                            .build())
                                                        .add( "evopod", JSON.createObjectBuilder()
                                                                            .add( "socketype", evopodObj.get("socketype").toString())
                                                                            .add( "voltage", evopodObj.get("voltage").toString())
                                                                            .add( "amperage", evopodObj.get("amperage").toString())
                                                                            .add( "phase", evopodObj.get("phase").toString())
                                                                            .add( "latitude", evopodObj.get("latitude").toString())
                                                                            .add( "longitude", evopodObj.get("longitude").toString())
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