/*
 * Copyright 2021 John Yeary <jyeary@bluelotussoftware.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluelotussoftware.jakarta.rest;

import com.bluelotussoftware.jakarta.sqlite.model.Person;
import com.bluelotussoftware.jakarta.sqlite.service.PersonService;
import com.bluelotussoftware.utils.Environment;
import jakarta.persistence.EntityManagerFactory;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 */
@Path("person")
public class PersonResource {

    private Environment env;

    public PersonResource() {
        env = new Environment();
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "Person data",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "404",
                        description = "No Persons found",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON))
            })
    @Operation(summary = "Display all of the Person objects in SQLite DB")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory factory = env.getEntityManagerFactory();
        PersonService service = new PersonService(factory.createEntityManager());
        List<Person> persons = service.findAll();
        if (persons != null) {
            return Response.ok(persons).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "GET a specific Person by ID",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "404",
                        description = "No Person with id found",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON))
            })
    @Operation(summary = "Display a specific Person object from SQLite DB")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(
            @Parameter(
                    description = "The id of the person to find.",
                    required = true,
                    example = "1",
                    schema = @Schema(type = SchemaType.INTEGER))
            @PathParam(value = "id") int id) {
        EntityManagerFactory factory = env.getEntityManagerFactory();
        PersonService service = new PersonService(factory.createEntityManager());
        Person p = service.find(id);
        if (p != null) {
            return Response.ok(p).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "201",
                        description = "Created Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "500",
                        description = "A server occurred while creating Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON))
            })
    @Operation(summary = "Create Person object in SQLite DB")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context final UriInfo uriInfo,
            @Parameter(
                    description = "The person object to create.",
                    required = true,
                    example = "{firstName:\"John\",lastName:\"Doe\",localeString:\"en_US\",phoneNumber:\"1234567890\"}",
                    schema = @Schema(type = SchemaType.OBJECT)) Person person) {
        EntityManagerFactory factory = env.getEntityManagerFactory();
        PersonService service = new PersonService(factory.createEntityManager());
        service.create(person);

        if (person.getId() != 0) {
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(PersonResource.class)
                    .path("{id}")
                    .build(person.getId());
            return Response.created(uri).entity(person).build();
        } else {
            return Response.serverError().build();
        }
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "Updated Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "500",
                        description = "A server occurred while updating Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON))
            })
    @Operation(summary = "Update Person object in SQLite DB")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Parameter(
                    description = "The person object to update.",
                    required = true,
                    example = "{firstName:\"John\",id:6,lastName:\"Doe\",localeString:\"en_US\",phoneNumber:\"1234567890\"}",
                    schema = @Schema(type = SchemaType.OBJECT)) Person person) {
        EntityManagerFactory factory = env.getEntityManagerFactory();
        PersonService service = new PersonService(factory.createEntityManager());
        person = service.update(person);
        return Response.ok(person).build();
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "Deleted Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "400",
                        description = "Bad Request, check your Person object.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON)),
                @APIResponse(
                        responseCode = "500",
                        description = "A server occurred while deleting Person",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON))
            })
    @Operation(summary = "Update Person object in SQLite DB")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @Parameter(
                    description = "The person object to delete.",
                    required = true,
                    example = "{firstName:\"John\",id:6,lastName:\"Doe\",localeString:\"en_US\",phoneNumber:\"1234567890\"}",
                    schema = @Schema(type = SchemaType.OBJECT)) Person person) {
        EntityManagerFactory factory = env.getEntityManagerFactory();
        PersonService service = new PersonService(factory.createEntityManager());
        service.delete(person);
        return Response.ok().build();
    }
}
