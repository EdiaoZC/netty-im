package com.skyeidos.im.entity.request;

import com.skyeidos.im.entity.Packet;
import lombok.Data;

import static com.skyeidos.im.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String userName;

    private String password;


    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
