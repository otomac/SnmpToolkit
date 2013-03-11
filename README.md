SNMPToolkit
===========

1. はじめに
-----------
SNMPToolkitは、多数のSNMP装置をシミュレートする試験ツール環境です。  
SnmpToolkitは、以下の機能を提供します。

1. SNMP v1/v2c ノードシミュレーション
    1. Get/GetNextオペレーションへの応答
    2. Trap/Notificationの送信
    3. Setオペレーションの対応
2. 複数ノード(要IPアドレス)のシミュレーション環境
3. MIBデータの動的再読み込み

2. インストール
---------------
# 1. 配置
## (1) snmptoolkit-1.x.x.tgz を展開する。
## (2) bin/setenv.sh を開き、初期設定を行う。

```
# SnmpToolkitを配置した基準ディレクトリ
TOOLKIT_HOME=/home/simagent                          ....[a]
export TOOLKIT_HOME

# SnmpToolkitの識別名。
# 複数のSnmpToolkitを同時に起動する場合にプロセス識別名として利用される。
TOOLKIT_NAME=toolkit1                                ....[b]
export TOOLKIT_NAME

# 追加のJava起動オプション
JAVA_HOME=/opt/jdk1.5.0_22                           ....[c]
export JAVA_HOME
```
[a] インストールディレクトリを記述する。  
[b] 適宜、TOOLKIT_NAME変数を設定する。  
	　　(１台のマシンで複数のSnmpToolkitを起動する場合には変更が必要)
[c] JDKのインストールパスを記述する。

## (3) conf/config.xml を開き、IPアドレスとポート番号を設定する。
```
<config>
    <property name="data-dir" value="../data"/>
    <property name="remote-port" value="10000"/>     ....[d]

    <managers>                                       ....[e]
        <manager>udp:192.168.1.100/162</manager>
        <manager>udp:192.168.1.101/162</manager>
    </managers>
</config>
```

[d] このツールのRMI待ち受けポート番号。  
　　Trap送信など、外部コマンドの接続に用いる。  
　　(１台のマシンで複数のSnmpToolkitを起動する場合は、ここを重複しない値にする必要がある)  
[e] Trap送信先ホストとポート番号の指定。  
　　フォーマットは udp:[hostname]/[port] の形式。複数記述可。

3. Agentの定義
--------------
# (1) agent-define.xml を書く。
```
<?xml version="1.0" encoding="EUC-JP"?>
<agents>
<!-- #01-1 -->
<agent address="192.168.101.101"                     ....[f]
       version="v2c"                                 ....[g]
       snmpPort="161"                                ....[h]
       trapPort="162"                                ....[i]
       roCommunity="public"                          ....[j]
       rwCommunity="private"                         ....[k]
       trapCommunity="public">mib-data1.csv</agent>  ....[l][m]
<!-- #01-2 -->
<agent address="192.168.101.102"
       version="v2c"
       snmpPort="161"
       trapPort="162"
       roCommunity="public"
       rwCommunity="private"
       trapCommunity="public">mib-data2.csv</agent>
   ：
</agents>
```
[f] AgentのIPアドレス  
[g] SNMPバージョン [v1/v2c]  
[h] Getを受け付けるポート番号  
[i] Trapを受け付けるポート番号(今は使っていない)  
[j] 読込みコミュニティ(GET/GETNEXT/GETBULK)  
[k] 書き込みコミュニティ(SET)  
[l] Trapコミュニティ(TRAP)  
[m] MIBファイル名。  
　※config.xml内の、data-dirで指定したディレクトリを基準にして探す。

# (2) MIBファイルを書く。
※Trapを送るだけなら、特に無くてよい。空のファイル(★)だけ用意する。  
３列のCSVファイルで、OID,型,値の順に書いていく。  
★ただし、1行だけ、oid,type,value,accessibility という記述が必要
```
oid,type,value,accessibility
1.3.6.1.2.1.1.1.0,string,Sample agent,
1.3.6.1.2.1.1.2.0,object-id,1.3.6.1.4.9999,READ-ONLY
：
```

# (3) 仮想IPアドレス定義
Linuxの場合は、以下のコマンドで作成する。
```
ifconfig eth0:1000 inet 192.168.101.101 netmask 255.255.255.0 up
```
※「eth0:1000」は、「(物理I/F名):(仮想I/F番号)」とする。  
サーバ側にはルーティング設定が必要。Linuxの場合は、以下のコマンドで作成する。
```
route add -net 192.168.0.0/16 gateway (自サーバのIPアドレス)
```
または、
```
route add -net 192.168.0.0/16 eth0
```

4. 起動
-------
インストールしたディレクトリ/binに移動し、以下のコマンドを実行。
```
./snmptoolkit.sh
```
ポート番号が1023以下ならば、rootユーザで実行しなければならない。

起動に成功すると、以下のようにコンソール出力がある。
```
：
listening started: 192.168.xxx.xxx
SnmpToolkit was started at [rmi://(IPアドレス):(RMIポート)].
```
反対に、どちらか／どちらも出ない場合は起動に失敗している。
log/snmptoolkit.log を見て問題を解決すること。

