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

import java.time.LocalDateTime;

import java.util.Collections;

import io.helidon.examples.oracle.madhacks.evo.* ;

/**
 * An implementation of
 * {@link io.helidon.examples.sockshop.carts.CartRepository} that that uses
 * MongoDB as a backend data store.
 */

@ApplicationScoped@Alternative@Priority(APPLICATION)@Traced
public class User  {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final JsonBuilderFactory evinfo = Json.createBuilderFactory(Collections.emptyMap());

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