package org.camunda.web.user;

import org.camunda.api.user.dto.GroupRq;
import org.camunda.api.user.dto.UserRq;
import org.camunda.api.user.service.UserService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.web.UrlPathConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UrlPathConsts.rootPath + "/user")
public class UserServiceController implements UserService {

    private final UserService userService;

    @Autowired
    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

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
