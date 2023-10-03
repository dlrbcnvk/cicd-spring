pipeline {
    agent any

    environment {
        REGION = 'ap-northeast-2';
        ECR_PATH = '943822899858.dkr.ecr.ap-northeast-2.amazonaws.com'
        ECR_IMAGE = 'cicd_spring_repository'
        AWS_CREDENTIAL_ID = 'jenkins-aws-credentials'
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

        stage('github clone') {
            steps {
                checkout scmGit(
                        branches: [[name: '*/main']],
                        extensions: [submodule(parentCredentials: true,reference: '', trackingSubmodules: true)],
                        userRemoteConfigs: [[credentialsId: 'cicd-test', url: 'https://github.com/dlrbcnvk/cicd-submodule']]
                )
                sh 'cd pwd; ls -al;'
            }
        }

//        stage('Add submodule') {
//            steps {
//                sh 'git config --global --add safe.directory /var/lib/jenkins/workspace/cicd-pipeline'
//                sh
//            }
//        }


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

        stage('Docker build') {
            steps {
                sh """
                    sudo docker build -t ${ECR_PATH}/${ECR_IMAGE}:${BUILD_NUMBER} -f Dockerfile .
                    sudo docker tag ${ECR_PATH}/${ECR_IMAGE}:${BUILD_NUMBER} ${ECR_PATH}/${ECR_IMAGE}:latest
                """
            }

            post {
                success {
                    echo 'success dockerizing project'
                }
                failure {
                    error 'fail dockerizing project' // exit pipeline
                }
            }
        }

        stage('Push to ECR') {
            steps {
                sh """
                    sudo docker push ${ECR_PATH}/${ECR_IMAGE}:${BUILD_NUMBER}
                    sudo docker push ${ECR_PATH}/${ECR_IMAGE}:latest
                """
            }
        }

        stage('CleanUp images') {
            steps {
                sh"""
                sudo docker rmi ${ECR_PATH}/${ECR_IMAGE}:${BUILD_NUMBER}
                sudo docker rmi ${ECR_PATH}/${ECR_IMAGE}:latest
                """
            }
        }
    }
}