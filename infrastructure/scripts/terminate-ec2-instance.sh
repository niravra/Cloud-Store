 # <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 # <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 # <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 # <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>


SECURITY_ID=$(aws ec2 describe-security-groups --group-name csye6225-fall2017-webapp | grep GroupId | awk '{print$2}' | tr -cd '[:alnum:]\n-')

aws ec2 modify-instance-attribute --instance-id $1 --disable-api-termination "{\"Value\": false}"

aws ec2 terminate-instances --instance-ids $1

aws ec2 wait instance-terminated --instance-ids $1

aws ec2 delete-security-group --group-id $SECURITY_ID



