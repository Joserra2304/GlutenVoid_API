package com.svalero.glutenvoid.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;

@JsonDeserialize(using = GlutenCondition.Deserializer.class)
public enum GlutenCondition {
    Sensibilidad, Ninguna, Celiaquia, Alergia;

    public static class Deserializer extends JsonDeserializer<GlutenCondition> {
        @Override
        public GlutenCondition deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String key = jsonParser.getText().toLowerCase();
            switch (key) {
                case "sensibilidad":
                    return Sensibilidad;
                case "ninguna":
                    return Ninguna;
                case "celiaquia":
                    return Celiaquia;
                case "alergia":
                    return Alergia;
                default:
                    throw new IllegalArgumentException("Unknown value: " + key);
            }
        }
    }
}
