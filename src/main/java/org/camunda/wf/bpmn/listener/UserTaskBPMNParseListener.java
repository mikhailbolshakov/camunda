package org.camunda.wf.bpmn.listener;

import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.util.xml.Namespace;
import org.camunda.common.spring.ApplicationContextProvider;
import org.camunda.wf.bpmn.consts.BPMNTemplateConsts;
import org.camunda.wf.userTask.UserTaskCreateListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserTaskBPMNParseListener extends AbstractBpmnParseListener {

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addUserTaskCreateListener(userTaskElement, activity);
    }

    private boolean createListenerExists(Element userTaskElement) {

        Element extensionElements = userTaskElement.element("extensionElements");
        if (extensionElements != null) {

            List<Element> listeners = extensionElements.elements("taskListener");

            if (listeners != null) {

                for (Element listener : listeners) {
                    if (TaskListener.EVENTNAME_CREATE.equals(listener.attribute("event"))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isTemplateApplied(Element userTaskElement, String template) {
        Namespace ns = new Namespace("http://camunda.org/schema/1.0/bpmn");
        String appliedTemplate = userTaskElement.attributeNS(ns, "modelerTemplate");
        return template.equals(appliedTemplate);
    }

    private void addUserTaskCreateListener(Element userTaskElement, ActivityImpl activity) {

        if (isTemplateApplied(userTaskElement, BPMNTemplateConsts.USER_TASK_EXTERNAL)) {

            UserTaskCreateListener createListener = ApplicationContextProvider.getContext().getBean(UserTaskCreateListener.class);

            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
            taskDefinition.addBuiltInTaskListener(TaskListener.EVENTNAME_CREATE, createListener);

        }
    }

}
