pipeline {
    agent any

    environment {
        IMAGE_NAME = 'backend-spring'
				CONTAINER_NAME = 'backend-spring-app'
        DOCKERHUB_CREDENTIAL = 'docker-hub'
				DOCKERHUB_ID = 'parkminho'
				GITHUB_REPOSITORY_CREDENTIAL = 'facam_ci_cd'
				GITHUB_REPOSITORY_URL = 'https://github.com/HiAvatar/backend.git'
				GITHUB_REPOSITORY_SSH = 'git@github.com:HiAvatar/backend.git'
				GITHUB_TARGET_BRANCH = 'develop'
        dockerImage = ''
    }

    stages {
        stage('Cloning Git Repository') {
            steps {
                echo 'Cloning Repository'
                git url: "${GITHUB_REPOSITORY_SSH}",
                branch: "${GITHUB_TARGET_BRANCH}",
                credentialsId: "${GITHUB_REPOSITORY_CREDENTIAL}"
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
           	    failure {
                    error 'This pipeline stops here...'
                }
            }
       }

        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'
                dir('.') {
										sh 'cp /var/jenkins_home/deploy/application.yml ./src/main/resources'
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
                dir('.') {
                    sh "docker pull ${DOCKERHUB_ID}/${IMAGE_NAME}"
                    sh "docker stop ${CONTAINER_NAME} || true"
                    sh "docker run -p 8080:8080 --rm -d --network ec2-user_default --name ${CONTAINER_NAME} ${DOCKERHUB_ID}/${IMAGE_NAME}"
                    sh "docker logs ${CONTAINER_NAME}"
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
