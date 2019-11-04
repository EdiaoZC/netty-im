package com.skyeidos.im.entity.response;

import com.skyeidos.im.entity.Packet;
import lombok.Data;

import static com.skyeidos.im.command.Command.LOGIN_RESPONSE;

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
