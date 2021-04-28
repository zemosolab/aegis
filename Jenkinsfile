pipeline {
	agent any
	stages {
		stage('build card-management') {
			steps {
				script {
					mainJenkinsFile = load "aegis_backend/card_management/Jenkinsfile"
					mainJenkinsFile.runjob()
				}
			}
		}
		stage('build device-management') {
			steps {
				script {
					mainJenkinsFile = load "aegis_backend/device_management/Jenkinsfile"
					mainJenkinsFile.runjob()
				}
			}
		}
		stage('build event-management') {
			steps {
				script {
					mainJenkinsFile = load "aegis_backend/event_management/Jenkinsfile"
					mainJenkinsFile.runjob()
				}
			}
		}
		stage('build user-management') {
			steps {
				script {
				mainJenkinsFile = load "aegis_backend/user_management/Jenkinsfile"
				mainJenkinsFile.runjob()
				}
			}
		}
	}
}
