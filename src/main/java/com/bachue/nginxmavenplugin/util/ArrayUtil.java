/**
 * 
 */
package com.bachue.nginxmavenplugin.util;

import java.util.Arrays;

/**
 * Class with array utils
 * @author Alejandro Vivas
 * @version 17/08/2017 0.0.1-SNAPSHOT
 * @since 17/08/2017 0.0.1-SNAPSHOT
 */
public class ArrayUtil
{
	/**
	 * concat to arrays. 
	 * @author Alejandro Vivas
	 * @version 17/08/2017 0.0.1-SNAPSHOT
	 * @since 17/08/2017 0.0.1-SNAPSHOT
	 * @param first First array
	 * @param second Second array
	 * @return concat array
	 */
	public static <T> T[] concat(T[] first, T[] second)
	{
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
