//RequestProcessor.java ----
// History: 2010/02/03 - Create
package jp.co.acroquest.tool.snmp.toolkit.request.processor;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

import org.snmp4j.PDU;

/**
 * SNMPの要求(PDU)を処理するプロセッサクラスの基底インタフェース。
 * 
 * @author akiba
 */
public interface RequestProcessor
{
    /**
     * 受信したPDUを処理する。
     * 
     * @param pdu 受信したPDU。
     * @return 処理した結果生成され、返信するPDU。
     * @throws SnmpToolkitException PDUの処理中にエラーが発生した場合。
     */
    PDU processPdu(PDU pdu)
        throws SnmpToolkitException;
}
