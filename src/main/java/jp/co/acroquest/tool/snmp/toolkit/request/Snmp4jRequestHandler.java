// Snmp4jTrapSender.java ----
// History: 2009/05/07 - Create
// 2009/05/21 - GETNEXT対応
// 2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.request;

import java.io.IOException;
import java.net.InetAddress;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.RequestProcessor;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.Snmp4jGetRequestProcessor;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.Snmp4jSetRequestProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * SNMP4J用のRequest処理クラス。
 *
 * @author akiba
 */
public class Snmp4jRequestHandler implements RequestHandler, CommandResponder
{
    /** Get typeStr */
    private static final String PDU_TYPESTR_GET      = "GET";
    /** GetBulk typeStr */
    private static final String PDU_TYPESTR_GETBULK  = "GETBULK";
    /** GetNext typeStr */
    private static final String PDU_TYPESTR_GETNEXT  = "GETNEXT";
    /** Inform typeStr */
    private static final String PDU_TYPESTR_INFORM   = "INFORM";
    /** Report typeStr */
    private static final String PDU_TYPESTR_REPORT   = "REPORT";
    /** Response typeStr */
    private static final String PDU_TYPESTR_RESPONSE = "RESPONSE";
    /** Set typeStr */
    private static final String PDU_TYPESTR_SET      = "SET";
    /** Trap(Notification) typeStr */
    private static final String PDU_TYPESTR_TRAP     = "TRAP";
    /** V1Trap typeStr */
    private static final String PDU_TYPESTR_V1TRAP   = "V1TRAP";
    /** Unknown typeStr */
    private static final String PDU_TYPESTR_UNKNOWN  = "unknown";

    /** SNMPスタック。 */
    private Snmp                snmp_;

    /** 読み込みコミュニティ。 */
    private String              roCommunity_;

    /** 書き込みコミュニティ。 */
    private String              rwCommunity_;

    /** RequestHandlerが扱うSNMP-Agent情報のサービス。 */
    private AgentService        agentService_;

    /** GET/GETNEXT の要求を処理するプロセッサ。 */
    private RequestProcessor    getReqProcessor_;

    /** SET の要求を処理するプロセッサ。 */
    private RequestProcessor    setReqProcessor_;

    /**
     * デフォルトコンストラクタ。
     */
    public Snmp4jRequestHandler()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void initHandler(AgentService agentService) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(Snmp4jRequestHandler.class);
        try
        {
            // AgentServiceを保存する
            this.agentService_ = agentService;

            Agent agent = agentService.getAgent();

            // 指定されたAgentの情報を取得する
            String address = agent.getAddress();
            int port = agent.getSnmpPort();
            String roCommunity = agent.getRoCommunity();
            String rwCommunity = agent.getRwCommunity();
            log.info("agent: " + address + ":" + port + "@" + roCommunity + "/" + rwCommunity);

            // SNMPスタックを初期化する
            UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(address), port);
            TransportMapping transportMapping = new DefaultUdpTransportMapping(udpAddress);
            this.snmp_ = new Snmp(transportMapping);
            this.snmp_.addCommandResponder(this);

            // 読み込みコミュニティのデフォルトチェック
            if (roCommunity == null)
            {
                this.roCommunity_ = DEFAULT_RO_COMMUNITY;
            }
            else
            {
                this.roCommunity_ = roCommunity;
            }

            // 書き込みコミュニティのデフォルトチェック
            if (rwCommunity == null)
            {
                this.rwCommunity_ = DEFAULT_RW_COMMUNITY;
            }
            else
            {
                this.rwCommunity_ = rwCommunity;
            }

