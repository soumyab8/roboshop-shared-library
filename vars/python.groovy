def lintChecks() {
  sh '''
    echo lint checks starting for ${COMPONENT}
    pylint *.py || true 
    echo lint checks completed for ${COMPONENT}
    '''
}

def call() {     // call is the default which will be called
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
        stage('Sonar Code Quality Check') {
            steps {
                script {
                     common.sonarCheck()
                    }
                }
            }

        stage('Test cases') {
            parallel{
                stage('Unit testing'){
                    steps{
                    sh "echo Unit testing completed"
                    }
                }

                  stage('Integration testing'){
                    steps{
                    sh "echo Integration testing completed"
                    }
                }

                 stage('Functional testing'){
                    steps{
                    sh "echo Functional testing completed"
                    }
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