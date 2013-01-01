//SimpleRequestSender.java ----
// History: 2005/02/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.request;

import java.net.InetAddress;

import jp.co.acroquest.tool.snmp.toolkit.stack.Snmp4jFactory;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;

/**
 * ダミーのRequest送信クラス。
 *
 * @author akiba
 * @version 1.0
 */
public class SimpleRequestSender
{
    public static void main(String[] args)
        throws Exception
    {
        SimpleRequestSender app = new SimpleRequestSender();
        app.send(args[0]);

        System.exit(0);
    }

    /**
     * 指定されたホストに対して固定のRequestを送信する。
     */
    private void send(String toHost)
        throws Exception
    {
        // PDUオブジェクトの生成
        PDU requestPdu = new PDUv1();
        requestPdu.setType(PDU.SET);

        VariableBinding varbind = new VariableBinding();
        varbind.setOid(new OID("1.3.6.1.2.1.1.1.0"));
        requestPdu.add(varbind);

        // 送信先ノードの決定
        CommunityTarget target = new CommunityTarget();
        OctetString community = new OctetString("public");
        target.setCommunity(community);
        target.setAddress(new UdpAddress(InetAddress.getByName(toHost), 161));
        target.setVersion(1);

        // 送信
        Snmp snmp = Snmp4jFactory.createSnmp("127.0.0.1", 161);
        snmp.send(requestPdu, target);
        System.out.println("INFO : Sent pdu.");
    }
}
