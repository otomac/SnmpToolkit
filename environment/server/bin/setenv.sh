#-----------------------------------------------------------------------------
#
# SnmpToolkitの環境設定ファイル
#
#-----------------------------------------------------------------------------

#-----------------------------------------------------------------------------
# SnmpToolkitを配置した基準ディレクトリ
TOOLKIT_HOME=/home/simagent/snmptoolkit
export TOOLKIT_HOME

#-----------------------------------------------------------------------------
# SnmpToolkitの識別名。
# 複数のSnmpToolkitを同時に起動する場合にプロセス識別名として利用される。
TOOLKIT_NAME=snmptoolkit
export TOOLKIT_NAME

#-----------------------------------------------------------------------------
# Javaのインストールパス
# (元から設定されていれば、そちらを利用する)
if [ -n JAVA_HOME ]; then
  JAVA_HOME=/opt/jdk1.5.0_22
  export JAVA_HOME
fi
JDK_BIN=${JAVA_HOME}/bin
export JDK_BIN

#-----------------------------------------------------------------------------
# 追加のJava起動オプション
JAVA_OPT="-Xms32m -Xmx32m"
export JAVA_OPT

#-----------------------------------------------------------------------------
# クラスパス指定
TOOLKIT_LIB=\
../lib/snmptoolkit.jar:\
../lib/SNMP4J.jar:\
../lib/commons-beanutils-1.8.0.jar:\
../lib/commons-collections-3.2.1.jar:\
../lib/commons-digester-2.0.jar:\
../lib/commons-logging-1.1.1.jar:\
../lib/SuperCSV-1.52.jar:\
../lib/spiffy-with_source-all-0.05.jar:\
../lib/log4j-1.2.15.jar:\
../conf
export TOOLKIT_LIB
