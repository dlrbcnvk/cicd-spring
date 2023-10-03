pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'cicd-test',
                    url: 'https://github.com/dlrbcnvk/cicd-spring.git'
            }
            post {
                success {
                    sh 'echo Successfully Cloned Repository'
                }
                failure {
                    sh 'echo Fail Cloned Repository'
                }
            }
        }


        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build'

                sh 'ls -al ./build/libs'
            }
            post {
                success {
                    echo 'gradle build success'
                }
                failure {
                    echo 'gradle build failed'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Docker 이미지 빌드
                    def dockerImageName = "cicd-test:latest"
                    def dockerfilePath = "./Dockerfile"
                    sh "sudo docker build -t ${dockerImageName} -f ${dockerfilePath} ."
                }
            }
            post {
                success {
                    echo 'Docker build success'
                }
                failure {
                    echo 'Docker build failed'
                }
            }
        }
    }
}