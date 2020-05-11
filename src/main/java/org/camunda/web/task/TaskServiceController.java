package org.camunda.web.task;

import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.api.task.dto.TaskSearchRq;
import org.camunda.api.task.dto.TaskSearchRs;
import org.camunda.api.task.service.TaskService;
import org.camunda.web.UrlPathConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = UrlPathConsts.rootPath + "/tasks")
public class TaskServiceController {

    private final TaskService taskService;

    @Autowired
    public TaskServiceController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ResponseBody
    @GetMapping
    public TaskSearchRs search(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String assigneeUserId) {

        TaskSearchRq srchRq = new TaskSearchRq();

        srchRq.assigneeUserId = assigneeUserId;
        srchRq.taskId = taskId;

        return taskService.search(srchRq);
    }

    @ResponseBody
    @PostMapping(value = "/{taskId}/completion")
    public TaskCompletionRs complete(@PathVariable String taskId, @RequestBody TaskCompletionRq completionRq) {
        return taskService.complete(taskId, completionRq);
    }
}
