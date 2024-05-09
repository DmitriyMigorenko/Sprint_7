package ru.praktikumservices.qascooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.courier.Courier;
import ru.praktikumservices.requests.CourierRequest;

import java.net.HttpURLConnection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    Courier courier = new Courier("Admin_Login123", "Password123_Qwerty");
    CourierRequest courierRequest = new CourierRequest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }


    @Test
    @DisplayName("Login courier happy path")
    public void loginCourier() {
        courierRequest.setCourier(courier);
        courierRequest.createCourier();
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Login courier without login")
    public void loginCourierWithoutLogin() {
        courierRequest.setCourier(new Courier("", "123"));
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login courier without password")
    public void loginCourierWithoutPassword() {
        courierRequest.setCourier(new Courier("Login", ""));
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login unknown courier")
    public void loginUnknownCourier() {
        courierRequest.setCourier(courier);
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login courier with wrong login")
    public void loginCourierWithWrongLogin() {
        courierRequest.setCourier(courier);
        courierRequest.createCourier();
        courierRequest.setCourier(new Courier("New_Login_123", "Password123_Qwerty"));
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
        courierRequest.setCourier(courier);
    }

    @Test
    @DisplayName("Login courier with wrong password")
    public void loginCourierWithWrongPassword() {
        courierRequest.setCourier(courier);
        courierRequest.createCourier();
        courierRequest.setCourier(new Courier("Admin_Login123", "123"));
        courierRequest.loginCourier()
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
        courierRequest.setCourier(courier);
    }


    @After
    public void deleteTestData() {
        courierRequest.deleteCourier();
    }
}
