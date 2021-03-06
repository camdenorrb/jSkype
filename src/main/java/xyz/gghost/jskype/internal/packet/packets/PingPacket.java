package xyz.gghost.jskype.internal.packet.packets;

import xyz.gghost.jskype.SkypeAPI;
import xyz.gghost.jskype.internal.packet.PacketBuilder;
import xyz.gghost.jskype.internal.packet.RequestType;

import java.net.URLEncoder;

public class PingPacket {
    private SkypeAPI api;

    public PingPacket(SkypeAPI api) {
        this.api = api;
    }

    public void doNow() {
        PacketBuilder ping = new PacketBuilder(api);
        ping.setType(RequestType.POST);
        ping.setUrl("https://web.skype.com/api/v1/session-ping");
        ping.setData("sessionId=" + api.getUuid().toString());
        ping.setIsForm(true);
        String data = ping.makeRequest();
        if (data == null || data.equals("---")) {
            api.log("Skype login expired... Reconnecting");
            try {
                api.login();
            }catch(Exception e){
                api.log("Failed to reconnect. ");
            }
        }

        PacketBuilder online = new PacketBuilder(api);
        online.setType(RequestType.POST);
        online.setUrl("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/" + URLEncoder.encode(api.getLoginTokens().getEndPoint()) + "/active");        online.setData("{\"timeout\":7}");
        online.makeRequest();

    }

}
