pipeline {
	agent any
	stages {
		stage('clone code from repo'){
			steps{
				git 'https://github.com/zemoso-int/aegis.git'
			}
		}
		stage('build card-management') {
			steps {
				script {
					dir('aegis_backend') {
						script {
							sh 'pwd'
						}
						build job: 'card_management', propagate: true, wait: true
					}
				}
			}
		}
		stage('build device-management') {
			steps {
				script {
					dir('aegis_backend/device_management') {
						script {
							sh 'pwd'
						}
						build job: 'Jenkinsfile', propagate: true, wait: true
					}
				}
			}
		}
		stage('build event-management') {
			steps {
				script {
					dir('aegis_backend/event_management') {
						script {
							sh 'pwd'
						}
						build job: 'Jenkinsfile', propagate: true, wait: true
					}
				}
			}
		}
		stage('build user-management') {
			steps {
				script {
					dir('aegis_backend/user_management') {
						script {
							sh 'pwd'
						}
						build job: 'Jenkinsfile', propagate: true, wait: true
					}
				}
			}
		}
	}
}
