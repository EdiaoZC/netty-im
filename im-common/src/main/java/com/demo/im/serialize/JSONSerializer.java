package com.demo.im.serialize;

import com.alibaba.fastjson.JSON;

public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }


}
