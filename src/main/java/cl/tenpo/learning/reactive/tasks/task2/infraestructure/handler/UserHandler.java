
package cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler;

import cl.tenpo.learning.reactive.tasks.task2.adapter.UserAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.AuthorizationService;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.AddUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final AuthorizationService userService;
    private final UserAdapter userAdapter;

    public Mono<ServerResponse> addUser(ServerRequest request) {
        return request.bodyToMono(AddUserRequest.class)
                .map(userAdapter::toModel)
                .flatMap(userService::addUser)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> removeUser(ServerRequest request) {
        return Mono.just(request.pathVariable("username"))
                .flatMap(userName -> userService.removeUser(userName)
                .then(ServerResponse.noContent().build())
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage())));
    }
}