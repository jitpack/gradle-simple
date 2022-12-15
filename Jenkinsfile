pipeline {
    agent any

    tools {
        gradle "gradle7"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHub_token', url: 'https://github.com/vgtstl/java-hello-world-with-gradle.git']]])
               }
        }
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }
    }
}
