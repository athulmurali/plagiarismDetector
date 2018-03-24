pipeline {
   agent any
	
   tools {
        maven 'maven1' 
    }

   stages {
       stage('Build') {
           steps {
               echo "Building"
	       sh 'mvn -f phaseC/BlackSheep/pom.xml compile'
               sh 'mvn -f phaseC/BlackSheep/pom.xml package'
           }
       }
       stage('Test'){
           steps {
               echo "Testing"
               sh 'mvn -f phaseC/BlackSheep/pom.xml test'
           }
       }
    }
}

