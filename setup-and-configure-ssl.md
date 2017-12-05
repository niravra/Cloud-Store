# csye6225-fall2017
NetWork Structures and Cloud Computing
=======
# Team Members #
* Ashwini Thaokar (thaokar.a@husky.neu.edu)
* Niravra Kar (kar.n@husky.neu.edu)
* Parakh Mahajan (mahajan.p@husky.neu.edu)
* Sumedh Saraf (saraf.s@husky.neu.edu)

# Requirements #
* Get the SSL Certificate
* Configure the SSL Certificate

# Steps #
* Use the free student account to order the ssl certificate from Namecheap
* Save the private key, certificates recieved in a ZIP file via the registerd mail
* Edit the Route 53 access Zone to add the key for genereating the aws certificate ARN
* Add the AWS ARN for the certificiate in the Listener part of load balancer
* Configure the Load Balancer to accept the HTTPS Connection in the port 443
