def call() {
    node {
        env.APP_TYP = "nodejs"
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
//         NEXUS = credentials('nexus')
//     }
//     stages {
//         // This should run for every commit on feature branch
//         stage('Lint checks') {
//             steps {
//                 script {
//                      lintChecks()
//                     }
//                 }
//             }
//         stage('Sonar Code Quality Check') {
//             steps {
//                 script {
//                      common.sonarCheck()
//                     }
//                 }
//             }

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
// // Checking the presense of the artifact on nexus
//         stage('Checking the release') {
//              when { 
//                expression { env.TAG_NAME != null }
//                 }              
//              steps {   
//                 script {
//                     env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl http://172.31.8.134:8081/service/rest/repository/browse/${COMPONENT}/ |grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                     print UPLOAD_STATUS
//                 }
//             }
//         }

// // Preparing an artifact with that tag should only happen if it doesn't exist on NEXUS.

//         stage('Prepare Artifacts') {
//             when { 
//                expression { env.TAG_NAME != null }
//                expression { env.UPLOAD_STATUS == "" }
//                 }   
//             steps {
//                 sh "npm install"   // Generates the nodes_modules
//                 sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js" 
//                 sh "echo Artifacts Preparation Completed................!!!"
//             }
//         }

//         stage('Uploading Artifacts') {
//             when { 
//                expression { env.TAG_NAME != null }
//                expression { env.UPLOAD_STATUS == "" }
//             }   
//             steps {
//                sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.8.134:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
//                // Curl returns failure when failed when you use -f
//                }
//             }
//         } // end of the stages
//     }  // end of the pipeline
// }  // end of function call 
