
package io.helidon.examples.oracle.madhacks.evo;

import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import io.helidon.examples.oracle.madhacks.evo.*;
// import io.helidon.examples.oracle.madhacks.evo.User;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 */
@Path("/evo")
@RequestScoped
public class EvoResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());


    /**
     * The greeting message provider.
     */
    private final EvoProvider evoProvider;

    /**
     * Using constructor injection to get a configuration property.
     * By default this gets the value from META-INF/microprofile-config
     *
     * @param evoConfig the configured greeting message
     */
    @Inject
    public EvoResource(EvoProvider evoConfig) {
        this.evoProvider = evoConfig;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() {
        return createResponse("World");
    }

    /**
     * Return a greeting message using the name that was provided.
     *
     * @param name the name to greet
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getMessage(@PathParam("name") String name) {
        return createResponse(name);
    }

    
       /**
     * Return a greeting message using the name that was provided.
     *
     * @param name the name to greet
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Path("/singup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(name = "userid",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.STRING, example = "{\"userid\" : \"shashi\"}")))
    @APIResponses({
            @APIResponse(name = "normal", responseCode = "204", description = "User Singup successfull!!"),
            @APIResponse(name = "missing 'User'", responseCode = "400",
                    description = "JSON did not contain setting for 'user'")})
    public Response singUpUser(JsonObject jsonObject) {
        System.out.println("Inside signup rest api");
        if (!jsonObject.containsKey("userid") || !jsonObject.containsKey("password") || !jsonObject.containsKey("mobile")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("error", "No userid provided")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        User user =new User();
        JsonObject singUpUser = user.signUpUser(jsonObject.getString("userid"),jsonObject.getString("password"),jsonObject.getString("mobile"));

        System.out.println(singUpUser.getString("exists"));
       // System.out.println(singUpUser.getString("exists").toString());

        if (singUpUser.getString("exists") == "true") {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("info", "userid already exists")
                    .add("singup", "declined")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }  else if (!singUpUser.containsKey("exists")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("info", "something went wrong")
                    .add("singup", "declined")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        JsonObject authReponse = JSON.createObjectBuilder()
                    .add("singup", "success")
                    .add("info", "userid was created")
                    .add("data", singUpUser.getString("data"))
                    .build();

        return Response.status(Response.Status.OK ).entity(authReponse).build();

      
    }

      /**
     * Return a greeting message using the name that was provided.
     *
     * @param name the name to greet
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(name = "userid",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.STRING, example = "{\"userid\" : \"shashi\"}")))
    @APIResponses({
            @APIResponse(name = "normal", responseCode = "204", description = "User login successfull!!"),
            @APIResponse(name = "missing 'User'", responseCode = "400",
                    description = "JSON did not contain setting for 'user'")})
    public Response authUser(JsonObject jsonObject) {
            System.out.println("Inside login rest api");
        if (!jsonObject.containsKey("userid")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("error", "No userid provided")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        String _userid = jsonObject.getString("userid");
        User user =new User();
        JsonObject loginUser = user.findUser(_userid.toString());

        if (!loginUser.containsKey("mobile")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("authinfo", "userid does not exists")
                    .add("auth", "declined")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        JsonObject authReponse = JSON.createObjectBuilder()
                    .add("auth", "success")
                    .add("authinfo", "userid was found")
                    .add("data", loginUser)
                    .build();

        return Response.status(Response.Status.OK ).entity(authReponse).build();

      
    }

       /**
     * Return a greeting message using the name that was provided.
     *
     * @param name the name to greet
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Path("/justme")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(name = "userid",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.STRING, example = "{\"userid\" : \"shashi\"}")))
    @APIResponses({
            @APIResponse(name = "normal", responseCode = "204", description = "User login successfull!!"),
            @APIResponse(name = "missing 'User'", responseCode = "400",
                    description = "JSON did not contain setting for 'user'")})
    public Response justme(JsonObject jsonObject) {
            System.out.println("Inside login rest api");
        if (!jsonObject.containsKey("userid")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("error", "No userid provided")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        String _userid = jsonObject.getString("userid");
        User user =new User();
        JsonObject loginUser = user.findUser(_userid.toString());

        if (!loginUser.containsKey("mobile")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("authinfo", "userid does not exists")
                    .add("auth", "declined")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        JsonObject authReponse = JSON.createObjectBuilder()
                    .add("auth", "success")
                    .add("authinfo", "userid was found")
                    .add("data", loginUser)
                    .build();

        return Response.status(Response.Status.OK ).entity(authReponse).build();

      
    }


    /**
     * Set the greeting to use in future messages.
     *
     * @param jsonObject JSON containing the new greeting
     * @return {@link Response}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @Path("/greeting")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(name = "greeting",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.STRING, example = "{\"greeting\" : \"Hola\"}")))
    @APIResponses({
            @APIResponse(name = "normal", responseCode = "204", description = "Greeting updated"),
            @APIResponse(name = "missing 'greeting'", responseCode = "400",
                    description = "JSON did not contain setting for 'greeting'")})
    public Response updateGreeting(JsonObject jsonObject) {

        if (!jsonObject.containsKey("greeting")) {
            JsonObject entity = JSON.createObjectBuilder()
                    .add("error", "No greeting provided")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }

        String newGreeting = jsonObject.getString("greeting");

        evoProvider.setMessage(newGreeting);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private JsonObject createResponse(String who) {
        String msg = String.format("%s %s!", evoProvider.getMessage(), who);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }
    
}
