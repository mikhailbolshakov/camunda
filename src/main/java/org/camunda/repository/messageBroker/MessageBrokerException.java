package org.camunda.repository.messageBroker;

public class MessageBrokerException extends Exception {

    public MessageBrokerException(String errorMessage) {
        super(errorMessage);
    }

    public MessageBrokerException(String errorMessageFmt, Object... args) {
        super(String.format(errorMessageFmt, args));
    }

    public MessageBrokerException(Throwable e, String errorMessage){
        super(errorMessage, e);
    }

    public MessageBrokerException(Throwable e, String errorMessageFmt, Object... args){
        super(String.format(errorMessageFmt, args), e);
    }

}
