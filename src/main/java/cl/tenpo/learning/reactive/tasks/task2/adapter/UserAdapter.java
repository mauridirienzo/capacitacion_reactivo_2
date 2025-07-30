package cl.tenpo.learning.reactive.tasks.task2.adapter;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity.AuthorizedUser;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.AddUserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public String toModel(AddUserRequest addUserRequest){
        return addUserRequest.username();
    }
    public User toModel(String username){
        return User.builder()
                .username(username)
                .build();
    }

    public User toModel(AuthorizedUser authorizedUser){
        return User.builder()
                .id(authorizedUser.id())
                .username(authorizedUser.username())
                .build();
    }

    public AuthorizedUser toEntity(User user){
        return AuthorizedUser.builder()
                .username(user.username())
                .build();
    }
}