bin/kill_snmptoolkit.sh を実行すると、終了させることができる。

★ここで終了させるプロセスは、setenv.sh でTOOLKIT_NAME環境変数設定した
名前のものである。  
SnmpToolkit を複数起動させるようなケースでは、必ずsetenv.sh を編集して
TOOLKIT_NAMEの値をユニークな名前に設定すること。

5. Trap定義ファイルの作成
-------------------------
以下のXMLファイルを作成し、保存する。  
※以下はv1Trap(LinkDown)の例
```
<traps>
<!-- v1 linkDown Trap -->
    <trap-data version="v1" reqId="1234">            ....[n]
        <generic>2</generic>                         ....[o]
        <specific>0</specific>                       ....[p]
        <enterprise></enterprise>                    ....[q]
        <varbind>
            <oid>1.3.6.1.2.1.2.2.1.1.1</oid>         ....[r]
            <value type="integer">1</value>
        </varbind>
    </trap-data>
</traps>
```
なお、trap-dataタグは繰り返し要素なので、１ファイルに複数のTrapデータを
定義することが可能。  
(1回のコマンド起動で1つのファイルしか送れないので、複数のTrapを一気に送信
する場合は、1ファイル中に複数のTrapデータを書く)

[n] Trapのバージョン。v1/v2cのどちらか。reqIdは必須ではない。  
　　※reqIdを省略した場合は、SnmpToolkit内部が保持する連番を割り当てる。  
[o] generic　  (必須)　linkdownは2、linkupは3、固有Trapは6。  
[p] specific   (必須)　MIB定義ファイル(asn.1)参照。  
[q] enterprise (必須)　MIB定義ファイル(asn.1)参照。  
[r] varbind    (任意)　Trapに含める情報。  
　　※typeに指定できるのは以下の7種類。
```
integer, string, octets, timestamp, ipaddress, object-id, hex
```

★v2Trapの場合：  
※以下はv2Trap(LinkDown)の例
```
<traps>
    <!-- v2Trap (linkDown) -->
    <trap-data version="v2c" reqId="1236">           ....[n]
        <trap-oid>1.3.6.1.6.3.1.1.5.3</trap-oid>     ....[s]
        <enterprise>1.3.6.1.2.1.11</enterprise>      ....[q]
        <!-- ifIndex -->
        <varbind>
            <oid>1.3.6.1.2.1.2.2.1.1.1</oid>         ....[r]
            <value type="integer">1</value>
        </varbind>
        <!-- ifAdminStatus -->
        <varbind>
            <oid>1.3.6.1.2.1.2.2.1.7.1</oid>
            <value type="integer">1</value>
        </varbind>
        <!-- ifOperStatus -->
        <varbind>
            <oid>1.3.6.1.2.1.2.2.1.8.1</oid>
            <value type="integer">2</value>
        </varbind>
    </trap-data>
</traps>
```
※v1Trapと同じ記述項目は同じ英字を振ってある

[n] Trapのバージョン。v1/v2cのどちらか。reqIdは必須ではない。  
　　※reqIdを省略した場合は、SnmpToolkit内部が保持する連番を割り当てる。  
[q] enterprise (必須)　MIB定義ファイル(asn.1)参照。  
[r] varbind    (任意)　Trapに含める情報。  
　　※typeに指定できるのは以下の7種類。
```
integer, string, octets, timestamp, ipaddress, object-id, hex
```
[s] TrapOID。Trapの種類を指定する。

6. Trapの送信
--------------
インストールしたディレクトリ/binに移動し、以下のコマンドを実行。
```
./sendtrap.sh (RMIのURL[u]) (AgentのIPアドレス[v]) (ファイルパス[w]) [(送信間隔[x])]
```
[u] RMIのURL。 rmi://localhost:10000 形式。  
[v] AgentのIPアドレス。ここが送信元AgentのIPアドレスになる。  
　　当然、agent-define.xmlに定義していないと使えない。(無視される)  
[w] ファイルパス。 XMLファイルを指定する。  
[x] Trap送信間隔。  
　　1ファイル中に複数のTrap(<trap-data>～</trap-data>)を記述している場合に、
　　それらの送信間隔をミリ秒単位で指定可能。  
　　省略した場合はウェイト無しで送信する。  
　　※HWやOSの性能にもよるが、おおよそ、10ms以下の精度で送信間隔を細かく制御することは困難。

7. MIBデータのリロード
----------------------
インストールしたディレクトリ/binに移動し、以下のコマンドを実行。
```
./reloadmib.sh (RMIのURL[u]) [(AgentのIPアドレス[v])]
```
[u] RMIのURL。 rmi://localhost:10000 形式。  
[v] AgentのIPアドレス。  
　　省略した場合は、agent-define.xml に定義した全てのIPアドレスが対象となる。  
　　指定する場合は、agent-define.xmlに定義していないと使えない。(無視される)

以上
