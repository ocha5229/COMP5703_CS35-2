package com.cs35.common.utils.uuid;

import java.util.concurrent.atomic.AtomicInteger;
import com.cs35.common.utils.DateUtils;
import com.cs35.common.utils.StringUtils;

/**
 * @author cs35 
 */
public class Seq
{
    // 
    public static final String commSeqType = "COMMON";

    // 
    public static final String uploadSeqType = "UPLOAD";

    // 
    private static AtomicInteger commSeq = new AtomicInteger(1);

    // 
    private static AtomicInteger uploadSeq = new AtomicInteger(1);

    // 
    private static final String machineCode = "A";

    /**
     * 
     * 
     * @return 
     */
    public static String getId()
    {
        return getId(commSeqType);
    }
    
    /**
     * 16 yyMMddHHmmss +  + 3
     * 
     * @return 
     */
    public static String getId(String type)
    {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type))
        {
            atomicInt = uploadSeq;
        }
        return getId(atomicInt, 3);
    }

    /**
     *  yyMMddHHmmss +  + length
     * 
     * @param atomicInt 
     * @param length 
     * @return 
     */
    public static String getId(AtomicInteger atomicInt, int length)
    {
        String result = DateUtils.dateTimeNow();
        result += machineCode;
        result += getSeq(atomicInt, length);
        return result;
    }

    /**
     * [1, 10  (length)), 0length
     * 
     * @return 
     */
    private synchronized static String getSeq(AtomicInteger atomicInt, int length)
    {
        // +1
        int value = atomicInt.getAndIncrement();

        // >=10  (length)1
        int maxSeq = (int) Math.pow(10, length);
        if (atomicInt.get() >= maxSeq)
        {
            atomicInt.set(1);
        }
        // ，0
        return StringUtils.padl(value, length);
    }
}
