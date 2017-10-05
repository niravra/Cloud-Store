 # <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 # <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 # <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 # <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>

SECURITY_ID=$(aws ec2 describe-security-groups --group-name csye6225-fall2017-webapp | grep GroupId | awk '{print$2}' | tr -cd '[:alnum:]\n-')

aws ec2 modify-instance-attribute --instance-id $1 --disable-api-termination "{\"Value\": false}"

aws ec2 terminate-instances --instance-ids $1
INSTANCE_STATUS=$(aws ec2 describe-instance-status --instance-id $1 | grep "InstanceStatuses" | awk '{print$2}')
echo $INSTANCE_STATUS
statusx=1
while [ $statusx == 1 ]; do
	if [ "$INSTANCE_STATUS" == "[]" ]; then
			[
				statusx=0
			]
		else
			sleep 30
		fi
done
aws ec2 delete-security-group --group-id $SECURITY_ID

aws cloudformation delete-stack --stack-name mystack
