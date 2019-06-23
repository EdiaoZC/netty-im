package com.demo.im.biz.entity;

import com.demo.im.entity.Session;
import io.netty.util.AttributeKey;

public final class Attributes {

    private Attributes(){

    }

    public static final AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("LOGIN");

    public static final AttributeKey<Session> SESSION = AttributeKey.newInstance("Session");
}
