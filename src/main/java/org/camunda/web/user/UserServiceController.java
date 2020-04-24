package org.camunda.web.user;

import org.camunda.api.user.dto.GroupRq;
import org.camunda.api.user.dto.UserRq;
import org.camunda.api.user.service.UserService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/camunda/api/custom/user")
public class UserServiceController implements UserService {

    @Autowired
    private UserService userService;

    @Override
    @PostMapping
    public String createUser(@RequestBody UserRq userRq) {
        return userService.createUser(userRq);
    }

    @Override
    @ResponseBody
    @GetMapping(value = "/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @Override
    @PostMapping(value = "/group")
    public String createGroup(@RequestBody GroupRq group) {
        return userService.createGroup(group);
    }

    @Override
    @PostMapping(value = "/{userId}/group")
    public void assign(@PathVariable String userId, @RequestParam() String groupId) {
        userService.assign(userId, groupId);
    }
}
