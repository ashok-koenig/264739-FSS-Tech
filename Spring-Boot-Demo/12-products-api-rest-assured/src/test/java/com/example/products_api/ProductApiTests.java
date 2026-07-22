package com.example.products_api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

public class ProductApiTests {

    @Test
    void testGetAllProducts(){
        RestAssured.baseURI = "http://localhost:8080";
        given().when().get("/v1/products")
                .then().statusCode(200).body("$.size()",greaterThan(0));
    }
}
