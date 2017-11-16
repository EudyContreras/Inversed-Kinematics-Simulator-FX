package com.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

 
	@SafeVarargs
	public static <T> T[] MERGE(T[]... arrays) {
		Class<?> componentType = arrays[0].getClass().getComponentType();
		int size = 0;
		for (T[] array : arrays) {
			size += array.length;
		}
		@SuppressWarnings("unchecked")
		T[] result = (T[]) arrays[0].getClass().cast(Array.newInstance(componentType, size));
		int pos = 0;
		for (T[] array : arrays) {
			System.arraycopy(array, 0, result, pos, array.length);
			pos += array.length;
		}
		return result;
	}

    public static <T> T[] CONCAT(T[] first, T[] second) {
        if (first == null && second != null) {
            return second;
        } else if (second == null && first != null) {
            return first;
        }
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


	@SafeVarargs
	public static <T> T[] MERGE_ALT(T[] ... arrays) {
	    final List<T> list = new ArrayList<>();
	    for (T[] array : arrays) {
	      list.addAll(Arrays.asList(array));
	    }
	    return list.toArray(arrays[0]);
	}

}
