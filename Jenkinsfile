pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'ap-northeast-2'
        AWS_ACCESS_KEY_ID = credentials('your-aws-access-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('your-aws-secret-access-key')
        ECR_REGISTRY = 'your-ecr-registry'
        DOCKER_IMAGE_TAG = 'your-docker-image-tag'
        DOCKER_IMAGE_NAME = 'your-docker-image-name'
    }

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

        stage('Build image') {
            app = docker.build("943822899858.dkr.ecr.ap-northeast-2.amazonaws.com/cicd_spring_repository")
        }

        stage('Push image') {
            sh 'rm  ~/.dockercfg || true'
            sh 'rm ~/.docker/config.json || true'

            docker.withRegistry('https://943822899858.dkr.ecr.ap-northeast-2.amazonaws.com', 'ecr:ap-northeast-2:jenkins-aws-credentials') {
                app.push("${env.BUILD_NUMBER}")
                app.push("latest")
            }
        }
    }
}