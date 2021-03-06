package aps2.reversestringfindmax;

public class ReverseStringFindMax {
	/**
	 * This function takes the string argument and reverses it.
	 * 
	 * @param str Input string.
	 * @return Reverse version of the string or null, if str is null.
	 */
	public String reverseString(String str) {
		//throw new UnsupportedOperationException("You need to implement this function!");
        if (str == null){
            return null;
        }
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * This function finds and returns the maximum element in the given array.
	 * 
	 * @param arr Initialized input array.
	 * @return The maximum element of the given array, or the minimum Integer value, if array is empty.
	 */
	public int findMax(int[] arr){
		//throw new UnsupportedOperationException("You need to implement this function!");
        if (arr.length > 0) {
            int max = arr[0];
            for (int el : arr) {
                if (el > max) {
                    max = el;
                }
            }
            return max;
        }
        return Integer.MIN_VALUE;
	}
}
