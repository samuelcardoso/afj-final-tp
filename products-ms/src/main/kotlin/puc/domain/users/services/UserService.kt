package puc.domain.users.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.application.consumers.IUserClient
import puc.domain.users.model.User
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(val userClient: IUserClient): IUserService {
    val logger = LoggerFactory.getLogger(this.javaClass)!!

    /* val userClient: UserClient = mock<UserClient>().apply {
        val user = Optional.of(User(1, "JohnDoe", setOf("ROLE_USER")))
        whenever(this.findById(1)).thenReturn(user);
        whenever(this.getMe()).thenReturn(user);
    } */


    override fun getLoggedUser(): User? {
        logger.info("Retrieving authenticated user from user microservice");
        val user = userClient.getLoggedUser().getOrNull();
        logger.info("Authenticated user retrieved from user microservice: $user");
        return user;
    }

}