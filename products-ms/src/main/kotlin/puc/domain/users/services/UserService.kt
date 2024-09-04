package puc.domain.users.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import puc.application._shared.IUserClient
import puc.domain.users.model.User
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(val userClient: IUserClient): IUserService {
    val logger = LoggerFactory.getLogger(this.javaClass)!!


    override fun getAuthenticatedUser(): User? {
        logger.info("Retrieving authenticated user from user microservice");
        val user = userClient.getAuthenticatedUser().getOrNull();
        logger.info("Authenticated user retrieved from user microservice: $user");
        return user;
    }

}