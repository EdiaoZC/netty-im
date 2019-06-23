package com.demo.im.entity.response;


import com.demo.im.entity.Packet;
import lombok.Data;

import static com.demo.im.command.Command.MESSAGE_RESPONSE;

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
