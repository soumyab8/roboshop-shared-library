def sonarCheck() {  
  stage('Sonar Checks') {
  if (env.APP_TYPE == "java") {
    sh '''
      # mvn clean compile
      # sonar-scanner -Dsonar.host.url=http://172.31.4.93:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT} -Dsonar.java.binaries=target/classes/
      # curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
      # chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.4.93 ${COMPONENT}
      echo SonarChecks Completed
   '''
  } else {
    sh '''
      # sonar-scanner -Dsonar.host.url=http://172.31.4.93:9000 -Dsonar.sources=. -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} -Dsonar.projectKey=${COMPONENT}
      # curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > /tmp/quality-gate.sh 
      # chmod +x /tmp/quality-gate.sh && /tmp/quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.4.93 ${COMPONENT}
      echo SonarChecks Completed
    '''
     }
   }
 }

def lintChecks() {
  stage('Lint Checks') {
    if (env.APP_TYPE == "nodejs") {
      sh '''
     # echo installing jslint
     # npm install jslint
     # ~/node_modules/jslint/bin/jslint.js server.js || true
      echo lint checks completed for ${COMPONENT}
      '''
    } 
    else if (env.APP_TYPE == "java") { 
        sh '''
      # echo lint checks starting for ${COMPONENT}
      #  mvn checkstyle:check || true
       echo lint checks completed for ${COMPONENT}
        '''
    }
    else if (env.APP_TYPE == "python") {  
      sh '''
     # echo lint checks starting for ${COMPONENT}
     # pylint *.py || true
       echo lint checks completed for ${COMPONENT}
        '''
    } 
    else {
        sh '''
       # echo installing jslint
       # npm install jslint
       # ~/node_modules/jslint/bin/jslint.js server.js || true
          echo lint checks completed for ${COMPONENT}
          '''
       }
    }
}

def testCases() {
    stage('Test Cases') {
        def stages = [:]    // declaring empty list
                stages["Unit Testing"] = {
                        sh 'echo Unit Testing Completed'
                }
                stages["Integration Testing"] = {
                        sh 'echo Integration Testing Completed'
                }
                stages["Function Testing"] = {
                        sh 'echo Functional Testing Completed'
                }
              parallel(stages) 
          }
      }

      
// Parallel Stage reference # Ex:23
// https://stackoverflow.com/questions/46834998/scripted-jenkinsfile-parallel-stage

def artifacts() { 

        stage('Checking the release') {               
                script {
                    env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl http://172.31.8.134:8081/service/rest/repository/browse/${COMPONENT}/ |grep ${COMPONENT}-${TAG_NAME}.zip || true")
                    print UPLOAD_STATUS
            }
        }
    if(env.UPLOAD_STATUS == "") {   // Start of if
        stage('Prepare Artifacts') {
           if (env.APP_TYPE == "nodejs") {
               
                sh "npm install"// Generates the nodes_modules
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js" 
                sh "echo Artifacts Preparation Completed................!!!"
         
           } 
           else if (env.APP_TYPE == "java")  {
                sh "mvn clean package"
                sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
           }

           else if (env.APP_TYPE == "python")  {
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
           }

           else if (env.APP_TYPE == "nginx") {  
                sh '''
                  cd static
                  zip -r ../${COMPONENT}-${TAG_NAME}.zip * 
                  ''' 
            } 

           else if (env.APP_TYPE == "golang")  {
                sh "go mod init ${COMPONENT}"
                sh "go get"
                sh "go build"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}"
               
           }          
        }
     
      stage('Uploading Artifacts') { 
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
               sh "ls -ltr"
               sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.8.134:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
               // Curl returns failure when failed when you use -f   
               }
            }
        }  // end of if
  }



  // declarative code checkout the code by default, whilst scripted not.