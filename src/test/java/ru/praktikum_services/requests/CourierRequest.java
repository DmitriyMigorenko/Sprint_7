package ru.praktikum_services.requests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import ru.praktikum_services.qa_scooter.courier.Courier;

import static io.restassured.RestAssured.given;

public class CourierRequest {
    private Courier courier;

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }

    @Step("Create courier")
    public Response createCourier() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post();
    }

    @Step("Login courier")
    public Response loginCourier() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post("/login");
    }

    @Step("Delete courier")
    public void deleteCourier() {
        Integer courierId = loginCourier()
                .then().log().all()
                .extract()
                .path("id");
        if (courierId != null) {
            given().delete("/" + courierId);
        }
    }
}