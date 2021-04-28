pipeline {
  agent any
  stages {
    stage('build card-management') {
      steps {
        script {
          mainJenkinsFile = load "aegis-backend/card-management/Jenkinsfile"
          mainJenkinsFile.runjob()
        }
      }
    }
    stage('build device-management') {
      steps {
      	script {
          mainJenkinsFile = load "aegis-backend/device-management/Jenkinsfile"
          mainJenkinsFile.runjob()
       }
      }
    }
    stage('build event-management') {
      steps {
        script {
          mainJenkinsFile = load "aegis-backend/event-management/Jenkinsfile"
          mainJenkinsFile.runjob()
        }
      }
    }
    stage('build user-management') {
      steps {
        script {
          mainJenkinsFile = load "aegis-backend/user-management/Jenkinsfile"
          mainJenkinsFile.runjob()
        }
      }
    }
  }
}
