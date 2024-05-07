package ru.praktikum_services.qa_scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum_services.qa_scooter.order.Order;
import ru.praktikum_services.requests.OrderRequest;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    Order order;
    OrderRequest orderRequest = new OrderRequest();

    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new Order("Jerry", "Smith", "Nord Avenu, 69", "45", "045-233-14", 5, "2025-12-13", "Very Fast, please!", new String[]{"BLACK"})},
                {new Order("Pam", "Smith", "Nord Avenu, 69", "45", "045-233-14", 5, "2025-02-13", "Very Fast, please!", new String[]{"BLACK, GREY"})},
                {new Order("Morty", "Smith", "Nord Avenu, 69", "45", "045-233-14", 5, "2025-11-13", "Very Fast, please!", new String[]{})}
        };
    }

    @Test
    @DisplayName("Create order happy path")
    public void createOrder() {
        orderRequest.setOrder(order);
        orderRequest.createOrder()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue());
    }
}