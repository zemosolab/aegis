pipeline {
  agent any
  stages {
    stage('build card-management') {
      steps {
        script {
          build job: 'aegis-backend/card-management/Jenkinsfile', propagate: true, wait: true
        }
      }
    }
    stage('build device-management') {
      steps {
      	script {
          build job: 'aegis-backend/device-management/Jenkinsfile', propagate: true, wait: true
       }
      }
    }
    stage('build event-management') {
      steps {
        script {
          build job: 'aegis-backend/event-management/Jenkinsfile', propagate: true, wait: true
        }
      }
    }
    stage('build user-management') {
      steps {
        script {
          build job: 'aegis-backend/user-management/Jenkinsfile', propagate: true, wait: true
        }
      }
    }
  }
}
