//SnmpVariableHelper.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.helper;

import java.net.InetAddress;

/**
 * SNMPの値を型指定文字列にしたがって変換するヘルパークラスの基底インタフェース。<br/>
 * 様々なSNMPスタックライブラリを利用できるよう、共通のインタフェースを規定している。
 * 
 * @author akiba
 */
public interface SnmpVariableHelper
{
    /** Integer */
    static final String INTEGER      = "integer";

    /** Octet-String */
    static final String OCTET_STRING = "octets";

    /** Hex-String */
    static final String HEX_STRING   = "hex";

    /** Timeticks */
    static final String TIMETICKS    = "timeticks";

    /** IP-Address */
    static final String IPADDRESS    = "ipaddress";
    
    /** Object-ID */
    static final String OBJECT_ID    = "object-id";
    
    /** String */
    static final String STRING       = "string";

    /**
     * 与えられたObjectとデータ型名からAsnObjectオブジェクトを作成する。
     * 
     * @param obj 変換対象オブジェクト。
     * @param typeStr オブジェクトのデータ型名。指定可能な型名は本インタフェースの定数として宣言しているものとする。
     * @return 変換後のAsnObjectオブジェクト。
     */
    Object createAsnObject(Object obj, String typeStr);
    
    /**
     * OctetStringを表現するStack上のオブジェクトを生成する。
     * 
     * @param str 変換元となるStringオブジェクト。
     * @return 変換したOctetStringオブジェクト。
     */
    Object createOctetString(String str);
    
    /**
     * OctetStringを表現するStack上のオブジェクトを生成する。
     * 
     * @param chars 変換元となるchar配列。
     * @return 変換したOctetStringオブジェクト。
     */
    Object createOctetString(char[] chars);
    
    /**
     * OctetStringを表現するStack上のオブジェクトを生成する。
     * 
     * @param bytes 変換元となるbyte配列。
     * @return 変換したOctetStringオブジェクト。
     */
    Object createOctetString(byte[] bytes);
    
    
    /**
     * Stringを表現するStack上のオブジェクトを生成する。
     * 
     * @param string 変換元となるString文字列。
     * @return 変換したOctetStringオブジェクト。
     */
    Object createString(String string);

    /**
     * IpAddressを表現するStack上のオブジェクトを生成する。
     * 
     * @param addr 変換元となるInetAddressオブジェクト。
     * @return 変換したIpAddressオブジェクト。
     */
    Object createIpAddress(InetAddress addr);

    /**
     * Integerを表現するStack上のオブジェクトを生成する。
     * 
     * @param intValue 変換元となるint値。
     * @return 変換したIntegerオブジェクト。
     */
    Object createInteger(int intValue);

    /**
     * Timeticksを表現するStack上のオブジェクトを生成する。
     * 
     * @param ticks 変換元となるtick値。
     * @return 変換したTimeticksオブジェクト。
     */
    Object createTimeticks(long ticks);
    
    /**
     * ObjectIDを表現するStack上のオブジェクトを生成する。
     * 
     * @param oid 変換元となるObjectID値。
     * @return 変換したObjectIDオブジェクト。
     */
    Object createObjectId(String oid);

    /**
     * Stack上のオブジェクトを文字列形式に変換する。
     * 
     * @param type Agentの型。
     * @param reqVar Stack上のオブジェクト。
     * @return 変換した文字列。
     */
    String convertToString(String type, Object reqVar);
}
