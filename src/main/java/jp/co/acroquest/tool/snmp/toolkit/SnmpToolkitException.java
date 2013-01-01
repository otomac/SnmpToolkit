//SnmpToolkitException.java ----
// History: 2004/11/22 - Create

package jp.co.acroquest.tool.snmp.toolkit;

/**
 * SnmpToolkitの内部で共通的に取り回す例外。
 * 
 * @author akiba
 * @version 1.00
 */
public class SnmpToolkitException extends Exception
{
    private static final long serialVersionUID = 930082892844855194L;

    /**
     * SnmpToolkitExceptionを生成する。
     */
    public SnmpToolkitException()
    {
        super();
    }

    /**
     * SnmpToolkitExceptionを生成する。
     * 
     * @param cause この例外が発生した理由。
     */
    public SnmpToolkitException(String cause)
    {
        super(cause);
    }

    /**
     * SnmpToolkitExceptionを生成する。
     * 
     * @param superException この例外に連鎖させる例外オブジェクト。
     */
    public SnmpToolkitException(Throwable superException)
    {
        super(superException);
    }

    /**
     * SnmpToolkitExceptionを生成する。
     * 
     * @param cause この例外が発生する理由。
     * @param superException この例外に連鎖させる例外オブジェクト。
     */
    public SnmpToolkitException(String cause, Throwable superException)
    {
        super(cause, superException);
    }
}
