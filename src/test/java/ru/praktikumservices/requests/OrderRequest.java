package ru.praktikumservices.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import ru.praktikumservices.qascooter.order.Order;

import static io.restassured.RestAssured.given;

public class OrderRequest {
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Step("Create order")
    public Response createOrder() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post();
    }

    @Step("Get order")
    public Response getOrder() {
        return given().log().all().get();
    }
}