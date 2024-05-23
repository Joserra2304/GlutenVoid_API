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
                case "sensitivity":
                    return Sensibilidad;
                case "ninguna":
                case "none":
                    return Ninguna;
                case "celiaquia":
                case "coeliac":
                case "celiac":
                    return Celiaquia;
                case "alergia":
                case "alergy":
                    return Alergia;
                default:
                    throw new IllegalArgumentException("Unknown value: " + key);
            }
        }
    }
}
