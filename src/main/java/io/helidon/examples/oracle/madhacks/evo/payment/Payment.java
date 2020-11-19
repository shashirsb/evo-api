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
import java.util.Date;
import io.helidon.examples.oracle.madhacks.evo.EvoProvider;

/**
 * An implementation of
 * {@link io.helidon.examples.sockshop.carts.CartRepository} that that uses
 * MongoDB as a backend data store.
 */

@ApplicationScoped@Alternative@Priority(APPLICATION)@Traced
public class Payment  {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory evinfo = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory singupJSON = Json.createBuilderFactory(Collections.emptyMap());

	public static EvoProvider asp = new EvoProvider("test");
	public static OracleDatabase db = asp.dbConnect();

	@Inject
	void Payment() {
		try {
			String UserResponse = createData();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    public JsonObject paymentGateway(String userid, String podownerid, JsonValue chargingcosts, JsonValue powersuppliercosts, JsonValue govttaxcost, JsonValue totalcost, String currency, String paymentmode) {
        try {
           

                JsonObject docObject = JSON.createObjectBuilder()
                .add( "userid", userid)
                .add( "podownerid",  podownerid)
                .add( "chargingcosts", chargingcosts )
                .add( "powersuppliercosts", powersuppliercosts)
                .add( "govttaxcost", govttaxcost)
                .add( "totalcost", totalcost)
                .add( "currency", currency)
                .add( "paymentmode", paymentmode)
                .build();

                // Create a JSON document.
                OracleCollection col = db.admin().createCollection("payment");
				OracleDocument doc = db.createDocumentFromString(docObject.toString());

				// Insert the document into a collection.
                col.insert(doc);
                System.out.println("Inserted successfully ---------------------");

                return singupJSON.createObjectBuilder()
                .add("data", JSON.createObjectBuilder()
                                    .add( "userid", userid)
                                    .add( "podownerid",  podownerid)
                                    .add( "totalcost", totalcost)
                                    .add( "currency", currency)
                                    .add( "paymentmode", paymentmode)
                                    .add( "status", "payment" + paymentmode + " was completed at" + new Date().getTime())
                                    .build())
                .build();      

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