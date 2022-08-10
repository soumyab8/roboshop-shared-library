def lintCheck(COMPONENT) {
echo "lint checks started for ${COMPONENT}"
  sh '''
    echo installing jslint
    npm install jslint
    ~/node_modules/jslint/bin/jslint.js server.js || true
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
                     lintCheck(COMPONENT)
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
        stage('Build') {
            steps {
                sh "echo Doing build"
               }
            }
        } // end of the stages
    }  // end of the pipeline
}  // end of function call 
