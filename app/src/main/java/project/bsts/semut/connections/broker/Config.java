package project.bsts.semut.connections.broker;

import project.bsts.semut.setup.Constants;

public class Config {
    public static final String hostName = Constants.MQ_HOSTNAME;
    public static final String virtualHostname = Constants.MQ_VIRTUAL_HOST;
    public static final String username = Constants.MQ_USERNAME;
    public static final String password = Constants.MQ_PASSWORD;
    public static final int port = Constants.MQ_PORT;
    public static final String exchange = Constants.MQ_EXCHANGE_NAME;
    public static final String rotuingkey = Constants.MQ_DEFAULT_ROUTING_KEY;
    public static final String queuename = Constants.MQ_EXCHANGE_NAME;
}
