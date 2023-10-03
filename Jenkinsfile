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

//        stage('Docker Build') {
//            steps {
//                script {
//                    // Docker 이미지 빌드
//                    def dockerImageName = "cicd-test:latest"
//                    def dockerfilePath = "./Dockerfile"
//                    sh "sudo docker build -t ${dockerImageName} -f ${dockerfilePath} ."
//                }
//            }
//            post {
//                success {
//                    echo 'Docker build success'
//                }
//                failure {
//                    echo 'Docker build failed'
//                }
//            }
//        }

        stage('Docker build') {
            steps {
                sh """
                    sudo docker build -t ${ECR_IMAGE}:$BUILD_NUMBER -f Dockerfile .
                    sudo docker tag ${ECR_IMAGE}:$BUILD_NUMBER ${ECR_IMAGE}:latest
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
                script {
//                    docker.withRegistry("https://${ECR_PATH}", "ecr:${REGION}:${AWS_CREDENTIAL_ID}") {
//                        docker.image("${ECR_IMAGE}:${BUILD_NUMBER}").push()
//                        docker.image("${ECR_IMAGE}:latest").push()
//                    }
                }

                sh """
                    sudo docker push ${ECR_PATH}:${ECR_IMAGE}:${BUILD_NUMBER}
                    sudo docker push ${ECR_PATH}:${ECR_IMAGE}:latest
                """
            }
        }

        stage('CleanUp images') {
            steps {
                sh"""
                sudo docker rmi ${ECR_PATH}/${ECR_IMAGE}:v$BUILD_NUMBER
                sudo docker rmi ${ECR_PATH}/${ECR_IMAGE}:latest
                """
            }
        }

//        stage('Deploy to k8s'){
//            withKubeConfig([credentialsId: "{EKS_JENKINS_CREDENTIAL_ID}",
//                            serverUrl: "${EKS_API}",
//                            clusterName: "${EKS_CLUSTER_NAME}"]){
//                sh "sed 's/IMAGE_VERSION/v${env.BUILD_ID}/g' service.yaml > output.yaml"
//                sh "aws eks --region ${REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}"
//                sh "kubectl apply -f output.yaml"
//                sh "rm output.yaml"
//            }
//        }
    }
}