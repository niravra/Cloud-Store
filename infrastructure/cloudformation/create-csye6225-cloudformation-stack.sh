AMI="ami-cd0f5cb6"
INSTANCE_TYPE="t2.micro"
KEYPAIR_NAME="id_rsa"


aws cloudformation create-stack --stack-name $1 --template-body file:///home/ashwini/.aws/cloudformation/template.json --parameters ParameterKey=ImageId,ParameterValue=$AMI ParameterKey=InstanceType,ParameterValue=$INSTANCE_TYPE ParameterKey=KeyName,ParameterValue=$KEYPAIR_NAME
sleep 15
VOLUME_ID=(aws ec2 create-volume --size 16 --region us-east-1 --availability-zone us-east-1c --volume-type gp2)
PUBLIC_IP=$(aws ec2 describe-instances | grep PublicIpAddress | awk '{print$2}' | tr -cd '[:alnum:]\n.')
echo $PUBLIC_IP

#echo "Configuring Route 53 and pointing it to above IP address"

jq '.Changes[0].ResourceRecordSet.ResourceRecords[0].Value = "'$PUBLIC_IP'"' /home/ashwini/.aws/cloudformation/change-resource-record-sets.json > tmp.$$.json && mv tmp.$$.json /home/ashwini/.aws/cloudformation/change-resource-record-sets.json

aws route53 change-resource-record-sets --hosted-zone-id ZL5MBTWXHBZL1 --change-batch file:///home/ashwini/.aws/cloudformation/change-resource-record-sets.json
