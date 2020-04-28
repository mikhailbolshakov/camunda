package org.camunda.application.user;

import org.camunda.api.user.dto.GroupRq;
import org.camunda.api.user.dto.UserRq;
import org.camunda.api.user.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.common.application.base.ApplicationServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl extends ApplicationServiceBaseImpl implements UserService {

    @Autowired
    private IdentityService identityService;

    @Override
    public String createUser(UserRq userRq) {

        User newUser = identityService.newUser(userRq.userName);

        newUser.setPassword(userRq.password);
        newUser.setEmail(userRq.email);
        newUser.setId(userRq.userName);
        newUser.setFirstName(userRq.firstName);
        newUser.setLastName(userRq.lastName);

        identityService.saveUser(newUser);

        return newUser.getId();
    }

    @Override
    public User getUser(String userId) {

        User us = identityService
                .createUserQuery()
                .userId(userId)
                .singleResult();

        return us;
    }

    @Override
    public String createGroup(GroupRq groupRq) {

        Group grp = identityService.newGroup(groupRq.groupName);
        grp.setName(groupRq.groupName);
        grp.setType(groupRq.groupType);

        identityService.saveGroup(grp);

        return grp.getId();
    }

    @Override
    public void assign(String userId, String groupId) {
        identityService.createMembership(userId, groupId);
    }
}
