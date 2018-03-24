pipeline {
   agent any
	
   stages {
       stage('Build') {
           steps {
               echo "Building"
	       sh 'mvn -f phaseC/BlackSheep/pom.xml'
               sh 'mvn compile'
               sh 'mvn package'
           }
       }
       stage('Test'){
           steps {
               echo "Testing"
	       sh 'mvn -f phaseC/BlackSheep/pom.xml'
               sh 'mvn test'
           }
       }
    }
}

