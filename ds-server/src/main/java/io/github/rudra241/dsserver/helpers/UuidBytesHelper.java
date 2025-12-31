package io.github.rudra241.dsserver.helpers;

import java.nio.ByteBuffer;
import java.util.UUID;
import com.google.protobuf.ByteString;
import io.grpc.Status;

public final class UuidBytesHelper {
    public static byte[] getRandomUuidBytes() {
        UUID u = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(u.getMostSignificantBits());
        bb.putLong(u.getLeastSignificantBits());
        return bb.array();
    }
    public static boolean isUuid(byte[] bytes) {
        return (bytes.length == 16);
    }
    public static boolean isUuid(ByteString bs) {
        return isUuid(bs.toByteArray());
    }
    public static byte[] toBytes(ByteString bs) {
        return bs.toByteArray();
    }
    public static ByteString toByteString(byte[] bytes) {
        return ByteString.copyFrom(bytes);
    }
    public static UUID toUuid(byte[] bytes) {
        if (!isUuid(bytes)) throw Status.INVALID_ARGUMENT.withDescription("Not a uuid").asRuntimeException();
        return new UUID(ByteBuffer.wrap(bytes).getLong(), ByteBuffer.wrap(bytes).getLong());
    }
    public static UUID toUuid(ByteString bs) {
        byte[] bytes = bs.toByteArray();
        return toUuid(bytes);
    }
}
