@ECHO OFF
cd src
py ./connect_to_board.py --endpoint a13ssfxeh62jku-ats.iot.eu-central-1.amazonaws.com --key stm.private.key --cert stm.cert.pem
cd ..