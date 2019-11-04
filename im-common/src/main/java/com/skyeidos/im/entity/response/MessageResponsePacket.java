package com.skyeidos.im.entity.response;


import com.skyeidos.im.entity.Packet;
import lombok.Data;

import static com.skyeidos.im.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {

    private String userId;

    private String userName;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
