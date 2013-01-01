//Snmp4jTrapSender.java ----
// History: 2004/11/22 - Create
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.trap;

import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitImpl;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.helper.Snmp4jVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.helper.SnmpVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.loader.SnmpConfiguration;
import jp.co.acroquest.tool.snmp.toolkit.stack.Snmp4jFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * SNMP4jライブラリを使用したTrapSender。
 *
 * @author akiba
 * @version 1.0
 */
public class Snmp4jTrapSender extends AbstractTrapSender
{
    private static final Object SNMP_VERSION_V1  = "v1";
    private static final Object SNMP_VERSION_V2C = "v2c";

    /** PDU送信に使用するSnmpオブジェクト。 */
    private Snmp snmp_;

    /**
     * Snmp4jTrapSenderを初期化する。
     *
     * @param host バインドするホスト名。
     * @exception SnmpToolkitException SNMPコンテキストの作成に失敗した場合。
     */
    public Snmp4jTrapSender(String host)
        throws SnmpToolkitException
    {
        super();
        Log log = LogFactory.getLog(TrapSender.class);

        // Snmpコンテキストの作成
        try
        {
            log.debug("Initializing Snmp4jTrapSender. host=" + host);
            this.snmp_ = Snmp4jFactory.createSnmp(host);
        }
        catch (IOException exception)
        {
            // TransportMappingの作成に失敗した場合は内部の例外にラップしてスローする
            throw new SnmpToolkitException("Failed to create transport mapping.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sendTrap(TrapData trapData) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(TrapSender.class);
        PDU pdu = null;

        int version = 0;
        String versionStr = trapData.getVersion();
        if (SNMP_VERSION_V1.equals(versionStr) == true)
        {
            if (log.isDebugEnabled() == true)
            {
                log.debug("Trap SNMP version is v1.");
            }
            version = SnmpConstants.version1;
            pdu = createTrapV1PDU(trapData);
        }
        else if (SNMP_VERSION_V2C.equals(versionStr) == true)
        {
            if (log.isDebugEnabled() == true)
            {
                log.debug("Trap SNMP version is v2c.");
            }
            version = SnmpConstants.version2c;
            pdu = createTrapV2cPDU(trapData);
        }

        // Trapを送信する
        try
        {
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            SnmpManagerList mgrList = configItem.getSnmpManagerList();
            SnmpManager[] managers = mgrList.getSnmpManagers();

            // 定義されている全てのマネージャに送信する
            for (SnmpManager manager : managers)
            {
                String mgrAddress = manager.getManagerAddress();
                if (mgrAddress == null)
                {
                    log.warn("Manager address is null.");
                    throw new SnmpToolkitException("Manager address is null.");
                }

                if (log.isDebugEnabled() == true)
                {
                    log.debug("snmp manager=" + mgrAddress);
                }

                CommunityTarget target = new CommunityTarget();
                target.setVersion(version);
                target.setCommunity(new OctetString(super.community_));
                Address address = GenericAddress.parse(mgrAddress);
                if (address == null)
                {
                    log.error("Failed to parse manager address.");
                    throw new SnmpToolkitException("Failed to parse manager address.");
                }
                target.setAddress(address);

                if (log.isDebugEnabled() == true)
                {
                    log.debug("Sending trap-pdu=" + pdu + ", target=" + target);
                }

                this.snmp_.send(pdu, target);
                log.info("Trap is sent to " + target.getAddress());
            }
        }
        catch (IOException ioEx)
        {
            throw new SnmpToolkitException("Failed to send pdu.", ioEx);
        }
        catch (RuntimeException ex)
        {
            log.error("Failed to send pdu caused by unknown error.", ex);
            throw ex;
        }
    }

    /**
     * SNMPv1TrapのPDUを取得する。
     *
     * @param trapData PDUに設定するTrapデータ。
     * @return SNMPv2TrapのPDU。
     * @throws SnmpToolkitException 不正なTrapデータだった場合。
     */
    private PDU createTrapV1PDU(TrapData trapData)
        throws SnmpToolkitException
    {
        SnmpVariableHelper varHelaper = new Snmp4jVariableHelper();

        PDUv1 pdu = new PDUv1();
        pdu.setType(PDU.V1TRAP);

        // RequestIDが指定されていれば、TrapPDUに設定する
        boolean hasReqId = trapData.hasReqId();
        if (hasReqId == true)
        {
            pdu.setRequestID(new Integer32(trapData.getReqId()));
        }

        int generic = trapData.getGeneric();
        int specific = trapData.getSpecific();
        String enterprise = trapData.getEnterprise();

        if (generic < 0)
        {
            throw new SnmpToolkitException("invalid generic: " + generic);
        }
        pdu.setGenericTrap(generic);

        if (specific < 0)
        {
            throw new SnmpToolkitException("invalid specific: " + specific);
        }
        pdu.setSpecificTrap(specific);

        if (enterprise == null)
        {
            throw new SnmpToolkitException("invalid enterprise: null");
        }
        pdu.setEnterprise(new OID(enterprise));

        // sysUpTimeは1/100単位、かつ32bit Integerを超えないようにする
        long sysUpTime = SnmpToolkitImpl.getSysUpTime();
        int  sysUpTimeInt = (int)((sysUpTime / 10) % 4294967296L);
        pdu.setTimestamp(sysUpTimeInt);

        // Trapに必要なVariableBindingの設定
        SnmpVarbind[] varbinds = trapData.getVarbinds();
        for (int index = 0; index < varbinds.length; index ++)
        {
            String oid   = varbinds[index].getOid();
            Object value = varbinds[index].getValue();
            String type  = varbinds[index].getType();
            Variable asnObject = (Variable) varHelaper.createAsnObject(value, type);
            if (asnObject != null)
            {
                pdu.add(new VariableBinding(new OID(oid), asnObject));
            }
        }

        return pdu;
    }

    /**
     * SNMPv2TrapのPDUを取得する。
     *
     * @param trapData PDUに設定するTrapデータ。
     * @return SNMPv2TrapのPDU。
     */
    private PDU createTrapV2cPDU(TrapData trapData)
    {
        SnmpVariableHelper varHelaper = new Snmp4jVariableHelper();

        PDU pdu = new PDU();
        pdu.setType(PDU.TRAP);

        // RequestIDが指定されていれば、TrapPDUに設定する
        // 指定されていなければ、自動的にインクリメントされた値を設定する
        boolean hasReqId = trapData.hasReqId();
        if (hasReqId == true)
        {
            pdu.setRequestID(new Integer32(trapData.getReqId()));
        }
        else
        {
            int newReqId = this.snmp_.getNextRequestID();
            pdu.setRequestID(new Integer32(newReqId));
        }

        // SysUpTime、TrapOIDの設定
        // sysUpTimeは1/100単位、かつ32bit Integerを超えないようにする
        long sysUpTime = SnmpToolkitImpl.getSysUpTime();
        int  sysUpTimeInt = (int)((sysUpTime / 10) % 4294967296L);
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                                    new TimeTicks(sysUpTimeInt)));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID,
                new OID(trapData.getTrapOid())));

        // Trapに必要なVariableBindingの設定
        SnmpVarbind[] varbinds = trapData.getVarbinds();
        for (int index = 0; index < varbinds.length; index ++)
        {
            String oid   = varbinds[index].getOid();
            Object value = varbinds[index].getValue();
            String type  = varbinds[index].getType();
            Variable asnObject = (Variable) varHelaper.createAsnObject(value, type);
            if (asnObject != null)
            {
                pdu.add(new VariableBinding(new OID(oid), asnObject));
            }
        }

        // trapEnterpriseの設定
        // XMLファイル中に<enterprise>要素を含んでいる場合は、それを設定する。
        // 指定がない場合は、設定しない。その場合は、自分でVarbindを設定する必要がある。
        String enterprise = trapData.getEnterprise();
        if (enterprise != null)
        {
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise,
                    new OID(enterprise)));
        }

        return pdu;
    }
}
