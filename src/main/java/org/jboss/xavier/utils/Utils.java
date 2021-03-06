package org.jboss.xavier.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.UUID;

public class Utils {

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static Optional<JsonNode> getFieldValueFromJsonNode(JsonNode node, String ...fieldName) {
        for (String s : fieldName) {
            if (node != null) {
                node = node.get(s);
            }
        }

        return Optional.ofNullable(node);
    }

}
