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
import java.time.LocalDateTime;
import java.util.Collections;
import io.helidon.examples.oracle.madhacks.evo.EvoProvider;

/**
 * An implementation of
 * {@link io.helidon.examples.sockshop.carts.CartRepository} that that uses
 * MongoDB as a backend data store.
 */

@ApplicationScoped@Alternative@Priority(APPLICATION)@Traced
public class Calculator  {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory evinfo = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory singupJSON = Json.createBuilderFactory(Collections.emptyMap());

	public static EvoProvider asp = new EvoProvider("test");
	public static OracleDatabase db = asp.dbConnect();

	@Inject
	void Calculator() {
		try {
			String UserResponse = createData();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public JsonObject findChargeCost(String userid, String podownerid, JsonValue consumedpowerinwatts, JsonValue consumedpowerinhours) {
        try {
            OracleCollection col = this.db.admin().createCollection("users");
            // Find all documents in the collection.
            OracleDocument oraDoc,
            resultDoc = null;
            String jsonFormattedString = null;
            Float powersupplierrate = 10;
            Float govttax = 15;
            OracleDocument filterSpec = this.db.createDocumentFromString("{ \"userid\" : \"" + podownerid + "\"}");
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
          
        if(podownerid.equals(jsonObject.getString("userid"))) {
            System.out.println("3-------------------------");


                JsonValue chargeamount,currency = jsonObject.NULL;

                 
                chargeamount = jsonObject.isNull("chargeamount") ? jsonObject.NULL : jsonObject.get("chargeamount"); 
                currency = jsonObject.isNull("currency") ? jsonObject.NULL : jsonObject.get("currency");                


                Float pricePerkWh = Float.parseFloat(jsonObject.get("chargeamount").toString());
                Float chargingCosts = ((Float.parseFloat(consumedpowerinwatts.toString()) * Float.parseFloat(consumedpowerinhours.toString()))/1000) * pricePerkWh;
                  
                Float powersuppliercosts = chargingCosts * (powersupplierrate/100);
                Float govttaxcost = chargingCosts * (govttax/100);
                Float totalcost = chargingCosts * (1 + ((powersupplierrate + govttax)/100));
  

                return singupJSON.createObjectBuilder()
                .add("data", JSON.createObjectBuilder()
                                    .add( "userid", userid)
                                    .add( "podownerid",  podownerid)
                                    .add( "consumedpowerinwatts", consumedpowerinwatts )
                                    .add( "consumedpowerinhours", consumedpowerinhours)
                                    .add( "priceperkwh", pricePerkWh)
                                    .add( "chargingcosts", chargingCosts)
                                    .add( "currency", currency)
                                    .add( "powersupplierrate", powersupplierrate +"%")
                                    .add( "powersuppliercosts", powersuppliercosts)
                                    .add( "govttax", govttax +"%")
                                    .add( "govttaxcost", govttaxcost)
                                    .add( "totalcost", totalcost)
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


	
	public String createData() {

		try {
			OracleCollection col = this.db.admin().createCollection("charges");
			col.admin().truncate();
		} catch(OracleException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "successfully created charges collection !!!";
	}

}