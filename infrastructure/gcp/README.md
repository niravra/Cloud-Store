# csye6225-fall2017
NetWork Structures and Cloud Computing
=======
# Team Members #
* Ashwini Thaokar (thaokar.a@husky.neu.edu)
* Niravra Kar (kar.n@husky.neu.edu)
* Parakh Mahajan (mahajan.p@husky.neu.edu)
* Sumedh Saraf (saraf.s@husky.neu.edu)
# Google Cloud in aws #

#1. Create an environment variable for the correct distribution:#
	export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)"

#2. Add the Cloud SDK distribution URI as a package source: #
	echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list

#3. Import the Google Cloud public key:#
	curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

#4. Update and install the Cloud SDK: #
	sudo apt-get update && sudo apt-get install google-cloud-sdk

#5. Optionally install any of these additional components: #

	google-cloud-sdk-app-engine-python
	google-cloud-sdk-app-engine-java
	google-cloud-sdk-app-engine-go
	google-cloud-sdk-datalab
	google-cloud-sdk-datastore-emulator
	google-cloud-sdk-pubsub-emulator
	google-cloud-sdk-cbt
	google-cloud-sdk-bigtable-emulator
	kubectl

#6. Run gcloud init to get started:#

	gcloud init
