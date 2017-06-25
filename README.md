# crashfinder-plugin
A Jenkins plugin for debugging of regression errors in continuous integration testing based on [crashfinder](https://github.com/heiqs/crashfinder) framework.

# Usage

1. Install jenkins version 1.634

2. Run jenkins: 
	java -jar jenkins.war

3. Install crashfinder in mvn 
	git clone https://github.com/heiqs/crashfinder.git
	cd crashfinder/
	mvn install

4. Install plugin in mvn

	git clone https://github.com/heiqs/crashfinder-plugin.git
	cd crashfinder-plugin/
	mvn install

5. Upload crashFinder.hpi from crashfinder-plugin/target in jenkins

6. Download and install python package "unidiff-0.1"

7. Configure a job in jenkins with needed parameters

8. Build the job

9. Use the reports from crashFinder in jenkins and also genereted files from jenkins build

License
---------
MIT License, see `license.txt` for more information.
