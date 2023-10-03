pipeline {
    agent any
    stages {
        stage('Checkout') {
            git branch: 'master',
                credentialsId: 'cicd-test'
                url: "https://github.com/dlrbcnvk/cicd-spring.git"
        }

        post {
            success {
                sh 'echo Successfully Cloned Repository'
            }
            failure {
                sh 'echo Fail Cloned Repository'
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
    }
}