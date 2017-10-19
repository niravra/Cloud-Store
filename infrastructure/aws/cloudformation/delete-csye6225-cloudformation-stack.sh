#SECURITY_ID=$(aws ec2 describe-security-groups --group-name stack-WebServerSecurityGroup-N1ZSNN8OPT23 | grep GroupId | awk '{print$2}' | tr -cd '[:alnum:]\n-')

#aws ec2 modify-instance-attribute --instance-id $1 --disable-api-termination "{\"Value\": false}"

#INSTANCE_ID=$(aws ec2 describe-instances | grep InstanceId | awk '{print$2}' | tr -cd '[:alnum:]\n-')

#aws ec2 modify-instance-attribute --instance-id $INSTANCE_ID --disable-api-termination "{\"Value\": false}"

#aws ec2 terminate-instances --instance-ids $INSTANCE_ID

#aws ec2 wait instance-terminated --instance-ids $INSTANCE_ID

#aws ec2 delete-security-group --group-id $SECURITY_ID

aws cloudformation delete-stack --stack-name $1
