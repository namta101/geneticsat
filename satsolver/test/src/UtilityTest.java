package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    public void ConvertStringArrayToIntArray_EmptyArray_ReturnsEmptyArray(){
        String[] strings = new String[]{};
        int[] ints = Utility.convertStringArrayToIntArray(strings);
        Assertions.assertArrayEquals(new int[]{}, ints);
    }

    @Test
    public void ConvertStringArrayToIntArray_NonEmptyArray_ReturnsNonEmptyArray(){
        String[] strings = new String[]{"0", "-2", "10", "20", "40"};
        int[] ints = Utility.convertStringArrayToIntArray(strings);
        Assertions.assertArrayEquals(new int[]{0, -2, 10, 20, 40}, ints);
    }

}