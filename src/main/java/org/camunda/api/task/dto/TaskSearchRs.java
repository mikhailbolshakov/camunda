package org.camunda.api.task.dto;

import java.util.ArrayList;
import java.util.List;

public class TaskSearchRs {
    public List<TaskDto> Items;

    public TaskSearchRs() {
        Items = new ArrayList<>();
    }
}
