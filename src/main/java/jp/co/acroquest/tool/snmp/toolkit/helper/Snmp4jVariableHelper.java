// Snmp4jVariableHelper.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.helper;

import java.net.InetAddress;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;

/**
 * SNMP4Jを利用するSNMP状態値変換ヘルパークラス。
 * 
 * @author akiba
 */
public class Snmp4jVariableHelper extends AbstractSnmpVariableHelper
{
    /**
     * {@inheritDoc}
     */
    public Object createOctetString(String str)
    {
        OctetString retObj;
        if (str == null)
        {
            retObj = new OctetString("");
        }
        else
        {
            retObj = new OctetString(str);
        }
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public Object createOctetString(char[] chars)
    {
        OctetString retObj;
        if (chars == null)
        {
            retObj = new OctetString("");
        }
        else
        {
            retObj = new OctetString(new String(chars));
        }
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public Object createOctetString(byte[] bytes)
    {
        OctetString retObj;
        if (bytes == null)
        {
            retObj = new OctetString("");
        }
        else
        {
            retObj = new OctetString(bytes);
        }
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public Object createString(String str)
    {
        OctetString retObj;
        if (str == null)
        {
            retObj = new OctetString("");
        }
        else
        {
            retObj = new OctetString(str);
        }
        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public Object createInteger(int intValue)
    {
        return new Integer32(intValue);
    }

    /**
     * {@inheritDoc}
     */
    public Object createTimeticks(long ticks)
    {
        long ulongValue = (ticks / 10) & Integer.MAX_VALUE;
        TimeTicks retObj = new TimeTicks(ulongValue);

        return retObj;
    }

    /**
     * {@inheritDoc}
     */
    public Object createIpAddress(InetAddress address)
    {
        return new IpAddress(address);
    }

    /**
     * {@inheritDoc}
     */
    public Object createObjectId(String oid)
    {
        return new OID(oid);
    }

    /**
     * {@inheritDoc}
     */
    public String convertToString(String type, Object reqObj)
    {
        // typeがnullならば例外をスローする
        if (type == null)
        {
            throw new NullPointerException("type is null.");
        }

        // reqObjがVariableでなければ例外をスローする
        if ((reqObj instanceof Variable) == false)
        {
            throw new IllegalArgumentException("illegal object-type: "
                    + reqObj.getClass().getName());
        }

        Variable reqVar = (Variable) reqObj;
        String retStr = null;
        if (type.equals(INTEGER))
        {
            retStr = reqVar.toString();
        }
        else if (type.equals(OCTET_STRING))
        {
            retStr = reqVar.toString();
        }
        else if (type.equals(HEX_STRING))
        {
            byte[] bytes = reqVar.toString().getBytes();
            StringBuilder sb = new StringBuilder(bytes.length * 3);
            for (int index = 0; index < bytes.length; index++)
            {
                if (index > 0)
                {
                    sb.append(':');
                }
                sb.append(Integer.toHexString(bytes[index]));
            }
            retStr = sb.toString();
        }
        else if (type.equals(TIMETICKS))
        {
            retStr = reqVar.toString();
        }
        else if (type.equals(IPADDRESS))
        {
            retStr = reqVar.toString();
        }
        else if (type.equals(OBJECT_ID))
        {
            retStr = reqVar.toString();
        }
        else if (type.equals(STRING))
        {
            retStr = reqVar.toString();
        }
        else
        {
            throw new IllegalArgumentException("unknown type: " + type);
        }

        return retStr;
    }
}
