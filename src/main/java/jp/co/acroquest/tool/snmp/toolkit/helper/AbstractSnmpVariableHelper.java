//AbstractSnmpVariableHelper.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.acroquest.tool.snmp.toolkit.SnmpEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SNMPの値を型指定文字列にしたがって変換するヘルパークラスの抽象クラス。<br/>
 * 各スタックに共通の処理を実装している。
 *
 * @author akiba
 */
public abstract class AbstractSnmpVariableHelper implements SnmpVariableHelper
{
    /** 日付解析用フォーマッタ。 */
    protected static final SimpleDateFormat PARSER =
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    /**
     * ヘルパークラスを初期化する。
     */
    protected AbstractSnmpVariableHelper()
    {
    }

    /**
     * Objectの型から、AsnObjectを生成する。
     *
     * @param value AsnObjectに変換したいObject。
     * @param type  Objectの型を表す文字列。
     * @return AsnObject。
     */
    public Object createAsnObject(Object value, String type)
        throws SnmpEncodingException
    {
        Log log = LogFactory.getLog(SnmpVariableHelper.class);
        Object obj = null;

        if (type.equals(OCTET_STRING))
        {
            // OctetString形式の場合は、そのままStringとして扱う
            String strObj = "";
            if (value != null)
            {
                strObj = (String) value;
            }
            log.debug("OCTET-STRING=" + strObj);
            obj = createOctetString(strObj);
        }
        else
        if (type.equals(STRING))
        {
            String strObj = "";
            if (value != null)
            {
                strObj = (String) value;
            }
            log.debug("STRING=" + strObj);
            obj = createString(strObj);
        }
        else
        if (type.equals(OBJECT_ID))
        {
            // ObjectID形式の場合は、そのままOIDに変換する
            String oidStr = (String) value;
            log.debug("OBJECT-ID=" + oidStr);
            obj = createObjectId(oidStr);
        }
        else
        if (type.equals(HEX_STRING))
        {
            // Hex-String形式の場合は、':' で区切られた16進文字列をbyte[]に変換する
            byte[]       bytes;
            if (value != null)
            {
                String   strObj   = (String) value;
                String[] strArray = strObj.split(":");

                bytes = new byte[strArray.length];
                StringBuffer buf   = new StringBuffer(strArray.length * 2);
                try
                {
                    for (int index = 0; index < strArray.length; index ++)
                    {
                        // byteに変換する前に、一度intを通すことで0x80以上の値を扱う
                        int intValue = Integer.parseInt(strArray[index], 16);
                        bytes[index] = (byte) (intValue & 0x000000FF);

                        if (log.isDebugEnabled())
                        {
                            buf.append(toHexString(bytes[index]) + ":");
                        }
                    }
                }
                catch (NumberFormatException exception)
                {
                    // Hex数値として認識できない場合は、例外をスローする
                    throw new SnmpEncodingException("cannot recognize value as numeric.", exception);
                }
                log.debug("HEX-STRING=" + buf.toString());
            }
            else
            {
                bytes = new byte[0];
                log.debug("HEX-STRING=");
            }
            obj = createOctetString(bytes);
        }
        else
        if (type.equals(TIMETICKS))
        {
            // Timeticks形式の場合は、
            Date dateObj = null;
            if (value instanceof Date)
            {
                dateObj = (Date) value;
            }
            else
            if (value instanceof String)
            {
                String strObj = (String) value;
                if ("NOW".equalsIgnoreCase(strObj))
                {
                    dateObj = new Date();
                }
                else
                {
                    try
                    {
                        dateObj = PARSER.parse(strObj);
                    }
                    catch (ParseException exception)
                    {
                        throw new SnmpEncodingException("failed to parse date-time.", exception);
                    }
                }
            }
            else
            {
                // Integer、String以外は例外をスローする
                String actualType = value.getClass().getName();
                throw new SnmpEncodingException("invalid object type: excepcted=Date, actual=" +
                                                actualType);
            }

            long ticks = dateObj.getTime();
            log.debug("TIMETICKS=" + ticks);
            obj = createTimeticks(ticks);
        }
        else
        if (type.equals(INTEGER))
        {
            // Integer形式は、(java.lang)IntegerとStringのみ有効
            Integer intObj = null;
            if (value instanceof Integer)
            {
                // (java.lang)Integerの場合
                intObj = (Integer) value;
            }
            else
            if (value instanceof String)
            {
                // Stringの場合
                intObj = Integer.valueOf((String) value);
            }
            else
            {
                // Integer、String以外は例外をスローする
                String actualType = value.getClass().getName();
                throw new SnmpEncodingException("invalid object type: expected=Integer, actual=" +
                                                actualType);
            }
            log.debug("INTEGER=" + intObj);
            obj = createInteger(intObj.intValue());
        }
        else
        if (type.equals(IPADDRESS))
        {
            // IpAddress形式は、InetAddressオブジェクトとして扱う
            InetAddress addrObj;
            try
            {
                addrObj = InetAddress.getByName((String) value);
                log.debug("IPADDRESS=" + addrObj.getHostAddress());
                obj = createIpAddress(addrObj);
            }
            catch (UnknownHostException exception)
            {
                // IpAddressオブジェクトに変換できなかった場合は
                // 独自の例外に置き換えてスローする
                throw new SnmpEncodingException("Invalid address.", exception);
            }
        }
        else
        {
            throw new SnmpEncodingException("Unknown type: " + type);
        }

        return obj;
    }

    /**
     * 1文字分のbyte値をHEX文字列に変換する。(やや重)
     *
     * @param ch HEX文字列に変換するbyte値。
     * @return HEX文字列。
     */
    private String toHexString(byte ch)
    {
        int tmpCh = ch & 0x000000ff;
        return Integer.toHexString(tmpCh);
    }
}
