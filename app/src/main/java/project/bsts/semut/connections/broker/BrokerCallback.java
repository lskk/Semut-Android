package project.bsts.semut.connections.broker;

public interface BrokerCallback {
    void onMQConnectionFailure(String message);

    void onMQDisconnected();

    void onMQConnectionClosed(String message);
}