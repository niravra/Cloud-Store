AMI="ami-cd0f5cb6"
INSTANCE_TYPE="t2.micro"

export KEYPAIR_NAME=$2

export VpcId=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)
export SubnetId=$(aws ec2 describe-subnets | grep SubnetId | awk '{print$2}' | tr -d '",'| tr '\n' ' ' | cut -d ' ' -f 1)
export SubnetId2=$(aws ec2 describe-subnets | grep SubnetId | awk '{print$2}' | tr -d '",'| tr '\n' ' ' | cut -d ' ' -f 2)
export HostedZoneId=$(aws route53 list-hosted-zones | grep Id | awk '{print$2}' | sed -e 's/^"//' -e 's/",//' | cut -d '/' -f 3)
export HostedZoneName=$(aws route53 list-hosted-zones --query "HostedZones[0].Name" --output text)
export User1=$(aws iam list-users | grep UserName | awk '{print$2}' | tr -d '",'| tr '\n' ' ' | cut -d ' ' -f 8)
export User2=$(aws iam list-users | grep UserName | awk '{print$2}' | tr -d '",'| tr '\n' ' ' | cut -d ' ' -f 9)

aws cloudformation create-stack --stack-name $1 --template-body file://template.json --parameters ParameterKey=ImageId,ParameterValue=$AMI ParameterKey=InstanceType,ParameterValue=$INSTANCE_TYPE ParameterKey=KeyName,ParameterValue=$KEYPAIR_NAME ParameterKey=DBEngine,ParameterValue=MySQL ParameterKey=EngineVersion,ParameterValue=5.6.35 ParameterKey=DBInstanceClass,ParameterValue=db.t2.medium  ParameterKey=DBInstanceIdentifier,ParameterValue=csye6225-fall2017 ParameterKey=MasterUsername,ParameterValue=$3 ParameterKey=MasterPassword,ParameterValue=$4 ParameterKey=DBName,ParameterValue=csye6225 ParameterKey=DynamoDBTableName,ParameterValue=csye6225 ParameterKey=BucketName,ParameterValue=$5 ParameterKey=VpcId,ParameterValue=$VpcId ParameterKey=SubnetId,ParameterValue=$SubnetId ParameterKey=SubnetId2,ParameterValue=$SubnetId2 ParameterKey=HostedZoneId,ParameterValue=$HostedZoneId ParameterKey=HostedZoneName,ParameterValue=$HostedZoneName ParameterKey=User1,ParameterValue=$User1 ParameterKey=User2,ParameterValue=$User2 --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
 
aws cloudformation wait stack-create-complete --stack-name $1
VOLUME_ID=(aws ec2 create-volume --size 16 --region us-east-1 --availability-zone us-east-1c --volume-type gp2)
PUBLIC_IP=$(aws ec2 describe-instances | grep PublicIpAddress | awk '{print$2}' | tr -cd '[:alnum:]\n.')
echo $PUBLIC_IP

