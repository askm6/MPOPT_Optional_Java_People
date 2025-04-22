package utils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import utils.DataValidation;

class DataValidationTest {

    @Test
    void testIsNumber() {
        assertTrue(DataValidation.isNumber('0'));
        assertTrue(DataValidation.isNumber('9'));
        assertFalse(DataValidation.isNumber('a'));
        assertFalse(DataValidation.isNumber(' '));
    }

    @Test
    void testIsLetter() {
        assertTrue(DataValidation.isLetter('a'));
        assertTrue(DataValidation.isLetter('Z'));
        assertTrue(DataValidation.isLetter(' '));  // Espacio en blanco permitido
        assertTrue(DataValidation.isLetter('-'));  // Guión permitido
        assertFalse(DataValidation.isLetter('1'));  // Número no permitido
        assertFalse(DataValidation.isLetter('%'));  // Símbolo especial no permitido
    }

    @Test
    void testCalculateNifLetter_ValidInput() {
        assertEquals("12345678Z", DataValidation.calculateNifLetter("12345678"));
        assertEquals("87654321X", DataValidation.calculateNifLetter("87654321"));
    }

    @Test
    void testCalculateNifLetter_InvalidInput() {
        assertThrows(NumberFormatException.class, () -> DataValidation.calculateNifLetter("ABCDEF"));
    }
}
