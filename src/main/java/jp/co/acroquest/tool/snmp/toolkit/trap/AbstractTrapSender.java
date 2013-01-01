// AbstractTrapSender.java ----
// History: 2004/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.trap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

/**
 * TrapSenderインタフェースを実装した抽象クラス。
 *
 * @author akiba
 * @version 1.0
 */
public abstract class AbstractTrapSender implements TrapSender
{
    /** Trap送信先ホスト名。 */
    protected String host_;

    /** Trap送信先ポート番号。 */
    protected int    port_;

    /** Trap送信コミュニティ名。 */
    protected String community_;

    /**
     * TrapSenderに共通のコンストラクタ。
     */
    protected AbstractTrapSender()
    {
        super();
    }

    /**
     * TrapSenderがTrapを送信するホストとポート番号を指定する。
     *
     * @param host Trap送信先ホスト名またはIPアドレス。
     * @param port Trap送信先ポート番号。
     * @throws SnmpToolkitException 送信先の設定に失敗した場合。
     */
    public void setTarget(String host, int port) throws SnmpToolkitException
    {
        this.host_ = host;
        this.port_ = port;
    }

    /**
     * Trap送信コミュニティ名を設定する。
     *
     * @param community Trap送信コミュニティ名。nullを指定した場合はpublicになる。
     */
    public void setCommunity(String community)
    {
        Log log = LogFactory.getLog(TrapSender.class);
        if (community == null)
        {
            this.community_ = "public";
        }
        else
        {
            this.community_ = community;
        }
        log.debug("Community is set to " + this.community_);
    }

    /**
     * Trap送信先のホスト名またはIPアドレスを取得する。
     *
     * @return Trap送信先のホスト名またはIPアドレス。
     */
    protected String getHost()
    {
        return this.host_;
    }

    /**
     * Trap送信先のポート番号を取得する。
     *
     * @return Trap送信先のポート番号。
     */
    protected int getPort()
    {
        return this.port_;
    }

    /**
     * Trap送信時のコミュニティ名を取得する。
     *
     * @return Trap送信時のコミュニティ名。
     */
    protected String getCommunity()
    {
        return this.community_;
    }
}
