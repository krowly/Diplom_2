import BaseTests.BaseOrderTest;
import Model.PojoOrder;
import Model.PojoUser;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static BaseTests.BaseUserTest.getAuthPj;
import static RestApis.RequestsOrders.orderCreateAuthorized;
import static RestApis.RequestsOrders.orderCreateUnauthorized;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class OrderCreation extends BaseOrderTest {
    PojoOrder order;
    @Before
    @Step("Cоздание заказа")
    public void Start() {
        order = makeOrder();
    }
    @Test
    @DisplayName("Тест Создание заказа пользователя")
    public void createOrderAuthorized() {
        PojoUser pj = getAuthPj();
        Response r = orderCreateAuthorized(order, pj.getAccessToken())
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract().response();
        assertTrue(r.jsonPath().get("name").toString().isEmpty());
        assertEquals(true, r.jsonPath().get("success"));
        assertTrue((Integer) r.path("order.number") > 0);
    }
    @Test
    @DisplayName("Тест Создание заказа без пользователя")
    public void createOrderUnauthorizedWithIngredients() {
        orderCreateUnauthorized(order)
                .then()
                .assertThat()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Тест Создание заказа с Wrong Hash")
    public void createOrderWithBadHash()
    {
        order.getIngredients().add("NotAHash");
        orderCreateUnauthorized(order)
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @Test
    @DisplayName("Тест Создание заказа без Ingredients")
    public void createOrderWithoutIngredients() {
        order.getIngredients().clear();
        Response r = orderCreateUnauthorized(order)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .extract().response();
        assertEquals(false, r.jsonPath().get("success"));
        assertEquals("Ingredient ids must be provided", r.jsonPath().get("message"));
    }
}
