// SnmpEncodingException.java ----
// History: 2004/03/21 - Create
package jp.co.acroquest.tool.snmp.toolkit;

/**
 * SNMP Encoding処理でエラーが発生したことを表す例外。
 *
 * @author akiba
 * @version 1.0
 */
public class SnmpEncodingException extends RuntimeException
{
    private static final long serialVersionUID = -9220149928704551626L;

    /**
     * SnmpEncodingExceptionを生成する。
     */
    public SnmpEncodingException()
    {
        super();
    }

    /**
     * SnmpEncodingExceptionを生成する。
     *
     * @param cause この例外が発生した理由。
     */
    public SnmpEncodingException(String cause)
    {
        super(cause);
    }

    /**
     * SnmpEncodingExceptionを生成する。
     *
     * @param superException この例外に連鎖させる例外オブジェクト。
     */
    public SnmpEncodingException(Throwable superException)
    {
        super(superException);
    }

    /**
     * SnmpEncodingExceptionを生成する。
     *
     * @param cause この例外が発生した理由。
     * @param superException この例外に連鎖させる例外オブジェクト。
     */
    public SnmpEncodingException(String cause, Throwable superException)
    {
        super(cause, superException);
    }
}
