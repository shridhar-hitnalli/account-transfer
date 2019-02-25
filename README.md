
# Account transfer REST
Test application implementing a RESTful API for money transfers between accounts

Technologies
------------

* JAX-RS API
* Jersey
* Jetty server
* Maven

### API
Application listen to port: 9999

1. @POST http://localhost:9999/api/accounts
2. @GET http://localhost:9999/api/accounts
3. @GET http://localhost:9999/api/accounts/{id}
4. @GET http://localhost:9999/api/accounts/{id}/balance
5. @POST http://localhost:9999/api/accounts/transfer
6. @PUT http://localhost:9999/api/accounts/{id}/deposit
7. @PUT http://localhost:9999/api/accounts/{id}/withdraw
8. @DELETE http://localhost:9999/api/accounts/{id}/withdraw


Running the Application
-----------------------

Run the application as follows:
>     mvn clean package exec:java

Tests
-----

The application includes an integration test AccountControllerTest which starts the jetty Server using the base URI http://localhost:9998/ and runs test cases.


