#!/bin/bash
#-----------------------------------------------------------------------------
# snmptoolkit.sh
#   SnmpToolkitのエンジン起動スクリプト
#
# History:
#   2009/07/25 - コメント整理
#   2010/03/01 - Java実行パスを固定化
#   2012/08/29 - パッケージ名変更
#-----------------------------------------------------------------------------

#-----------------------------------------------------------------------------
# スクリプトの配置ディレクトリを調べる
bindir=`dirname ${0}`
basedir=`cd ${bindir}; pwd`

#-----------------------------------------------------------------------------
# 環境定義ファイルを読み込む
source ${basedir}/setenv.sh

#-----------------------------------------------------------------------------
# Agent定義ファイルのパスを定義する
# →スクリプト実行時の引数が指定されていればそちらを優先する
DATA_FILE=data/agent-define.xml
if [ "X$1" != "X" ]; then
  if [ "$1" = "-v" ]; then
    # バージョン表示オプションを指定した場合
    TOOL_OPT="-v"
  else
    # それ以外はAgent定義ファイル名とみなす
    DATA_FILE=$1
    TOOL_OPT=${TOOLKIT_HOME}/${DATA_FILE}
  fi
fi

if [ -z ${TOOL_OPT} ]; then
  TOOL_OPT=${TOOLKIT_HOME}/${DATA_FILE}
fi

export TOOL_OPT

#-----------------------------------------------------------------------------
# SnmpToolkitエンジンを起動する
nohup ${JDK_BIN}/java                                              \
  -D${TOOLKIT_NAME}                                                \
  -Dsnmptoolkit.configPath=${TOOLKIT_HOME}/conf/config.xml         \
  -cp ${TOOLKIT_LIB}                                               \
  ${JAVA_OPT}                                                      \
  jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitImpl                \
    ${TOOL_OPT}
