sudo systemctl stop awslogs.service

cd /var/awslogs/etc

if [-f "awslogs.conf"]
then
	sudo rm awslogs.conf
fi
