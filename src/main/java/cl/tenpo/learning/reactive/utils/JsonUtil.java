package cl.tenpo.learning.reactive.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class JsonUtil {

  private JsonUtil() {}

  public static String escapeJson(String input) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(input).replace("\"", "\\\"");
    } catch (JsonProcessingException e) {
      log.error("Error escaping JSON: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to escape JSON", e);
    }
  }
}
