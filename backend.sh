#!/usr/bin/env bash

SERVICE_NAME='lottery-backend'

CUR_DIR=`pwd`

CMD_FLAG="backend-0.0.1.jar"

SQ_CMD="nohup java -jar ${CMD_FLAG} > ${SERVICE_NAME}-log.out 2>&1 &"

psid_app=0

checkpid() {
    app_ps=`jps -lv | grep "${CMD_FLAG}"`
    if [ -n "$app_ps" ]; then
        psid_app=`echo $app_ps | awk '{print $1}'`
    else
        psid_app=0
    fi
}

start_app() {
    checkpid
    if [ $psid_app -ne 0 ]; then
        echo "================================"
        echo "warn: $SERVICE_NAME already started! (pid=$psid_app)"
        echo "================================"
        return 1
    else
        eval "$SQ_CMD"
        checkpid
        if [ $psid_app -ne 0 ]; then
            echo " $SERVICE_NAME start [Succ] (pid=$psid_app) [OK]"
            return 0
        else
            echo " $SERVICE_NAME start [Failed]"
            return 1
        fi
    fi
}

stop_app() {
    checkpid
    set +e
	if [ $psid_app -ne 0 ]; then
		# try using shutdown
		echo -n "Sending SHUTDOWN to " $SERVICE_NAME " ..."
        kill $psid_app
		TERM_FAILED=0
        i=0
        while kill -0 $psid_app 2>/dev/null; do
            if [ $i -eq 10 ] ; then
                TERM_FAILED=1
                break
            else
                echo -n "."
            fi
            i=$(($i+1))
            sleep 1
        done

		echo

		if [ $TERM_FAILED -eq 0 ] ; then
			# TERM success
			rm -f $psid_app
			return 0
		fi

		# send TERM
		echo -n "Sending TERM to " $SERVICE_NAME " ..."
		kill -TERM $psid_app
		i=0
		TERM_FAILED=0
		while kill -0 $psid_app 2>/dev/null; do
			if [ $i -eq 10 ] ; then
				TERM_FAILED=1
				break
			else
				echo -n "."
			fi
			i=$(($i+1))
			sleep 1
		done
		echo

		if [ $TERM_FAILED -eq 0 ] ; then
			# TERM success
			rm -f $psid_app
			return 0
		fi

		echo -n "Sending KILL to " $SERVICE_NAME " ..."
		kill -KILL $psid_app
		i=0
		KILL_FAILED=0
		while kill -0 $psid_app 2>/dev/null; do
			if [ $i -eq 10 ] ; then
				KILL_FAILED=1
				break
			else
				echo -n "."
			fi
			i=$(($i+1))
			sleep 1
		done
		echo

		if [ $KILL_FAILED -eq 0 ] ; then
			# KILL success
			rm -f $psid_app
			return 0
		fi

		echo "ERROR: KILL $SERVICE_NAME failed!"
		return 1
	else
		echo "WARNING: $SERVICE_NAME is not running!"
		return 0
	fi

}

status_app() {
   checkpid

   if [ $psid_app -ne 0 ];  then
      echo "$SERVICE_NAME is running! (pid=$psid_app)"
   else
      echo "$SERVICE_NAME is not running"
   fi
   return 0
}

help() {
	echo "Usage: $0 {start|stop|restart|status|info|logs}"
	echo "Server: ${SERVICE_NAME}"
	return 0
}

case "$1" in
    'start')
        start_app $2;code=$?
        ;;
    'stop')
        stop_app;code=$?
        ;;
    'restart')
        stop_app
        start_app $2;code=$?
        ;;
    'status')
	    status_app;code=$?
        ;;
    'logs')
        tail -f ${SERVICE_NAME}-log.out
        code=$?
        ;;
    'help')
        help;code=$?
        ;;
    *)
esac

exit $code