package org.camunda.api.task.service;

import org.camunda.api.task.dto.TaskCompletionRq;
import org.camunda.api.task.dto.TaskCompletionRs;
import org.camunda.api.task.dto.TaskSearchRq;
import org.camunda.api.task.dto.TaskSearchRs;

public interface TaskService {

    // searches for tasks
    public TaskSearchRs search(TaskSearchRq taskSearchRq);

    // completes a task
    public TaskCompletionRs complete(String taskId, TaskCompletionRq taskCompletionRq);

}
