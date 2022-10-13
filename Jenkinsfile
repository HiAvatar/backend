pipeline {
    agent any

    environment {
        IMAGE_NAME = 'backend-spring'
	CONTAINER_NAME = 'backend-spring-app'
        DOCKERHUB_CREDENTIAL = 'docker-hub'
	DOCKERHUB_ID = 'parkminho'
	DEPLOY_PATH = '/var/jenkins_home/deploy'
        dockerImage = ''
    }

    stages {
        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'
                dir('.') {
		    sh "cp ${DEPLOY_PATH}/application.yml ./src/main/resources"
                    sh './gradlew clean build'
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Build Docker') {
            steps {
                echo 'Build Docker'
                script {
                    dockerImage = docker.build "${DOCKERHUB_ID}/${IMAGE_NAME}"
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                script {
                    docker.withRegistry('', "${DOCKERHUB_CREDENTIAL}") {
                        dockerImage.push("latest")
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Pull Docker Image & Docker Run'
                dir("${DEPLOY_PATH}") {
                    sh "docker pull ${DOCKERHUB_ID}/${IMAGE_NAME}"
                    sh "docker-compose stop ${CONTAINER_NAME}"
		    sh "docker-compose up -d"
		    sh "docker image prune -f"
                }
            }
        }
    }
	
		post {
		    success {
		        slackSend (channel: '#jenkins-log', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
		    }
		    failure {
		        slackSend (channel: '#jenkins-log', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
		    }
		}
}
