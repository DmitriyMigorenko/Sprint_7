package ru.praktikum_services.qa_scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.courier.Courier;
import ru.praktikum_services.requests.CourierRequest;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {

    Courier courier = new Courier("Login_Login_123", "New_Password_123", "Alex");
    CourierRequest courierRequest = new CourierRequest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }


    @Test
    @DisplayName("Create courier happy path")
    public void createCourier() {
        courierRequest.setCourier(courier);
        boolean isCourierCreated = courierRequest.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("ok");
        assertTrue(isCourierCreated);
    }

    //Есть баг, не сходится message
    @Test
    @DisplayName("Create duplicate courier")
    public void duplicateCourier() {
        courierRequest.setCourier(courier);
        courierRequest.createCourier();
        courierRequest.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Create courier without login")
    public void createCourierWithoutLogin() {
        courierRequest.setCourier(new Courier("", "123", "Alex"));
        courierRequest.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create courier without password")
    public void createCourierWithoutPassword() {
        courierRequest.setCourier(new Courier("123", "", "Alex"));
        courierRequest.createCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @After
    public void deleteTestData() {
        courierRequest.deleteCourier();
    }
}