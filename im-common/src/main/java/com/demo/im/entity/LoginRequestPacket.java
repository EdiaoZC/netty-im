package com.demo.im.entity;

import com.demo.im.command.Command;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {
    private Integer id;

    private String username;

    private String password;


    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
