package ru.praktikum_services.qa_scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.requests.OrderRequest;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {
    OrderRequest orderRequest = new OrderRequest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Test
    @DisplayName("Get order happy path")
    public void getOrder() {
        orderRequest.getOrder()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("orders", notNullValue());
    }
}