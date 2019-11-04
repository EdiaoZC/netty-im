package com.skyeidos.im.entity.request;

import com.skyeidos.im.entity.Packet;
import lombok.Data;

import static com.skyeidos.im.command.Command.MESSAGE_REQUEST;

@Data
public class MessageRequestPacket extends Packet {


    private String userId;

    private String userName;

    private String message;

    public MessageRequestPacket(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
