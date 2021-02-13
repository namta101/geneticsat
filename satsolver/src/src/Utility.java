package src;

public final class      Utility {
    private Utility() {
        throw new UnsupportedOperationException();
    }

    public static int[] convertStringArrayToIntArray(String[] stringArray) {
        int size = stringArray.length;
        int[] intArray = new int[size];
        for (int i = 0; i < size; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }
}
