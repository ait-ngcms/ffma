#/bin/bash
./mongod -v --logpath /var/log/mongodb/server1.log --logRotate reopen --logappend --rest --port 8060 --dbpath ../../data/db &

