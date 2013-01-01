//Snmp4jGetRequestProcessor.java ----
// History: 2010/02/03 - Create
package jp.co.acroquest.tool.snmp.toolkit.request.processor;

import java.util.List;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.helper.Snmp4jVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.helper.SnmpVariableHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * GET/GETNEXT を処理するRequestProcessor。
 *
 * @author akiba
 */
public class Snmp4jGetRequestProcessor implements RequestProcessor
{
    /** RequestHandlerが扱うSNMP-Agent情報のサービス。 */
    private AgentService agentService_;

    /**
     * Snmp4jGetRequestProcessorを初期化する。
     *
     * @param agentService RequestHandlerが扱うSNMP-Agent情報のサービス。
     */
    public Snmp4jGetRequestProcessor(AgentService agentService)
    {
        this.agentService_ = agentService;
    }

    /**
     * {@inheritDoc}
     */
    public PDU processPdu(PDU pdu) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());

        // 応答用のPDUを生成する
        PDU retPdu = new PDU();
        retPdu.setType(PDU.RESPONSE);

        // 要求のリクエストIDを取得し、応答PDUにセットする
        Integer32 requestID = pdu.getRequestID();
        retPdu.setRequestID(requestID);

        // SNMP4J用の型変換オブジェクト
        SnmpVariableHelper varHelper = new Snmp4jVariableHelper();

        // AgentServiceからAgentを取得する
        Agent agent = this.agentService_.getAgent();

        // 要求PDUのVarbindを走査する
        int varCount = 0;
        List<?> reqVarbinds = pdu.getVariableBindings();
        for (Object reqVarbindObj : reqVarbinds)
        {
            varCount++;

            // 要求されているOIDを取得する
            VariableBinding reqVarbind = (VariableBinding) reqVarbindObj;
            OID oid = reqVarbind.getOid();

            // GETNEXTでない場合は、指定されたOIDそのものを取得しようとする
            // GETNEXTの場合は、指定されたOID配下で最も近いオブジェクトを探す
            boolean exact = (pdu.getType() != PDU.GETNEXT);
            SnmpVarbind foundVarbind = agent.findObject(oid.toString(), exact);
            if (foundVarbind == null)
            {
                // Varbindが見つからなかった場合はnoSuchNameを返す
                log.warn("varbind is not found. oid=" + oid.toString());

                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_SUCH_NAME);
                retPdu.setErrorIndex(varCount);

                Variable retObject = new Null();
                VariableBinding retVarbind = new VariableBinding(oid, retObject);
                retPdu.add(retVarbind);
                break;
            }

            // READ-WRITEでなければエラー応答を返す
            String accessibility = foundVarbind.getAccessibility();
            if (accessibility.equals(SnmpVarbind.ACCESSIBILITY_NOT_ACCESSIBLE) == true)
            {
                log.warn("varbind is not accessible. accessibility=" + accessibility);

                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_ACCESS);
                retPdu.setErrorIndex(varCount);

                Variable retObject = new Null();
                VariableBinding retVarbind = new VariableBinding(oid, retObject);
                retPdu.add(retVarbind);
                break;
            }

            try
            {
                // 正常の応答を返す
                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);
                retPdu.setErrorIndex(0);

                log.debug("varbind is found: " + foundVarbind.toString());
                String typeStr = foundVarbind.getType();
                Object retValueObj = foundVarbind.getValue();
                Variable retObject = (Variable) varHelper.createAsnObject(retValueObj, typeStr);

                OID retOID = new OID(foundVarbind.getOid());
                VariableBinding retVarbind = new VariableBinding(retOID, retObject);
                retPdu.add(retVarbind);
            }
            catch (Exception exception)
            {
                log.warn("exception occured", exception);

                // 未知のエラーを示す応答PDUを作成する
                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_GENERAL_ERROR);
                retPdu.setErrorIndex(varCount);

                if (foundVarbind != null)
                {
                    OID retOID = new OID(foundVarbind.getOid());
                    Variable retObject = new Null();
                    VariableBinding retVarbind = new VariableBinding(retOID, retObject);
                    retPdu.add(retVarbind);
                }
            }
        }
        return retPdu;
    }
}
