package org.camunda.application.task;

import org.camunda.api.task.dto.*;
import org.camunda.api.task.service.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.common.application.base.ApplicationServiceBaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TaskServiceImpl extends ApplicationServiceBaseImpl implements TaskService {

    private final org.camunda.bpm.engine.TaskService camundaTaskService;

    @Autowired
    public TaskServiceImpl(org.camunda.bpm.engine.TaskService camundaTaskService) {
        this.camundaTaskService = camundaTaskService;
    }

    @Override
    public TaskSearchRs search(TaskSearchRq taskSearchRq) {

        TaskSearchRs rs = new TaskSearchRs();
        boolean filterSet = false;

        TaskQuery qry = camundaTaskService.createTaskQuery();

        if (!StringUtils.isEmpty(taskSearchRq.assigneeUserId)) {
            qry.taskAssignee(taskSearchRq.assigneeUserId);
            filterSet = true;
        }

        if (!StringUtils.isEmpty(taskSearchRq.taskId)) {
            qry.taskId(taskSearchRq.taskId);
            filterSet = true;
        }

        if (filterSet) {
            for (Task task : qry.list()) {
                TaskDto taskDto = new TaskDto();
                taskDto.id = task.getId();
                taskDto.name = task.getName();
                taskDto.assigneeUserId = task.getAssignee();
                taskDto.formKey = task.getFormKey();
                taskDto.processInstanceId = task.getProcessInstanceId();
                rs.Items.add(taskDto);
            }
        }
        else {
            throw new IllegalArgumentException("Filters aren't specified");
        }

        return rs;
    }

    @Override
    public TaskCompletionRs complete(String taskId, TaskCompletionRq taskCompletionRq) {

        TaskCompletionRs rs = new TaskCompletionRs();

        if (taskCompletionRq.variables != null && !taskCompletionRq.variables.isEmpty())
            camundaTaskService.complete(taskId, taskCompletionRq.variables);
        else
            camundaTaskService.complete(taskId);

        return rs;

    }


}
