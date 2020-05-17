package org.camunda.wf.messaging.builder;

import org.camunda.wf.messaging.common.*;
import org.camunda.wf.messaging.message.ServiceTaskMessage;
import org.camunda.wf.messaging.message.UserTaskMessage;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MessageBuilder {

    private final String DEFAULT_VERSION = "1.0.0";

    private final static ThreadLocal<MessageBase> message = new ThreadLocal<>();

    private void assertMessage() {
        if (message.get() == null)
            throw new IllegalArgumentException("Message isn't initialized");
    }

    private void setHeader(MessageBase message) {
        Header header = new Header();
        message.setHeader(header);
        setVersion(message, DEFAULT_VERSION);
    }

    private void setVersion(MessageBase message, String version) {
        message.getHeader().setSchemaVersion(version);
    }

    private void setCurrentTimestamp(MessageBase message) {
        message.getHeader().setTimestamp(new Date());
    }

    private <T extends MessageBase> T assertAndGet(Class<T> type) {

        assertMessage();

        MessageBase msgBase = message.get();

        if(!type.isAssignableFrom(msgBase.getClass()))
        //if(!msgBase.getClass().isAssignableFrom(type))
            throw new IllegalArgumentException("This method cannot be applied to the given message type");

        return (T) msgBase;
    }

    public MessageBuilder serviceTaskMessage() {
        ServiceTaskMessage msg = new ServiceTaskMessage();
        setHeader(msg);
        message.set(msg);
        return this;
    }

    public MessageBuilder userTaskMessage() {
        UserTaskMessage msg = new UserTaskMessage();
        setHeader(msg);
        message.set(msg);
        return this;
    }

    public <T extends MessageBase> MessageBuilder withMessageType(Class<T> messageType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if(!MessageBase.class.isAssignableFrom(messageType))
            throw new IllegalArgumentException("You can only create a message with type which is a subclass of MessageBase");

        T inst = (T)messageType.getDeclaredConstructor().newInstance();
        setHeader(inst);
        message.set(inst);

        return this;
    }

    public MessageBuilder withUniqueID() {
        assertMessage();
        message.get().getHeader().setMessageId(UUID.randomUUID().toString());
        return this;
    }

    public MessageBuilder withSchemaVersion(String version) {
        assertMessage();
        setVersion(message.get(), version);
        return this;
    }

    public MessageBuilder withProcess(String processDefId, String processId) {
        TaskMessage msg = assertAndGet(TaskMessage.class);

        ProcessContext pc = new ProcessContext();
        pc.setProcessDefId(processDefId);
        pc.setProcessId(processId);
        msg.setProcessContext(pc);

        return this;
    }

    public MessageBuilder withTask(String taskDefId, String taskId) {
        TaskMessage msg = assertAndGet(TaskMessage.class);

        TaskContext tc = new TaskContext();
        tc.setTaskDefId(taskDefId);
        tc.setTaskId(taskId);
        msg.setTaskContext(tc);

        return this;
    }

    public MessageBuilder withUserContext(String formId, String assignedUserId) {
        UserTaskMessage msg = assertAndGet(UserTaskMessage.class);

        UserContext uc = new UserContext();
        uc.setFormId(formId);
        uc.setAssignedUserId(assignedUserId);
        msg.setUserContext(uc);

        return this;
    }

    public MessageBuilder withVariables(Map<String, Object> variables) {
        message.get().setVariables(variables);
        return this;
    }

    public MessageBase build() {
        assertMessage();
        return message.get();
    }

    public String buildAsString() {
        assertMessage();
        return message.get().toString();
    }

}
