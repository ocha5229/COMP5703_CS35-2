package com.cs35.common.utils.uuid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import com.cs35.common.exception.UtilException;

/**
 * （universally unique identifier）（UUID）
 *
 * @author cs35
 */
public final class UUID implements java.io.Serializable, Comparable<UUID>
{
    private static final long serialVersionUID = -1185015143654744140L;

    /**
     * SecureRandom 
     *
     */
    private static class Holder
    {
        static final SecureRandom numberGenerator = getSecureRandom();
    }

    /** UUID64 */
    private final long mostSigBits;

    /** UUID64 */
    private final long leastSigBits;

    /**
     * 
     * 
     * @param data 
     */
    private UUID(byte[] data)
    {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++)
        {
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i < 16; i++)
        {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    /**
     *  UUID。
     *
     * @param mostSigBits  {@code UUID}  64 
     * @param leastSigBits  {@code UUID}  64 
     */
    public UUID(long mostSigBits, long leastSigBits)
    {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    /**
     *  4（）UUID 。
     * 
     * @return  {@code UUID}
     */
    public static UUID fastUUID()
    {
        return randomUUID(false);
    }

    /**
     *  4（）UUID 。  UUID。
     * 
     * @return  {@code UUID}
     */
    public static UUID randomUUID()
    {
        return randomUUID(true);
    }

    /**
     *  4（）UUID 。  UUID。
     * 
     * @param isSecure {@link SecureRandom}，
     * @return  {@code UUID}
     */
    public static UUID randomUUID(boolean isSecure)
    {
        final Random ng = isSecure ? Holder.numberGenerator : getRandom();

        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f; /* clear version */
        randomBytes[6] |= 0x40; /* set to version 4 */
        randomBytes[8] &= 0x3f; /* clear variant */
        randomBytes[8] |= 0x80; /* set to IETF variant */
        return new UUID(randomBytes);
    }

    /**
     *  3（）UUID 。
     *
     * @param name  UUID 。
     *
     * @return  {@code UUID}
     */
    public static UUID nameUUIDFromBytes(byte[] name)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest(name);
        md5Bytes[6] &= 0x0f; /* clear version */
        md5Bytes[6] |= 0x30; /* set to version 3 */
        md5Bytes[8] &= 0x3f; /* clear variant */
        md5Bytes[8] |= 0x80; /* set to IETF variant */
        return new UUID(md5Bytes);
    }

    /**
     *  {@link #toString()} {@code UUID}。
     *
     * @param name  {@code UUID} 
     * @return  {@code UUID}
     * @throws IllegalArgumentException  name  {@link #toString} 
     *
     */
    public static UUID fromString(String name)
    {
        String[] components = name.split("-");
        if (components.length != 5)
        {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < 5; i++)
        {
            components[i] = "0x" + components[i];
        }

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     *  UUID  128  64 。
     *
     * @return  UUID  128  64 。
     */
    public long getLeastSignificantBits()
    {
        return leastSigBits;
    }

    /**
     *  UUID  128  64 。
     *
     * @return  UUID  128  64 。
     */
    public long getMostSignificantBits()
    {
        return mostSigBits;
    }

    /**
     *  {@code UUID} .  {@code UUID} 。
     * <p>
     * :
     * <ul>
     * <li>1  UUID
     * <li>2 DCE  UUID
     * <li>3  UUID
     * <li>4  UUID
     * </ul>
     *
     * @return  {@code UUID} 
     */
    public int version()
    {
        // Version is bits masked by 0x000000000000F000 in MS long
        return (int) ((mostSigBits >> 12) & 0x0f);
    }

    /**
     *  {@code UUID} 。 {@code UUID} 。
     * <p>
     * ：
     * <ul>
     * <li>0  NCS 
     * <li>2 <a href="http://www.ietf.org/rfc/rfc4122.txt">IETF&nbsp;RFC&nbsp;4122</a>(Leach-Salz), 
     * <li>6 ，
     * <li>7 
     * </ul>
     *
     * @return  {@code UUID} 
     */
    public int variant()
    {
        // This field is composed of a varying number of bits.
        // 0 - - Reserved for NCS backward compatibility
        // 1 0 - The IETF aka Leach-Salz variant (used by this class)
        // 1 1 0 Reserved, Microsoft backward compatibility
        // 1 1 1 Reserved for future definition.
        return (int) ((leastSigBits >>> (64 - (leastSigBits >>> 62))) & (leastSigBits >> 63));
    }

    /**
     *  UUID 。
     *
     * <p>
     * 60  {@code UUID}  time_low、time_mid  time_hi 。<br>
     *  100 ， UTC（） 1582  10  15 。
     *
     * <p>
     *  UUID（ version  1）。<br>
     *  {@code UUID}  UUID， UnsupportedOperationException。
     *
     * @throws UnsupportedOperationException  {@code UUID}  version  1  UUID。
     */
    public long timestamp() throws UnsupportedOperationException
    {
        checkTimeBase();
        return (mostSigBits & 0x0FFFL) << 48//
                | ((mostSigBits >> 16) & 0x0FFFFL) << 32//
                | mostSigBits >>> 32;
    }

    /**
     *  UUID 。
     *
     * <p>
     * 14  UUID  clock_seq 。clock_seq  UUID 。
     * <p>
     * {@code clockSequence}  UUID（ version  1）。  UUID  UUID，
     * UnsupportedOperationException。
     *
     * @return  {@code UUID} 
     *
     * @throws UnsupportedOperationException  UUID  version  1
     */
    public int clockSequence() throws UnsupportedOperationException
    {
        checkTimeBase();
        return (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
    }

    /**
     *  UUID 。
     *
     * <p>
     * 48  UUID  node 。 IEEE 802 ， UUID 。
     * <p>
     *  UUID（ version  1）。<br>
     *  UUID  UUID， UnsupportedOperationException。
     *
     * @return  {@code UUID} 
     *
     * @throws UnsupportedOperationException  UUID  version  1
     */
    public long node() throws UnsupportedOperationException
    {
        checkTimeBase();
        return leastSigBits & 0x0000FFFFFFFFFFFFL;
    }

    /**
     * {@code UUID} 。
     *
     * <p>
     * UUID  BNF ：
     * 
     * <pre>
     * {@code
     * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               = [0-9a-fA-F]
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @return {@code UUID} 
     * @see #toString(boolean)
     */
    @Override
    public String toString()
    {
        return toString(false);
    }

    /**
     * {@code UUID} 。
     *
     * <p>
     * UUID  BNF ：
     * 
     * <pre>
     * {@code
     * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               = [0-9a-fA-F]
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param isSimple ，'-'UUID
     * @return {@code UUID} 
     */
    public String toString(boolean isSimple)
    {
        final StringBuilder builder = new StringBuilder(isSimple ? 32 : 36);
        // time_low
        builder.append(digits(mostSigBits >> 32, 8));
        if (!isSimple)
        {
            builder.append('-');
        }
        // time_mid
        builder.append(digits(mostSigBits >> 16, 4));
        if (!isSimple)
        {
            builder.append('-');
        }
        // time_high_and_version
        builder.append(digits(mostSigBits, 4));
        if (!isSimple)
        {
            builder.append('-');
        }
        // variant_and_sequence
        builder.append(digits(leastSigBits >> 48, 4));
        if (!isSimple)
        {
            builder.append('-');
        }
        // node
        builder.append(digits(leastSigBits, 12));

        return builder.toString();
    }

    /**
     *  UUID 。
     *
     * @return UUID 。
     */
    @Override
    public int hashCode()
    {
        long hilo = mostSigBits ^ leastSigBits;
        return ((int) (hilo >> 32)) ^ (int) hilo;
    }

    /**
     * 。
     * <p>
     *  {@code null}、 UUID 、 UUID  varriant、（）， {@code true}。
     *
     * @param obj 
     *
     * @return ， {@code true}； {@code false}
     */
    @Override
    public boolean equals(Object obj)
    {
        if ((null == obj) || (obj.getClass() != UUID.class))
        {
            return false;
        }
        UUID id = (UUID) obj;
        return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
    }

    // Comparison Operations

    /**
     *  UUID  UUID 。
     *
     * <p>
     *  UUID ， UUID  UUID ， UUID  UUID。
     *
     * @param val  UUID  UUID
     *
     * @return  UUID 、 val ， -1、0  1。
     *
     */
    @Override
    public int compareTo(UUID val)
    {
        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        return (this.mostSigBits < val.mostSigBits ? -1 : //
                (this.mostSigBits > val.mostSigBits ? 1 : //
                        (this.leastSigBits < val.leastSigBits ? -1 : //
                                (this.leastSigBits > val.leastSigBits ? 1 : //
                                        0))));
    }

    // -------------------------------------------------------------------------------------------------------------------
    // Private method start
    /**
     * hex
     * 
     * @param val 
     * @param digits 
     * @return 
     */
    private static String digits(long val, int digits)
    {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    /**
     * time-basedUUID
     */
    private void checkTimeBase()
    {
        if (version() != 1)
        {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
    }

    /**
     * {@link SecureRandom}， (RNG)
     * 
     * @return {@link SecureRandom}
     */
    public static SecureRandom getSecureRandom()
    {
        try
        {
            return SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new UtilException(e);
        }
    }

    /**
     * <br>
     * ThreadLocalRandomJDK 7，。
     * 
     * @return {@link ThreadLocalRandom}
     */
    public static ThreadLocalRandom getRandom()
    {
        return ThreadLocalRandom.current();
    }
}
