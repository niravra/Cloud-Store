 # <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 # <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 # <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 # <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 
AMI="ami-cd0f5cb6"
INSTANCE_TYPE="t2.micro"
KEYPAIR_NAME="AKIAI4HK3HQTPYAV5HAA"

aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "my sg" --vpc-id vpc-623c861b
SECURITY_ID=$(aws ec2 describe-security-groups --group-names csye6225-fall2017-webapp | grep "GroupId" | awk '{print$2}' | sed -e 's/^"//' -e 's/"$//')
echo $SECURITY_ID
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 22 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 80 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 443 --cidr 10.0.2.15/24
echo "Launching EC2 instance"
INSTANCE_ID=$(aws ec2 run-instances --image-id $AMI --count 1 --instance-type $INSTANCE_TYPE --key-name $KEYPAIR_NAME --security-group-ids $SECURITY_ID --region us-east-1 | grep InstanceId | awk '{print$2}' | tr -cd '[:alnum:]\n-')
echo $INSTANCE_ID
echo "Checking if  instance has reached running state"
aws ec2 wait instance-running --instance-ids $INSTANCE_ID
#status=1
#while [ $status == 1 ]; do
#    INSTANCE_STATUS=$(aws ec2 describe-instance-status --instance-id $INSTANCE_ID | grep -A3 "InstanceState" | grep "Name" | awk '{print$2}' | sed -e 's/^"//' -e 's/"$//')
#    if [ "$INSTANCE_STATUS" == "running" ]; then
#        status=0
#        echo "Instance is running!"
#    else
#	echo "Instance is still not in running state, wait for 30 seconds before checking again."
#	sleep 30
#    fi
#done

echo "Creating General Purpose SSD Volume"
VOLUME_ID=$(aws ec2 create-volume --size 16 --region us-east-1 --availability-zone us-east-1c --volume-type gp2 | grep VolumeId | awk '{print$2}' | tr -cd '[:alnum:]\n-')

echo "Getting the public IP of the instance"
PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID | grep PublicIpAddress | awk '{print$2}' | tr -cd '[:alnum:]\n.')
echo $PUBLIC_IP

echo "Enabling termination protection"
aws ec2 modify-instance-attribute --instance-id $INSTANCE_ID --disable-api-termination "{\"Value\": true}"

echo "Configuring Route 53 and pointing it to above IP address"

jq '.Changes[0].ResourceRecordSet.ResourceRecords[0].Value = "'$PUBLIC_IP'"' /home/sumedh/code/csye6225-fall2017-1/infrastructure/scripts/change-resource-record-sets.json > tmp.$$.json && mv tmp.$$.json /home/sumedh/code/csye6225-fall2017-1/infrastructure/scripts/change-resource-record-sets.json

aws route53 change-resource-record-sets --hosted-zone-id Z3CXV931442NPP --change-batch file:///home/sumedh/code/csye6225-fall2017-1/infrastructure/scripts/change-resource-record-sets.json
