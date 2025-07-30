package cl.tenpo.learning.reactive.tasks.task2.integration;

import cl.tenpo.learning.reactive.tasks.task2.domain.port.PercentagePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.impl.HistoryServiceImpl;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.AddUserRequest;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.CalculationRequest;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.response.CalculationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)

@AutoConfigureWebTestClient
class ReactiveApiIntegrationTests extends BaseIntegrationTest{

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private PercentagePort percentagePort;
    @Autowired
    private HistoryServiceImpl historyService;

    @Test
    @DisplayName("POST /users - should add user")
    void test1() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AddUserRequest.builder().username("test-user").build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("POST /calculation - should calculate result with percentage")
    void test2() {
        CalculationRequest request = new CalculationRequest(7.0, 3.0);
        when(percentagePort.getPercentage()).thenReturn(Mono.just(15.0));

        webTestClient.post()
                .uri("/calculation")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CalculationResponse.class)
                .value(response -> {
                    assert response.result() == 16.1;
                });
    }

    @Test
    @DisplayName("GET /history - should return history if authorized")
    void test3() {
        webTestClient.get()
                .uri("/history")
                .header("username", "admin")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].endpoint").exists();
    }

    @Test
    @DisplayName("GET /history - should fail if user is not authorized")
    void test4() {
        webTestClient.get()
                .uri("/history")
                .header("username", "unauthorized-user")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
