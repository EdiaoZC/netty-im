package com.demo.im.entity.response;

import com.demo.im.entity.Packet;
import lombok.Data;

import static com.demo.im.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {


    private boolean success;

    private String userId;

    private String userName;

    private String msg;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
