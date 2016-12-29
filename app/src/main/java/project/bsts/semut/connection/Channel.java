package project.bsts.semut.connection;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import project.bsts.semut.config.Config;

/**
 * Created by hynra on 12/29/16.
 */

public class Channel {
    public static ManagedChannel buildChannel(){
        ManagedChannel mChannel = ManagedChannelBuilder.forAddress(Config.GRPC_HOST, Config.GRPC_PORT)
                .usePlaintext(true)
                .build();
        return  mChannel;
    }
}
