//Snmp4jFactory.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - SnmpStackFactoryを導入
//          2009/08/15 - AgentService対応
package jp.co.acroquest.tool.snmp.toolkit.stack;

import java.io.IOException;
import java.net.InetAddress;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.request.Snmp4jRequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.trap.Snmp4jTrapSender;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * Snmp4jのオブジェクト生成用ファクトリクラス。
 *
 * @author akiba
 * @version 1.0
 */
public class Snmp4jFactory extends SnmpStackFactory
{
    /**
     * {@inheritDoc}
     */
    public TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException
    {
        String address       = agent.getAddress();
        String host          = getHostIpAddress(address);
        String trapCommunity = agent.getTrapCommunity();

        TrapSender sender = new Snmp4jTrapSender(host);

        // 生成したTrapSenderに初期情報を設定する
        sender.setCommunity(trapCommunity);

        return sender;
    }

    /**
     * ホスト表記からIPアドレスを抽出する。
     *
     * @param address ホストの表記。"ホスト名:ポート番号"の形式を期待している。
     * @return ホストの表記から抽出したIPアドレス。ポート番号がなければ入力そのままを返す。
     */
    private String getHostIpAddress(String address)
    {
        String host;
        //TODO: TCP/UDPの切替を可能にする
        String hostAndPort = address.replace("udp://", "");
        int portIndex = hostAndPort.indexOf(':');
        if (portIndex < 0)
        {
            host = hostAndPort;
        }
        else
        {
            host = hostAndPort.substring(0, portIndex);
        }
        return host;
    }

    /**
     * {@inheritDoc}
     */
    public RequestHandler createRequestHandler(AgentService agentService)
        throws SnmpToolkitException
    {
        RequestHandler handler = new Snmp4jRequestHandler();
        handler.initHandler(agentService);

        return handler;
    }

    /**
     * Snmpオブジェクトを生成する。
     *
     * @param host バインド先ホスト名。
     * @param port バインド先ポート番号。
     * @return Snmpオブジェクト。
     * @throws IOException Snmpオブジェクトの初期化に失敗した場合。
     */
    public static Snmp createSnmp(String host, int port)
        throws IOException
    {
        //TODO: TCP/UDPの切替を可能にする
        UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(host), port);
        TransportMapping transportMapping = new DefaultUdpTransportMapping(udpAddress);
        Snmp snmp = new Snmp(transportMapping);

        return snmp;
    }

    /**
     * Trap送信用のSnmpオブジェクトを生成する。
     *
     * @param host バインド先ホスト名。
     * @return Trap送信用のSnmpオブジェクト。
     * @throws IOException Snmpオブジェクトの初期化に失敗した場合。
     */
    public static Snmp createSnmp(String host)
        throws IOException
    {
        //TODO 匿名ポートの確保は可能か？
        Snmp snmp = createSnmp(host, SnmpStackFactory.DEFAULT_TRAP_PORT);
        return snmp;
    }
}
