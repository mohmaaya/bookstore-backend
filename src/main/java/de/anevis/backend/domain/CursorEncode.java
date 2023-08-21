package de.anevis.backend.domain;

import java.nio.ByteBuffer;
import java.util.Base64;

public class CursorEncode {
    public static String encodeId(long id) {
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putLong(id).array());
    }

    public static long decodeCursor(String cursor) {
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        return ByteBuffer.wrap(decodedBytes).getLong();
    }
}
