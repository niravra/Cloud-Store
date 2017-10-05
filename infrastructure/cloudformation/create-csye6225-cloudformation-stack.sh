
 # <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 # <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 # <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 # <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 



AMI="ami-cd0f5cb6"
INSTANCE_TYPE="t2.micro"
KEYPAIR_NAME="id_rsa"

aws cloudformation create-stack --stack-name mystack --template-body file:///home/ashwini/.aws/cloudformation/template.json

aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "my sg" --vpc-id vpc-6cb45214
SECURITY_ID=$(aws ec2 describe-security-groups --group-names csye6225-fall2017-webapp | grep "GroupId" | awk '{print$2}' | sed -e 's/^"//' -e 's/"$//')
echo $SECURITY_ID

aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 22 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 80 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 443 --cidr 10.0.2.15/24
echo "Launching EC2 instance"

INSTANCE_ID=$(aws ec2 run-instances --image-id $AMI --count 1 --instance-type $INSTANCE_TYPE --key-name $KEYPAIR_NAME --security-group-ids $SECURITY_ID --region us-east-1 | grep InstanceId | awk '{print$2}' | tr -cd '[:alnum:]\n-')
echo $INSTANCE_ID

echo "Getting the public IP of the instance"
PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID | grep PublicIpAddress | awk '{print$2}' | tr -cd '[:alnum:]\n.')
echo $PUBLIC_IP

echo "Configuring Route 53 and pointing it to above IP address"

jq '.Changes[0].ResourceRecordSet.ResourceRecords[0].Value = "'$PUBLIC_IP'"' /home/ashwini/.aws/route53/change-resource-record-sets.json > tmp.$$.json && mv tmp.$$.json /home/ashwini/.aws/route53/change-resource-record-sets.json

aws route53 change-resource-record-sets --hosted-zone-id ZL5MBTWXHBZL1 --change-batch file:///home/ashwini/.aws/route53/change-resource-record-sets.json
