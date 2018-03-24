pipeline {
   agent any
	
   tools {
        maven 'maven1' 
        jdk 'jdk8'
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
       stage('SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                        sh 'mvn -f phaseC/BlackSheep/pom.xml clean install'
                        sh 'mvn -f phaseC/BlackSheep/pom.xml sonar:sonar'
                }
            }
        }
            
        stage('Quality') {
          steps {
            sh 'sleep 30'
            timeout(time: 10, unit: 'SECONDS') {
               retry(5) {
                    script {
                    def qg = waitForQualityGate()
                    if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
              }
            }
          }
        }
      }
    }

  }
}

