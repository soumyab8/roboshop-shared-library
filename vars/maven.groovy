def lintChecks() {
  sh '''
    echo lint checks starting for ${COMPONENT}
    mvn checkstyle:check || true
    echo lint checks completed for ${COMPONENT}
    '''
}

def sonarCheck(){
    sh '''
    mvn clean compile
    sonar-scanner -Dsonar.host.url=http://172.31.12.77:9000 -Dsonar.sources=. -Dsonar.projectKey=shipping -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.java.binaries=target/classes/
    '''
}

def call() {     //call is the default which will be called
pipeline {
    agent any 
    environment { 
        SONAR = credentials('sonar')
    }
    stages {
        // This should run for every commit on feature branch
        stage('Lint checks') {
            steps {
                script {
                    lintChecks()
                    }
                }
            }

        stage('SONAR checks') {
            steps {
                script {
                    sonarCheck()
                    }
                }
            }
    

        stage('Build') {
            steps {
                sh "echo Doing build"
               }
            }
        } // end of the stages
    }  // end of the pipeline
}  // end of function call 