            // RequestProcessorを初期化する
            this.getReqProcessor_ = new Snmp4jGetRequestProcessor(agentService);
            this.setReqProcessor_ = new Snmp4jSetRequestProcessor(agentService);
        }
        catch (IOException exception)
        {
            log.error("IOException occured.", exception);
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void startListening() throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());
        Agent agent = this.agentService_.getAgent();
        log.info("startListening: " + "address=[" + agent.getAddress() + "]");

        try
        {
            this.snmp_.listen();
            log.info("start : listening started." + " address=[" + agent.getAddress() + "]");
            System.out.println("start : listening at [" + agent.getAddress() + "]");
        }
        catch (IOException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stopListening() throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());
        Agent agent = this.agentService_.getAgent();
        if (agent == null)
        {
            log.warn("agent is null. stopListening is nothing to do.");
            return;
        }

        log.info("stopListening address=[" + agent.getAddress() + "]");

        try
        {
            this.snmp_.close();
            log.info("stop  : listening stopped." + " address=[" + agent.getAddress() + "]");
            System.out.println("stop  : agent [" + agent.getAddress() + "]");
        }
        catch (IOException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * 受信したRequestで指定されたOIDに対するResponseを送信する。
     *
     * @param event Requestを受信したことを示すSNMP4Jのイベント。
     */
    public void processPdu(CommandResponderEvent event)
    {
        Log log = LogFactory.getLog(this.getClass());

        // Eventから受信したPDUを取得する
        PDU pdu = event.getPDU();
        log.info("processPdu : pdu=" + pdu);

        // コミュニティ名を取得する
        String securityName = new String(event.getSecurityName());

        // 応答用PDU
        PDU retPdu = null;

        try
        {
            // GET, GETNEXT, SETのみに対応する
            if (pdu.getType() == PDU.GET || pdu.getType() == PDU.GETNEXT)
            {
                // SNMPコミュニティ名をチェックする
                if (this.roCommunity_.equals(securityName) == false)
                {
                    log.warn("invalid request has received. community: " + securityName + " <> "
                            + this.roCommunity_);
                    return;
                }

                // 受信したPDUを処理する
                log.debug("PDU type [" + toTypeString(pdu.getType()) + "] is applicable.");
                event.setProcessed(true);
                retPdu = this.getReqProcessor_.processPdu(pdu);
            }
            else if (pdu.getType() == PDU.SET)
            {
                // SNMPコミュニティ名をチェックする
                if (rwCommunity_.equals(securityName) == false)
                {
                    log.warn("invalid request has received. community: " + securityName + " <> "
                            + this.rwCommunity_);
                    return;
                }

                // 受信したPDUを処理する
                log.debug("PDU type [" + toTypeString(pdu.getType()) + "] is applicable.");
                event.setProcessed(true);
                retPdu = this.setReqProcessor_.processPdu(pdu);
            }
            else
            {
                log.warn("PDU type [" + toTypeString(pdu.getType()) + "] is not applicable.");
                return;
            }
        }
        catch (SnmpToolkitException exception)
        {
            log.warn("exception occured in processPdu.", exception);
        }

        // 送信するResponsePDUが無ければ、終了する
        if (retPdu == null || retPdu.size() == 0)
        {
            log.info("there is nothing to respond.");
            return;
        }

        // ResponsePDUに必要な情報を設定し、送信する
        CommunityTarget target = new CommunityTarget();
        target.setAddress(event.getPeerAddress());
        int snmpVersion = getSnmpVersion(event.getSecurityLevel(), event.getSecurityModel());
        target.setVersion(snmpVersion);
        target.setCommunity(new OctetString(securityName));
        try
        {
            this.snmp_.send(retPdu, target);
            log.info("sent response: " + retPdu + ", target=" + target);
        }
        catch (IOException exception)
        {
            log.warn("Failed to send response.", exception);
        }
    }

    /**
     * PDUのtype値に対応する名称文字列を取得する。
     *
     * @param type PDUのtype値。
     * @return PDUのtypeを表す名称文字列。
     */
    private String toTypeString(int type)
    {
        String typeStr;
        switch (type)
        {
        case PDU.GET:
            typeStr = PDU_TYPESTR_GET;
            break;
        case PDU.GETBULK:
            typeStr = PDU_TYPESTR_GETBULK;
            break;
        case PDU.GETNEXT:
            typeStr = PDU_TYPESTR_GETNEXT;
            break;
        case PDU.INFORM:
            typeStr = PDU_TYPESTR_INFORM;
            break;
        case PDU.REPORT:
            typeStr = PDU_TYPESTR_REPORT;
            break;
        case PDU.RESPONSE:
            typeStr = PDU_TYPESTR_RESPONSE;
            break;
        case PDU.SET:
            typeStr = PDU_TYPESTR_SET;
            break;
        case PDU.TRAP: // includes NOTIFICATION
            typeStr = PDU_TYPESTR_TRAP;
            break;
        case PDU.V1TRAP:
            typeStr = PDU_TYPESTR_V1TRAP;
            break;
        default:
            typeStr = PDU_TYPESTR_UNKNOWN;
            break;
        }

        return typeStr;
    }

    /**
     * 指定されたセキュリティパラメータからSNMPバージョンをあらわす数値に変換する。<br/>
     *
     *
     * @param securityLevel セキュリティレベル番号。
     * @param securityModel セキュリティモデル番号。
     * @return SNMPバージョンをあらわす数値。
     */
    private int getSnmpVersion(int securityLevel, int securityModel)
    {
        int snmpVersion = SnmpConstants.version1;
        switch (securityLevel)
        {
        case 0:
            snmpVersion = SnmpConstants.version1;
            break;
        case 1:
        	if (securityModel == 1)
        	{
                snmpVersion = SnmpConstants.version1;
        	}
        	else if (securityModel == 2)
        	{
                snmpVersion = SnmpConstants.version2c;
        	}
            break;
        case 3:
            snmpVersion = SnmpConstants.version3;
            break;
        default:
            break;
        }

        return snmpVersion;
    }

    /**
     * {@inheritDoc}
     */
    public void setAgent(Agent agent)
    {
        //this.agent_ = agent;
    }

    /**
     * {@inheritDoc}
     */
    public Agent getAgent()
    {
        return this.agentService_.getAgent();
    }
}
