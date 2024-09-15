package com.am.crm.customer.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UUIDUtilTest {

    @Test
    void testToUUID_validUUID() {
        String validUUID = "123e4567-e89b-12d3-a456-426614174000";
        UUID result = UUIDUtil.toUUID(validUUID);
        assertEquals(UUID.fromString(validUUID), result);
    }

    @Test
    void testToUUID_invalidUUID() {
        String invalidUUID = "not-a-uuid";
        assertThrows(IllegalArgumentException.class, () -> UUIDUtil.toUUID(invalidUUID));
    }

    @Test
    void testToUUID_nullInput() {
        assertThrows(IllegalArgumentException.class, () -> UUIDUtil.toUUID(null));
    }

    @Test
    void testToUUID_emptyInput() {
        assertThrows(IllegalArgumentException.class, () -> UUIDUtil.toUUID(""));
    }

    @Test
    void testToUUID_blankInput() {
        assertThrows(IllegalArgumentException.class, () -> UUIDUtil.toUUID("   "));
    }
}
