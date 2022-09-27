def call() {
    node {
        env.APP_TYPE = "golang"
        common.lintChecks()
        common.sonarCheck()
        common.testCases()
        if(env.TAG_NAME != null) {
        common.artifacts()
        }
    }
}

// def call() {     // call is the default which will be called
// pipeline {
//     agent any 
//     environment { 
//         SONAR = credentials('sonar')
//     }
//     stages {
//         // This should run for every commit on feature branch
            
//         stage('Test Cases') {
//             parallel {
//                 stage('Unit Testing') {
//                     steps {
//                         // mvn test or npm test
//                         sh "echo Unit Testing Completed"
//                     }
//                 }
//                 stage('Integration Testing') {
//                     steps {
//                         // mvn verify or npm verify
//                         sh "echo Integration Testing Completed"
//                     }
//                 }
//                 stage('Function Testing') {
//                     steps {
//                         sh "echo Functional Testing Completed"
//                     }
//                 }
//             }
//         }

//         stage('Build') {
//             steps {
//                 sh "echo Doing build"
//                }
//             }
//         } // end of the stages
//     }  // end of the pipeline
// }  // end of function call 