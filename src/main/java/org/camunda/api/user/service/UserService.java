package org.camunda.api.user.service;

import org.camunda.api.user.dto.GroupRq;
import org.camunda.api.user.dto.UserRq;
import org.camunda.bpm.engine.identity.User;

public interface UserService {

    // create a new user
    public String createUser(UserRq userRq);

    // get user by Id
    public User getUser(String userId);

    // create a group
    public String createGroup(GroupRq group);

    // assign an user to a group
    public void assign(String userId, String groupId);

}
