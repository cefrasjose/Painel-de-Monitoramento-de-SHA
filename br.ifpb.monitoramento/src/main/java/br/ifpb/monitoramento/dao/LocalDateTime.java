package br.ifpb.monitoramento.dao;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class LocalDateTime implements JsonSerializer<java.time.LocalDateTime>, JsonDeserializer<java.time.LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(java.time.LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        //Converte o objeto LocalDateTime para String (ex: "2023-12-10T15:30:00")
        return new JsonPrimitive(formatter.format(src));
    }

    @Override
    public java.time.LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //Converte a String do JSON de volta para LocalDateTime
        return java.time.LocalDateTime.parse(json.getAsString(), formatter);
    }
}